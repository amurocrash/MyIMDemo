package cn.cmgame.miguim.core;

import android.content.Context;

import cn.cmgame.miguim.MiguIM;

/**
 * Created by Amuro on 2017/10/20.
 */
//
public class IMManager
{
	public static void initialize(Context context)
	{
		IMCore.getInstance().initialize(context);
	}

	public static void connect(MiguIM.ConnectArgs args,
			MiguIM.IConnectionListener connectionListener, MiguIM.IMsgListener msgListener)
	{
		IMCore.getInstance().connect(args, connectionListener, msgListener);
	}

	public static void disconnect()
	{
		IMCore.getInstance().disconnect();
	}
}
