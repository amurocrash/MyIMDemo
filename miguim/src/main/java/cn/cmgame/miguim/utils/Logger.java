package cn.cmgame.miguim.utils;

import android.util.Log;

/**
 * Created by Amuro on 2017/10/10.
 */

public class Logger
{
	private static final String DEFAULT_TAG = "migu";
	private String mTag;

	private Logger(String tag)
	{
		this.mTag = tag;
	}

	public static Logger getLogger()
	{
		return new Logger(DEFAULT_TAG);
	}

	public static Logger getLogger(String tag)
	{
		return new Logger(tag);
	}

	public void v(String msg)
	{
		v(mTag, msg);
	}

	public void v(String tag, String msg)
	{
		Log.v(tag, msg);
	}

	public void e(String msg)
	{
		e(mTag, msg);
	}

	public void e(String tag, String msg)
	{
		Log.e(tag, msg);
	}
}
