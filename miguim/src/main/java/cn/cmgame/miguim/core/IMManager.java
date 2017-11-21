package cn.cmgame.miguim.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

import cn.cmgame.miguim.IMIntentService;
import cn.cmgame.miguim.IMService;
import cn.cmgame.miguim.MiguIM;

/**
 * Created by Amuro on 2017/10/20.
 */
//
public class IMManager
{
	public static Context sAppContext;

	public static void initialize(Context context, Class<? extends IMIntentService> callbackClass)
	{
		sAppContext = context.getApplicationContext();
		invokeService(IMService.ACTION_INIT, "callback", callbackClass);
	}

	public static void connect(MiguIM.ConnectArgs args)
	{
		if(args == null)
		{
			//TODO 从manifest中获取参数
		}

		invokeService(IMService.ACTION_CONNECT, "connectArgs", args);
	}

	public static void disconnect()
	{
		invokeService(IMService.ACTION_DISCONNECT);
	}

	private static void invokeService(String action)
	{
		invokeService(action, null, null);
	}

	private static void invokeService(String action, String dataKey, Serializable data)
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

//	public static void initialize(Context context)
//	{
//		IMCore.getInstance().initialize(context);
//	}
//
//	public static void connect(MiguIM.ConnectArgs args,
//			MiguIM.IConnectionListener connectionListener, MiguIM.IMsgListener msgListener)
//	{
//		IMCore.getInstance().connect(args, connectionListener, msgListener);
//	}
//
//	public static void disconnect()
//	{
//		IMCore.getInstance().disconnect();
//	}

}
