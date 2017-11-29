package cn.cmgame.miguim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.utils.DeviceUtils;
import cn.cmgame.miguim.utils.Logger;

/**
 * Created by Amuro on 2017/10/21.
 */
public class SocketManager
{
	private static final String TAG = "socket_manager";

	public static final String ACTION_INVOKE = "action_invoke";
	public static final String RESULT_INIT_SUCCESS = "result_init_success";
	public static final String RESULT_INIT_FAILED = "result_init_failed";
	public static final String RESULT_CONNECT_SUCCESS = "result_connect_success";
	public static final String RESULT_CONNECT_FAILED = "result_connect_failed";
	public static final String RESULT_DISCONNECTED = "result_disconnected";
	public static final String RESULT_NEW_MSG = "result_new_msg";

	public class ErrorCode
	{
		public static final int CONNECT_SUCCESS = 0;
		public static final int NOT_DISCONNECT = 1;
		public static final int CONNECT_EXCEPTION = 2;
		public static final int CONNECT_TIMEOUT = 3;
	}

	public static class ConnectArgs
	{
		public String appId;
		public String appKey;
		public Map<String, String> exts;
	}

	public static class DeviceInfo
	{
		public static String deviceId;
		public static String imei;
		public static String imsi;
		public static String tel;
		public static String iccid;
		public static String brand;
		public static String model;
	}

	private Logger logger;
	private Context mContext;
	private Class callbackClass;
	private SocketCore socketCore;

	public SocketManager(Context mContext)
	{
		this.mContext = mContext;
		logger = Logger.getLogger(TAG);
	}

	public void publishResult(String result)
	{
		publishResult(result, null);
	}

	public void publishResult(String result, int errorCode, String errorMsg)
	{
		Bundle data = new Bundle();
		data.putInt("errorCode", errorCode);
		data.putString("errorMsg", errorMsg);
		publishResult(result, data);
	}

	public void publishResult(String result, Bundle data)
	{
//		IMIntentService.onNewAction(
//				mContext, callbackClass, IMIntentService.ACTION_INVOKE, result, data);

		Intent intent = new Intent(mContext, callbackClass);
		intent.setAction(ACTION_INVOKE);
		intent.putExtra("result", result);
		if(data != null)
		{
			intent.putExtra("data", data);
		}
		mContext.startService(intent);
	}

	public void init(Class callbackClass)
	{
		this.callbackClass = callbackClass;
		publishResult(RESULT_INIT_SUCCESS);
	}

	public void connect(String appId, String appKey, Map<String, String> exts)
	{
		if(socketCore == null)
		{
			socketCore = new SocketCore(this);

			DeviceInfo.deviceId = DeviceUtils.getDeviceId(mContext);
			DeviceInfo.imei = DeviceUtils.getIMEI(mContext);
			DeviceInfo.imsi = DeviceUtils.getIMSI(mContext);
			DeviceInfo.tel = DeviceUtils.getTel(mContext);
			DeviceInfo.iccid = DeviceUtils.getICCID(mContext);
			DeviceInfo.brand = DeviceUtils.getBrand();
			DeviceInfo.model = DeviceUtils.getModel();
		}

		ConnectArgs args = new ConnectArgs();
		args.appId = appId;
		args.appKey = appKey;
		args.exts = exts;

		socketCore.connect(args);
	}

	public void disconnect()
	{
		if (socketCore != null)
		{
			socketCore.disconnect();
		}
	}
}
