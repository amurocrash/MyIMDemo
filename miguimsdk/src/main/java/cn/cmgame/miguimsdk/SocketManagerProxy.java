package cn.cmgame.miguimsdk;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by Amuro on 2017/11/21.
 */

public class SocketManagerProxy
{

	private Context context;
	private ClassLoader classLoader;
	private boolean isPluginLoaded = false;

	private Handler handler;
	private Class realSocketManagerClass;
	private Object realSocketManager;

	private boolean isMock = false;

	public SocketManagerProxy(Context context)
	{
		this.context = context;
		this.handler = new Handler(Looper.getMainLooper());
	}

	public void setMock(boolean mock)
	{
		isMock = mock;
	}

	public void init(final Class<? extends IMIntentService> callbackClass)
	{
		if(isPluginLoaded)
		{
			return;
		}

		if(isMock)
		{
			realInit(callbackClass);
			return;
		}

		//TODO 模拟下载插件
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				handler.post(new Runnable()
				{
					@Override
					public void run()
					{
						realInit(callbackClass);
					}
				});

			}
		}).start();


	}

	private void realInit(Class<? extends IMIntentService> callbackClass)
	{
		try
		{
			if(!isMock)
			{
				String dexPath =
						Environment.getExternalStorageDirectory() + "/miguim/"
								+ context.getPackageName() + "/" + "im.jar";
				String optPath = getDir(
						context.getCacheDir().getAbsolutePath() + "/miguim/opt").getAbsolutePath();
				classLoader = new DexClassLoader(
						dexPath,
						optPath,
						null,
						context.getClassLoader()
				);


			}
			else
			{
				classLoader = context.getClassLoader();
			}

			realSocketManagerClass =
					classLoader.loadClass("cn.cmgame.miguim.SocketManager");

			Constructor constructor = realSocketManagerClass.getConstructor(Context.class);
			constructor.setAccessible(true);
			realSocketManager = constructor.newInstance(context);

			Method initMethod = realSocketManagerClass.getDeclaredMethod("init", Class.class);
			initMethod.setAccessible(true);
			initMethod.invoke(realSocketManager, callbackClass);
		}
		catch (Exception e)
		{

		}

		isPluginLoaded = true;
	}

	private Method connectMethod;
	private Method disconnectMethod;

	public void connect(MiguIM.ConnectArgs args)
	{
		try
		{
			if (connectMethod == null)
			{
				connectMethod =
						realSocketManagerClass.getDeclaredMethod(
								"connect", new Class[]{String.class, String.class, Map.class});
				connectMethod.setAccessible(true);
			}

			connectMethod.invoke(realSocketManager,
					new Object[]{args.appId, args.appKey, args.exts});
		}
		catch (Exception e)
		{

		}
	}

	public void disconnect()
	{
		try
		{
			if (disconnectMethod == null)
			{
				disconnectMethod =
						realSocketManagerClass.getDeclaredMethod("disconnect");
				disconnectMethod.setAccessible(true);
			}

			disconnectMethod.invoke(realSocketManager);
		}
		catch (Exception e)
		{

		}
	}

	private File getDir(String dir)
	{
		File fileDir = new File(dir);
		if(!fileDir.exists())
		{
			fileDir.mkdirs();
		}

		return fileDir;
	}

}
