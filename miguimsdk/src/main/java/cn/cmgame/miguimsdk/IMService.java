package cn.cmgame.miguimsdk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by Amuro on 2017/10/20.
 */

public class IMService extends Service
{
	private static final String TAG = "im_service";
	private static final String SP_TAG = "miguim";

	public static final String ACTION_INIT = "action_init";
	public static final String ACTION_CONNECT = "action_connect";
	public static final String ACTION_DISCONNECT = "action_disconnect";
	public static final String ACTION_AWAKEN = "action_awaken";

	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private SocketManagerProxy mSocketManager;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.v(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.v(TAG, "onStartCommand");
		if(intent != null)
		{
			String action = intent.getAction();

			if(ACTION_INIT.equals(action))
			{
				if(mSocketManager == null)
				{
					mSocketManager = new SocketManagerProxy(this);
				}

				mSocketManager.setMock(true);
				Class<? extends IMIntentService> callbackClass =
						(Class<? extends IMIntentService>)intent.getSerializableExtra("callback");

				saveCallbackClassName(callbackClass.getName());
				mSocketManager.init(callbackClass);

			}
			else if(ACTION_CONNECT.equals(action))
			{
				if(mSocketManager != null)
				{
					IMManager.ConnectArgs args =
							(IMManager.ConnectArgs) intent.getSerializableExtra("connectArgs");

					saveArgs(args);
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
			else if(ACTION_AWAKEN.equals(action))
			{
				awakenSocket();
			}
		}

		return START_STICKY;
	}

	private void awakenSocket()
	{
		try
		{
			if (mSocketManager == null)
			{
				mSocketManager = new SocketManagerProxy(this);
			}


			mSocketManager.init(
					(Class<? extends IMIntentService>) Class.forName(getCallbackClassName()));
			IMManager.ConnectArgs args = getArgsFromSp();
			mSocketManager.connect(args);
		}
		catch (Exception e)
		{

		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	private void saveArgs(IMManager.ConnectArgs args)
	{
		SharedPreferences sp = this.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("appId", args.appId);
		editor.putString("appKey", args.appKey);

		if(args.exts != null && args.exts.size() > 0)
		{
			Set<String> set = new LinkedHashSet<>();
			for (Map.Entry<String, String> entry : args.exts.entrySet())
			{
				String str = entry.getKey() + ":" + entry.getValue();
				set.add(str);
			}
			editor.putStringSet("ext", set);
		}

		editor.commit();
	}

	private IMManager.ConnectArgs getArgsFromSp()
	{
		IMManager.ConnectArgs connectArgs = new IMManager.ConnectArgs();
		SharedPreferences sp = this.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		String appId = sp.getString("appId", "");
		String appKey = sp.getString("appKey", "");
		connectArgs.appId = appId;
		connectArgs.appKey = appKey;

		Set<String> extSet = sp.getStringSet("ext", null);

		if(extSet != null && extSet.size() > 0)
		{
			connectArgs.exts = new HashMap<>();

			for(Iterator<String> itor = extSet.iterator(); itor.hasNext();)
			{
				String str = itor.next();
				String key = str.split(":")[0];
				String value = str.split(":")[1];

				connectArgs.exts.put(key, value);
			}
		}

		return connectArgs;
	}

	private void saveCallbackClassName(String className)
	{
		SharedPreferences sp =
				this.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("callback", className);
		editor.commit();
	}

	private String getCallbackClassName()
	{
		SharedPreferences sp =
				this.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		String callback = sp.getString("callback", "");

		return callback;
	}
}
