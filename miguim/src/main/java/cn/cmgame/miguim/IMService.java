package cn.cmgame.miguim;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.cmgame.miguim.core.SocketManager;
import cn.cmgame.miguim.utils.Logger;

/**
 * Created by Amuro on 2017/10/20.
 */

public class IMService extends Service
{
	private static final String TAG = "im_service";

	public static final String ACTION_INIT = "action_init";
	public static final String ACTION_CONNECT = "action_connect";
	public static final String ACTION_DISCONNECT = "action_disconnect";

	private Logger logger;

	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private SocketManager mSocketManager;

	@Override
	public void onCreate()
	{
		super.onCreate();
		logger = Logger.getLogger(TAG);
		logger.v("onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		logger.v("onStartCommand");
		if(intent != null)
		{
			String action = intent.getAction();

			if(ACTION_INIT.equals(action))
			{
				if(mSocketManager == null)
				{
					mSocketManager = new SocketManager(this);
				}

				Class<? extends IMIntentService> callbackClass =
						(Class<? extends IMIntentService>) intent.getSerializableExtra("callback");

				mSocketManager.init(callbackClass);

			}
			else if(ACTION_CONNECT.equals(action))
			{
				if(mSocketManager != null)
				{
					MiguIM.ConnectArgs args =
							(MiguIM.ConnectArgs) intent.getSerializableExtra("connectArgs");

					mSocketManager.connect(args);
				}
			}
			else if(ACTION_DISCONNECT.equals(action))
			{
				if(mSocketManager != null)
				{
					mSocketManager.disconnect();
				}
			}
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		logger.v("onDestroy");
	}
}
