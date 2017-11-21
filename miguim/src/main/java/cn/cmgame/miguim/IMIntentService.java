package cn.cmgame.miguim;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import cn.cmgame.miguim.core.SocketManager;

/**
 * Created by Amuro on 2017/11/17.
 */

public abstract class IMIntentService extends IntentService
{
	public static final String ACTION_INVOKE = "action_invoke";
	private Handler handler;

	public IMIntentService()
	{
		super("IMIntentService");
		handler = new Handler(Looper.getMainLooper());
	}

	public static void onNewAction(
			Context mContext, Class<? extends IMIntentService> callbackClass,
			String action, String result, Bundle data)
	{
		Intent intent = new Intent(mContext, callbackClass);
		intent.setAction(action);
		intent.putExtra("result", result);
		if(data != null)
		{
			intent.putExtra("data", data);
		}
		mContext.startService(intent);
	}

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
				if (SocketManager.RESULT_INIT_SUCCESS.equals(result))
				{
					onInit(true);
				}
				else if (SocketManager.RESULT_CONNECT_SUCCESS.equals(result))
				{
					onConnect(MiguIM.ErrorCode.CONNECT_SUCCESS, "success");
				}
				else if (SocketManager.RESULT_CONNECT_FAILED.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					int errorCode = data.getInt("errorCode");
					String errorMsg = data.getString("errorMsg");
					onConnect(errorCode, errorMsg);
				}
				else if (SocketManager.RESULT_DISCONNECTED.equals(result))
				{
					onDisconnect();
				}
				else if (SocketManager.RESULT_NEW_MSG.equals(result))
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
