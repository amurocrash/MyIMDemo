package cn.cmgame.miguimsdk;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import cn.cmgame.miguimsdk.push.PushBean;
import cn.cmgame.miguimsdk.push.notification.PushNotifyManager;

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
	private static final String RESULT_NEW_MSG_PUSH = "result_new_msg_push";
	private static final String RESULT_NEW_MSG_PUSH_PASS_THROUGH = "result_new_msg_pass_through";


	private Handler handler;

	public IMIntentService()
	{
		super("IMIntentService");
		handler = new Handler(Looper.getMainLooper());
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
				else if (RESULT_NEW_MSG_PUSH.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					PushBean pushBean = (PushBean) data.get("pushBean");
					PushNotifyManager pm = new PushNotifyManager(IMIntentService.this);
					pm.doNotify(pushBean);
				}
				else if(RESULT_NEW_MSG_PUSH_PASS_THROUGH.equals(result))
				{
					Bundle data = intent.getBundleExtra("data");
					String json = (String) data.get("msg");
					onNewMsg(json);
				}
			}
		});
	}

	protected abstract void onInit(boolean success);
	protected abstract void onConnect(int result, String msg);
	protected abstract void onDisconnect();
	protected abstract void onNewMsg(String json);

}
