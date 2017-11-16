package cn.cmgame.miguim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.cmgame.miguim.utils.ToastUtils;

/**
 * Created by Amuro on 2017/11/16.
 */

public class IMReceiver extends BroadcastReceiver
{
	/**
	 * <action android:name="cn.cmgame.miguim.invoke"/>
	 <action android:name="android.intent.action.BOOT_COMPLETED"/>
	 <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
	 <action android:name="android.intent.action.USER_PRESENT"/>
	 <action android:name="android.intent.action.MEDIA_MOUNTED"/>
	 <action android:name="android.intent.action.ACTION_POWER_CONNECTED"/>
	 <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED"/>
	 * @param context
	 * @param intent
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent == null)
		{
			return;
		}

		String action = intent.getAction();
		if("android.intent.action.ACTION_POWER_CONNECTED".equals(action))
		{
			ToastUtils.show(context, action);
		}
		else if("cn.cmgame.miguim.invoke".equals(action))
		{
			ToastUtils.show(context, action);
		}
	}
}
































