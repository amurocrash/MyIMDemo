package cn.cmgame.miguim;

import android.content.Context;

import java.io.Serializable;
import java.util.Map;

import cn.cmgame.miguim.core.IMManager;

/**
 * interface of the framework
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

//	public interface IConnectionListener
//	{
//		class ErrorCode
//		{
//			public static final int NOT_DISCONNECT = 1;
//			public static final int CONNECT_EXCEPTION = 2;
//			public static final int CONNECT_TIMEOUT = 3;
//		}
//
//		void onSuccess();
//		void onFailed(int errorCode, String errorMsg);
//		void onDisconnected();
//	}
//
//	public interface ISendingListener
//	{
//		class ErrorCode
//		{
//			public static final int SUCCEED = 1000;
//			public static final int FAILED = 1001;
//			public static final int TIME_OUT = 1002;
//		}
//
//		void onSucceed();
//		void onError(int code, String msg);
//	}
//
//
//	public interface IMsgListener
//	{
//		void onNewMsg(String msg);
//	}

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
