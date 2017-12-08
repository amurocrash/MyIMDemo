package cn.cmgame.miguimsdk.push.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import cn.cmgame.miguim.utils.HttpUtils;
import cn.cmgame.miguim.utils.ResourcesUtils;
import cn.cmgame.miguimsdk.push.PushBean;

public class PushNotifyManager
{
	private Context context;
	
	public PushNotifyManager(Context context)
	{
		this.context = context;
	}

	public void doNotify(PushBean msg)
	{
		//有的rom会拿不到资源文件
		if(ResourcesUtils.getLayout(context, "notification_message_icon") == 0)
		{
			doDefaultNotify(msg);

		}
		else
		{
			String imgPath = msg.getImgPath();
			
			//imgPath为空则展示icon类的Notification
			if(TextUtils.isEmpty(imgPath))
			{
				doIconNotify(msg);

			}
			else
			{
				doImageNotify(msg);
			}
		}
	}
	
	private void doDefaultNotify(PushBean msg)
	{
		new DefaultNotifyInvoker(context, msg).doNotify();
	}

	private void doIconNotify(final PushBean msg)
	{
		HttpUtils.getImage(msg.getIcon(), new HttpUtils.IBitmapListener()
		{
			@Override
			public void onSuccess(Bitmap bitmap)
			{
				new IconNotifyInvoker(context, msg, bitmap).doNotify();
			}

			@Override
			public void onFailed()
			{
				new IconNotifyInvoker(
						context,
						msg,
						BitmapFactory.decodeResource(
 							context.getResources(),
							ResourcesUtils.getDrawableId(context, "icon_notification"))
						).doNotify();
			}
		});

//		HttpUtils.getImage(msg.getIcon(), new IHttpRsp()
//		{
//
//            @Override
//            public void onSuccess(Object response)
//            {
//            	new IconNotifyInvoker(context, msg, (Bitmap)response).doNotify();
//            }
//
//            @Override
//            public void onFailure(String status, String message)
//            {
//				new IconNotifyInvoker(context, msg, BitmapFactory.decodeResource(
// context.getResources(), ResourcesUtil.getDrawableId("icon_notification"))).doNotify();
//            }
//        });
	}
	
	private void doImageNotify(final PushBean msg)
	{
		HttpUtils.getImage(msg.getImgPath(), new HttpUtils.IBitmapListener()
		{
			@Override
			public void onSuccess(Bitmap bitmap)
			{
				new ImageNotifyInvoker(context, msg, bitmap).doNotify();
			}

			@Override
			public void onFailed()
			{
				doIconNotify(msg);
			}
		});

//		ImageUtil.getBitmap(msg.getImgPath(), new IHttpRsp()
//		{
//
//			@Override
//			public void onSuccess(Object response)
//			{
//				new ImageNotifyInvoker(context, msg, (Bitmap)response).doNotify();
//			}
//
//			@Override
//			public void onFailure(String status, String message)
//			{
//				doIconNotify(msg);
//			}
//
//		});
	}
	
}









































