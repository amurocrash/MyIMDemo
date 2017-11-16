package cn.cmgame.miguim.core;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.io.Serializable;

import cn.cmgame.miguim.IMService;
import cn.cmgame.miguim.MiguIM;
import cn.cmgame.miguim.utils.Logger;
import cn.cmgame.miguim.utils.Singleton;

/**
 * Created by Amuro on 2017/10/20.
 */

public class IMCore
{
	private IMCore()
	{
		logger = Logger.getLogger(TAG);
	}

	public static Singleton<IMCore> gDefault = new Singleton<IMCore>()
	{
		@Override
		protected IMCore create()
		{
			return new IMCore();
		}
	};

	public static IMCore getInstance()
	{
		return gDefault.get();
	}


	//监听长连接的各种result，回调给外部
	private class ServiceMsgReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent == null)
			{
				return;
			}

			String action = intent.getAction();
			if (IM_BROADCAST_ACTION.equals(action))
			{
				String result = intent.getStringExtra("result");
				if (SocketManager.RESULT_INIT_SUCCESS.equals(result))
				{

				}
				//connect success
				else if (SocketManager.RESULT_CONNECT_SUCCESS.equals(result))
				{
					if (mConnectionListener != null)
					{
						mConnectionListener.onSuccess();
					}
				}
				//connect failed
				else if (SocketManager.RESULT_CONNECT_FAILED.equals(result))
				{
					if (mConnectionListener != null)
					{
						Bundle data = intent.getBundleExtra("data");
						int errorCode = data.getInt("errorCode");
						String errorMsg = data.getString("errorMsg");

						mConnectionListener.onFailed(errorCode, errorMsg);
					}
				}
				//disconnect
				else if (SocketManager.RESULT_DISCONNECTED.equals(result))
				{
					if (mConnectionListener != null)
					{
						mConnectionListener.onDisconnected();
					}
				}
				//on new message
				else if (SocketManager.RESULT_NEW_MSG.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					String msg = (String) data.get("msg");
					if (mMsgListener != null)
					{
						mMsgListener.onNewMsg(msg);
					}
				}
			}
		}
	}

	private static final String TAG = "imcore";
	private static final String ACTION_SOCKET_RESULT = "action_socket_result";

	private Logger logger;
	public static Context sAppContext;
	public static String IM_BROADCAST_ACTION;
	private ServiceMsgReceiver serviceMsgReceiver;
	private MiguIM.IConnectionListener mConnectionListener;
	private MiguIM.IMsgListener mMsgListener;

	public void initialize(Context context)
	{
		sAppContext = context.getApplicationContext();
		IM_BROADCAST_ACTION = sAppContext.getPackageName() + "_" + ACTION_SOCKET_RESULT;

		//注册广播监听长连接的各种result
		if (serviceMsgReceiver == null)
		{
			serviceMsgReceiver = new ServiceMsgReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(IM_BROADCAST_ACTION);
		sAppContext.registerReceiver(serviceMsgReceiver, filter);

		//启动长连接服务
		invokeService(IMService.ACTION_INIT);
	}

	public void connect(MiguIM.ConnectArgs args,
			MiguIM.IConnectionListener connectionListener, MiguIM.IMsgListener msgListener)
	{
		this.mConnectionListener = connectionListener;
		this.mMsgListener = msgListener;
		invokeService(IMService.ACTION_CONNECT, "connectArgs", args);
	}

	public void disconnect()
	{
		invokeService(IMService.ACTION_DISCONNECT);
	}

	private void invokeService(String action)
	{
		invokeService(action, null, null);
	}

	private void invokeService(String action, String dataKey, Serializable data)
	{
		Intent intent = new Intent();
		ComponentName cn =
				new ComponentName(sAppContext, "cn.cmgame.miguim.IMService");
		intent.setComponent(cn);
		intent.setAction(action);

		if(data != null)
		{
			intent.putExtra(dataKey, data);
		}

		sAppContext.startService(intent);
	}

}
