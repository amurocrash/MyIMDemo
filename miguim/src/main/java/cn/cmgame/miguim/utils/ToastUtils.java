package cn.cmgame.miguim.utils;

import android.content.Context;
import android.widget.Toast;

import cn.cmgame.miguim.core.IMCore;
import cn.cmgame.miguim.core.IMManager;

/**
 * Created by Amuro on 2017/10/25.
 */

public class ToastUtils
{
	public static void show(String msg)
	{
		Toast.makeText(IMManager.sAppContext, msg, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
