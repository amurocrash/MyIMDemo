package cn.cmgame.migu_im_demo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Amuro on 2017/11/21.
 */

public class ToastUtils
{
	public static void show(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
