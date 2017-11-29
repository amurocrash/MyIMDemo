package cn.cmgame.miguimsdk;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

/**
 * Created by Amuro on 2017/11/17.
 */

public abstract class IMIntentService extends IntentService
{
	private static final String ACTION_INVOKE = "action_invoke";
	private static final String RESULT_INIT_SUCCESS = "result_init_success";
	private static final String RESULT_INIT_FAILED = "result_init_failed";
	private static final String RESULT_CONNECT_SUCCESS = "result_connect_success";
	private static final String RESULT_CONNECT_FAILED = "result_connect_failed";
	private static final String RESULT_DISCONNECTED = "result_disconnected";
	private static final String RESULT_NEW_MSG = "result_new_msg";

	private Handler handler;

	public IMIntentService()
	{
		super("IMIntentService");
		handler = new Handler(Looper.getMainLooper());
	}

//	public static void onNewAction(
//			Context mContext, Class<? extends IMIntentService> callbackClass,
//			String action, String result, Bundle data)
//	{
//		Intent intent = new Intent(mContext, callbackClass);
//		intent.setAction(action);
//		intent.putExtra("result", result);
//		if(data != null)
//		{
//			intent.putExtra("data", data);
//		}
//		mContext.startService(intent);
//	}

	@Override
	protected void onHandleIntent(@Nullable final Intent intent)
	{
		if(intent == null)
		{
			return;
		}

		String action = intent.getAction();
		if(!ACTION_INVOKE.equals(action))
		{
			return;
		}

		final String result = intent.getStringExtra("result");
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				if (RESULT_INIT_SUCCESS.equals(result))
				{
					onInit(true);
				}
				else if (RESULT_CONNECT_SUCCESS.equals(result))
				{
					onConnect(MiguIM.ErrorCode.CONNECT_SUCCESS, "success");
				}
				else if (RESULT_CONNECT_FAILED.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					int errorCode = data.getInt("errorCode");
					String errorMsg = data.getString("errorMsg");
					onConnect(errorCode, errorMsg);
				}
				else if (RESULT_DISCONNECTED.equals(result))
				{
					onDisconnect();
				}
				else if (RESULT_NEW_MSG.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					String msg = (String) data.get("msg");
					onNewMsg(msg);
				}
			}
		});
	}

	protected abstract void onInit(boolean success);
	protected abstract void onConnect(int result, String msg);
	protected abstract void onDisconnect();
	protected abstract void onNewMsg(String json);

}
