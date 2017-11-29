package cn.cmgame.miguim.push.notification;

import android.content.Context;
import android.graphics.Bitmap;

import cn.cmgame.miguim.push.PushBean;

public abstract class AbstractNotifyInvoker implements INotifyInvoker
{
	protected Context context;
	protected PushBean message;
	protected Bitmap bitmap;
	
	public AbstractNotifyInvoker(Context context, PushBean message, Bitmap bitmap)
	{
		this.context = context;
		this.message = message;
		this.bitmap = bitmap;
	}
	
}
