package cn.cmgame.miguimsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

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
				new ComponentName(sAppContext, "cn.cmgame.miguimsdk.IMService");
		intent.setComponent(cn);
		intent.setAction(action);

		if(data != null)
		{
			intent.putExtra(dataKey, data);
		}

		sAppContext.startService(intent);
	}

}
