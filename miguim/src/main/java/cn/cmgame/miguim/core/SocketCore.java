package cn.cmgame.miguim.core;

import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import cn.cmgame.miguim.MiguIM;
import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.core.packet.SerialGenerator;
import cn.cmgame.miguim.core.packet.VerifyPacket;
import cn.cmgame.miguim.core.packet.disposer.AbsPacketDisposer;
import cn.cmgame.miguim.core.packet.HeartbeatPacket;
import cn.cmgame.miguim.core.packet.disposer.PacketDisposerFactory;
import cn.cmgame.miguim.core.packet.UploadInfoPacket;
import cn.cmgame.miguim.core.packet.receiver.PacketReceiver;
import cn.cmgame.miguim.core.packet.receiver.ReceiveMsg;
import cn.cmgame.miguim.core.tokenmanager.ConfigManager;
import cn.cmgame.miguim.utils.ByteArrayUtils;
import cn.cmgame.miguim.utils.Logger;
import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * Created by Amuro on 2017/10/21.
 */

public class SocketCore
{
	private static final String TAG = "socket_core";
	private static final String TOKEN_URL = "http://192.168.1.102:8080/token";

	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_DISCONNECTING = 1;
	public static final int STATE_CONNECTING = 2;
	public static final int STATE_CONNECTED = 3;

	private int state = STATE_DISCONNECTED;

	public static Logger logger;
	private SocketManager mSocketManager;
	private ConfigManager mConfigManager;
	private MiguIM.ConnectArgs connectArgs;

	private Socket socketClient;

	private ConnectionThread mConnectionThread;
	private DisconnectionThread mDisconnectionThread;
	private SendThread mSendThread;
	private ReceiveThread mReceiveThread;

	private SerialGenerator serialGenerator;
	private LinkedBlockingQueue<AbsPacketDisposer> sendingPacketQueue;
	private SendingPktManager sendingPktManager;
	private HeartBeatCountDownTimer heartBeatTimer;


	public SocketCore(SocketManager socketManager)
	{
		this.logger = Logger.getLogger(TAG);
		this.mSocketManager = socketManager;
	}

	public boolean isConnected()
	{
		return state == STATE_CONNECTED;
	}

	public boolean isDisconnected()
	{
		return state == STATE_DISCONNECTED;
	}

	public boolean isDisconnecting()
	{
		return state == STATE_DISCONNECTING;
	}

	public Socket getSocketClient()
	{
		return socketClient;
	}

	public SocketManager getSocketManager()
	{
		return mSocketManager;
	}

	public void connect(final MiguIM.ConnectArgs args)
	{
		//fetched token and token is valid, directly connect socket
		if(mConfigManager != null && mConfigManager.isTokenValid())
		{
			realConnect(args);
			return;
		}

		//fetch token
		if (mConfigManager == null)
		{
			mConfigManager = new ConfigManager(TOKEN_URL);
		}

		mConfigManager.setMock(true);
		mConfigManager.requestToken(new ConfigManager.ITokenListener()
			{
				@Override
				public void onSuccess()
				{
					realConnect(args);
				}

				@Override
				public void onFailed()
				{
					mSocketManager.publishResult(SocketManager.RESULT_CONNECT_FAILED);
				}
			}
		);

	}

	private void realConnect(MiguIM.ConnectArgs args)
	{
		if (!isDisconnected())
		{
			mSocketManager.publishResult(
					SocketManager.RESULT_CONNECT_FAILED,
					MiguIM.ErrorCode.NOT_DISCONNECT, "");

			return;
		}

		this.connectArgs = args;

		if(mConnectionThread == null)
		{
			mConnectionThread = new ConnectionThread();
		}

		mConnectionThread.start();
		state = STATE_CONNECTING;
	}

	public void disconnect()
	{
		if (isDisconnected() || isDisconnecting())
		{
			return;
		}

		if(mDisconnectionThread == null)
		{
			mDisconnectionThread = new DisconnectionThread();
		}
		mDisconnectionThread.start();
		state = STATE_DISCONNECTING;
	}

	private void verify()
	{
		JsonObject params = new JsonObject();
		params.addProperty("device", SocketManager.DeviceInfo.deviceId);
		params.addProperty("appid", connectArgs.appId);
		params.addProperty("token", mConfigManager.getToken());
		VerifyPacket verifyPacket =
				new VerifyPacket(serialGenerator.addAndGet(), params);
		enqueuePacket(verifyPacket);

	}

	public void startHeartbeat()
	{
		if(heartBeatTimer == null)
		{
			heartBeatTimer = new HeartBeatCountDownTimer();
		}

		heartBeatTimer.start();
	}

	public void uploadDeviceInfo()
	{
		JsonObject params = new JsonObject();
		params.addProperty("imei", SocketManager.DeviceInfo.imei);
		params.addProperty("imsi", SocketManager.DeviceInfo.imsi);
		params.addProperty("tel", SocketManager.DeviceInfo.tel);
		params.addProperty("iccid", SocketManager.DeviceInfo.iccid);
		params.addProperty("brand", SocketManager.DeviceInfo.brand);
		params.addProperty("model", SocketManager.DeviceInfo.model);

		JsonObject exts = null;
		if(connectArgs.exts != null)
		{
			exts = new JsonObject();
			for(Map.Entry<String, String> entry : connectArgs.exts.entrySet())
			{
				exts.addProperty(entry.getKey(), entry.getValue());
			}
		}

		UploadInfoPacket uploadInfoPacket =
				new UploadInfoPacket(serialGenerator.addAndGet(), params, exts);
		enqueuePacket(uploadInfoPacket);
	}

	private void enqueuePacket(AbsSocketPacket packet)
	{
		try
		{
			AbsPacketDisposer packetDisposer
					= PacketDisposerFactory.getPacketDisposer(packet, this);

			sendingPktManager.put(packet.getSerial(), packetDisposer);
			sendingPacketQueue.put(packetDisposer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**************************连接线程**************************/
	private class ConnectionThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				if(socketClient == null)
				{
					socketClient = new Socket();
				}

				socketClient.setKeepAlive(true);
				socketClient.connect(
						new InetSocketAddress(
								mConfigManager.getServerIp(),
								mConfigManager.getPort()),
								mConfigManager.getTimeOut());

				state = STATE_CONNECTED;
				logger.v("connect server success");

				if(sendingPacketQueue == null)
				{
					sendingPacketQueue = new LinkedBlockingQueue<>();
				}

				if(mSendThread == null)
				{
					mSendThread = new SendThread();
				}

				if(mReceiveThread == null)
				{
					mReceiveThread = new ReceiveThread();
				}

				mSendThread.start();
				mReceiveThread.start();

				if(serialGenerator == null)
				{
					serialGenerator = new SerialGenerator();
				}
				if(sendingPktManager == null)
				{
					sendingPktManager = new SendingPktManager();
				}
				sendingPktManager.start();

				verify();
			}
			catch (Exception e)
			{
				logger.v("connect exception: " + e.getMessage());

				mSocketManager.publishResult(
						SocketManager.RESULT_CONNECT_FAILED,
						MiguIM.ErrorCode.CONNECT_EXCEPTION, e.getMessage());

				disconnect();
			}
		}
	}

	/**************************断连线程***************************/
	private class DisconnectionThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				mConnectionThread = null;

				if(mSendThread != null)
				{
					mSendThread.forceStop = true;
					mSendThread = null;
				}

				if(mReceiveThread != null)
				{
					mReceiveThread.forceStop = true;
					mReceiveThread = null;
				}

				if(heartBeatTimer != null)
				{
					heartBeatTimer.forceStop();
					heartBeatTimer = null;
				}

				if(sendingPacketQueue != null)
				{
					sendingPacketQueue.clear();
					sendingPacketQueue = null;
				}

				serialGenerator = null;

				if(sendingPktManager != null)
				{
					sendingPktManager.forceStop();
					sendingPktManager = null;
				}

//				PacketDisposerFactory.getInstance().destroy();

				socketClient.getOutputStream().close();
				socketClient.getInputStream().close();
				socketClient.close();

			}
			catch (Exception e)
			{
				logger.v("disconnect exception: " + e.getMessage());
			}
			finally
			{
				socketClient = null;
				state = STATE_DISCONNECTED;
				mSocketManager.publishResult(SocketManager.RESULT_DISCONNECTED);
				mDisconnectionThread = null;
			}
		}
	}


	/*************************发送线程***************************/
	private class SendThread extends Thread
	{
		private boolean forceStop;

		@Override
		public void run()
		{
			while (isConnected() && !forceStop)
			{
				try
				{
					AbsPacketDisposer packetDisposer = sendingPacketQueue.take();
					packetDisposer.sendPacket();
				}
				catch (Exception e)
				{
					logger.v("send msg failed: " + e.getMessage());
					//TODO 处理发送消息时发生的异常
//					LogUtils.e("发送消息失败: " + e.getMessage());
//					if(packet.getSendingListener() != null)
//					{
//						H.sendMessage(
//								H.makeMessage(SocketHandler.WHAT_SEND_MSG_ERROR,
//										ISendingListener.ErrorCode.FAILED,
//										e.getMessage(),
//										packet.getSendingListener()));
//					}

				}
			}
		}
	}

	/*************************接收线程***************************/
	private class ReceiveThread extends Thread
	{
		private boolean forceStop;
		private InputStream inStream;

		@Override
		public void run()
		{
			while(isConnected() && !forceStop)
			{
				try
				{
					if (inStream == null)
					{
						inStream = socketClient.getInputStream();
					}

					BufferedInputStream bis = new BufferedInputStream(inStream);

					byte[] bufferLength = new byte[4]; //长度字节数组
					byte[] bufferBody = null; //数据体
					int len_readed = 0; //已读索引
					int body_readed = 0;
					int body_length = 0;

					boolean body_read_start = false; //是否开始读取数据体

					while (!forceStop)
					{
						//有数据到达
						if (bis.available() > 0)
						{
							if (len_readed <= 3)
							{
								bufferLength[len_readed] = (byte) bis.read(); //读取1个字节
								len_readed++;

								if (len_readed == 4)
								{
									body_read_start = true;
									body_length = ByteArrayUtils.bytesToInt(bufferLength, 0);
									bufferBody = new byte[body_length]; //初始化数据体
								}
							}

							if (body_read_start)
							{
								//已开始读取数据体
								bufferBody[body_readed] = (byte) bis.read(); //读取1个字节
								body_readed++;

								//判断数据体是否读满
								if (body_readed == body_length)
								{
									// 处理body
									byte[] buffer = new byte[4 + body_length];
									System.arraycopy(bufferLength, 0, buffer, 0, 4);
									System.arraycopy(bufferBody, 0, buffer, 4, body_length);

									sendingPktManager.disposeResp(buffer);

									// 重置
									bufferBody = null;
									len_readed = 0;
									body_readed = 0;
									body_length = 0;
									body_read_start = false;
								}
							}
						}
					}


				}
				catch (Exception e)
				{
					logger.v("receive msg failed: " + e.getMessage());
				}
			}
		}
	}

	/****************************心跳timer********************************/
	//注意这玩意儿是跑在Connection线程的
	public class HeartBeatCountDownTimer
	{
		private boolean forceStop = false;

		private Timer timer;
//		public boolean isLastHeatBeatResp = true;

		private void start()
		{
			final long interval = mConfigManager.getHeartBeatInterval();

			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					if(isConnected() && !forceStop)
					{
						HeartbeatPacket heartbeatPacket =
								new HeartbeatPacket(serialGenerator.addAndGet());
						enqueuePacket(heartbeatPacket);
					}
				}
			}, interval, interval);
			logger.v("start heartbeat");

		}

		protected void forceStop()
		{
			this.forceStop = true;
			if(timer != null)
			{
				timer.cancel();
				timer.purge();
			}
		}

	}

	/***************************缓存所有发送消息，控制超时******************/
	private class SendingPktManager
	{
		private boolean forceStop = false;
		private Timer timer;

		private void forceStop()
		{
			forceStop = true;

			if(timer != null && allPacketMap != null)
			{
				allPacketMap.clear();
				timer.cancel();
				timer.purge();
			}
		}

		private ConcurrentHashMap<Integer, AbsPacketDisposer> allPacketMap;


		private SendingPktManager()
		{
			allPacketMap = new ConcurrentHashMap<>();
		}

		private void start()
		{
			timer = new Timer();
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					if(isConnected() && !forceStop)
					{
						scanAllPkt();
					}
				}
			}, mConfigManager.getSendMsgTimeout(), mConfigManager.getSendMsgTimeout());
		}


		private void put(int serial, AbsPacketDisposer packetDisposer)
		{
			allPacketMap.put(serial, packetDisposer);
		}

		private void disposeResp(byte[] originalData) throws Exception
		{
			ReceiveMsg receiveMsg = PacketReceiver.parsePacket(originalData);
			if(AbsSocketPacket.TYPE_SYSTEM == receiveMsg.getMsgType())
			{
				if("response".equals(receiveMsg.getOperate()))
				{
					AbsPacketDisposer disposer =
							allPacketMap.remove(
									receiveMsg.getParam("sequence").getAsInt());
					disposer.disposeResp(receiveMsg);
				}
				else
				{
					//目前服务端还没有其他的系统消息，先留着
					logger.v("fxxking system msg with operate: " +
							receiveMsg.getOperate() + " which can't be disposed.");
				}
			}
			else if(AbsSocketPacket.TYPE_BIZ == receiveMsg.getMsgType())
			{
				Bundle data = new Bundle();
				data.putString("msg", receiveMsg.getBaseJson());
				mSocketManager.publishResult(
						SocketManager.RESULT_NEW_MSG, data);
			}
			else
			{
				throw new Exception(
						"can't dispose the msg which type is " + receiveMsg.getMsgType());
			}

//			ReceiveMsg response = PacketReceiver.parsePacket(buffer);
//			if (response.getType() == AbsSocketPacket.TYPE_UNIFIED_RESP)
//			{
//				AbsSocketPacket packet = allPacketMap.remove(response.getSerial());
//				packet.getSender().disposeResp(response.getContent(), packet.getSendingListener());
//			}
//			else if(response.getType() == AbsSocketPacket.TYPE_BIZ_SELF)
//			{
//				LogUtils.e("收到服务端的serial：" + response.getSerial());
//				APacketDisposer disposer =
//						PacketDisposerFactory.getInstance().getPacketDisposer(response.getType(), 0);
//				disposer.init(SocketCore.this);
//				disposer.onMsgArrived(response.getContent(), response.getSerial());
//			}

		}

		private void scanAllPkt()
		{
			if(allPacketMap.size() == 0)
			{
				return;
			}

			long currentTime = System.currentTimeMillis();

			for(AbsPacketDisposer disposer : allPacketMap.values())
			{
				if(disposer.getSendTime() == -1)
				{
					//消息尚未发送，不处理
					continue;
				}

				long timeDiff = currentTime - disposer.getSendTime();
				if(timeDiff >= (mConfigManager.getSendMsgTimeout() - 1000))
				{
					disposer.onTimeOut();
				}
			}
		}
	}
}
