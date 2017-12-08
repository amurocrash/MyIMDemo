package cn.cmgame.miguimsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Amuro on 2017/10/20.
 */
//
public class IMManager
{
	public static class ConnectArgs implements Serializable
	{
		public String appId;
		public String appKey;
		public Map<String, String> exts;
	}

	public static Context sAppContext;

	public static void initialize(Context context, Class<? extends IMIntentService> callbackClass)
	{
		sAppContext = context.getApplicationContext();
		invokeService(IMService.ACTION_INIT, "callback", callbackClass);
	}

	public static void connect()
	{
		connect(null, null, null);
	}

	public static void connect(Map<String, String> exts)
	{
		connect(null, null, exts);
	}

	public static void connect(String appId, String appKey)
	{
		connect(appId, appKey, null);
	}

	public static void connect(String appId, String appKey, Map<String, String> exts)
	{
		ConnectArgs args = new ConnectArgs();
		if(appId == null || appKey == null)
		{
			//TODO 从manifest中获取参数
			try
			{
				ApplicationInfo appInfo =
						sAppContext.getPackageManager().getApplicationInfo(
								sAppContext.getPackageName(), PackageManager.GET_META_DATA);

				args.appId = appInfo.metaData.getString("MIGU_APP_ID");
				args.appKey = appInfo.metaData.getString("MIGU_APP_KEY");
			}
			catch (Exception e)
			{
				throw new RuntimeException(
						"get appId or appKey with Exception: " + e.getMessage());
			}
		}

		if(exts != null)
		{
			args.exts = exts;
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
