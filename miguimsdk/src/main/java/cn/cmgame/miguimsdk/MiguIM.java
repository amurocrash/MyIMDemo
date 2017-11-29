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
	public static class ConnectArgs implements Serializable
	{
		public String appId;
		public String appKey;
		public Map<String, String> exts;
	}

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

	public static void connect(ConnectArgs args)
	{
		IMManager.connect(args);
	}

	public static void disconnect()
	{
		IMManager.disconnect();
	}

}
