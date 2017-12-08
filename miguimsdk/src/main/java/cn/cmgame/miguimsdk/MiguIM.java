package cn.cmgame.miguimsdk;

import android.content.Context;

import java.io.Serializable;
import java.util.Map;

/**
 * interface of the IM framework
 * Created by Amuro on 2017/10/20.
 */
public class MiguIM
{
	public class ErrorCode
	{
		public static final int CONNECT_SUCCESS = 0;
		public static final int NOT_DISCONNECT = 1;
		public static final int CONNECT_EXCEPTION = 2;
		public static final int CONNECT_TIMEOUT = 3;
	}

	public static void initialize(Context context, Class<? extends IMIntentService> callbackClass)
	{
		IMManager.initialize(context, callbackClass);
	}

	public static void connect()
	{
		IMManager.connect();
	}

	public static void connect(String appId, String appKey)
	{
		IMManager.connect(appId, appKey);
	}

	public static void connect(Map<String, String> exts)
	{
		IMManager.connect(exts);
	}

	public static void connect(String appId, String appKey, Map<String, String> exts)
	{
		IMManager.connect(appId, appKey, exts);
	}



	public static void disconnect()
	{
		IMManager.disconnect();
	}

}
