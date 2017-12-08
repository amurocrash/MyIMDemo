package cn.cmgame.miguim.core.packet.disposer;

import java.io.OutputStream;
import java.net.Socket;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.core.packet.receiver.ReceiveMsg;
import cn.cmgame.miguim.utils.Logger;
import cn.cmgame.miguim.utils.StreamUtils;

/**
 * Created by Amuro on 2017/11/7.
 */

/**
 * 客户端发送消息的核心父类
 * @param <T> 对应的协议byte流封装类
 * @see AbsSocketPacket
 */
public abstract class AbsPacketDisposer<T extends AbsSocketPacket>
{
	protected SocketCore socketCore;
	protected Socket socketClient;
	protected OutputStream outputStream;
//	protected MiguIM.ISendingListener sendingListener;
	protected long sendTime = -1;
	protected AbsSocketPacket packetForSend;

	protected Logger logger;

//	public AbsPacketDisposer(T packet, SocketCore core)
//	{
//		this(packet, core, null);
//	}

	public AbsPacketDisposer(T packet, SocketCore core)
	{
		this.packetForSend = packet;
		this.socketCore = core;
		this.socketClient = core.getSocketClient();
//		this.sendingListener = sendingListener;
		this.logger = socketCore.logger;
	}

	public void sendPacket() throws Exception
	{
		realSendPacket();
	}

	protected void realSendPacket() throws Exception
	{
		if (outputStream == null)
		{
			outputStream = socketClient.getOutputStream();
		}

		logger.v(packetForSend.toString());
		StreamUtils.sendPacket(
					outputStream,
					packetForSend.generateFinalData(),
					socketClient.getSendBufferSize());

//		byte[] data = packetForSend.generateFinalData();
//		int bufferSize = socketClient.getSendBufferSize();
//
//		if (data.length <= bufferSize)
//		{
//			outputStream.write(data);
//			outputStream.flush();
//		}
//		else
//		{
//			int totalLength = data.length;
//			int sendLength = 0;
//
//			while (sendLength < totalLength)
//			{
//				int count;
//				if (totalLength - sendLength >= bufferSize)
//				{
//					count = bufferSize;
//				}
//				else
//				{
//					count = totalLength - sendLength;
//				}
//
//				sendLength += count;
//				outputStream.write(data, sendLength, count);
//				outputStream.flush();
//			}
//		}

		this.sendTime = System.currentTimeMillis();
//		packet.setSendTime(System.currentTimeMillis());

	}

	public long getSendTime()
	{
		return sendTime;
	}

	public abstract void onTimeOut();
	public void disposeResp(ReceiveMsg receiveMsg)
	{
		int status = receiveMsg.getParam("status").getAsInt();

		if(status == ReceiveMsg.UnifyRespCode.MSG_RESP_SUCCESS)
		{
			onRespSuccess();
		}
		else
		{
			String msg = receiveMsg.getParam("message").getAsString();
			onRespFailed(status, msg);
		}
	}

	protected void onRespSuccess(){}
	protected void onRespFailed(int status, String msg){}
}
