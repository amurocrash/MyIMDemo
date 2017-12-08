package cn.cmgame.miguimsdk.push.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;

import cn.cmgame.miguim.utils.ResourcesUtils;
import cn.cmgame.miguimsdk.push.PushBean;

public class ImageNotifyInvoker extends AbstractNotifyInvoker
{

	public ImageNotifyInvoker(Context context, PushBean message,
			Bitmap bitmap)
	{
		super(context, message, bitmap);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doNotify()
	{
		Notification notify = null;
        String title = message.getTitle();
        
        if (!TextUtils.isEmpty(title)) 
        {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //自定义界面   
            RemoteViews bigView= new RemoteViews(
            		context.getPackageName(), ResourcesUtils.getLayout(context, "notification_message_pic"));
            bigView.setImageViewBitmap(
					ResourcesUtils.getId(context, "notification_img"), bitmap);
            bigView.setTextViewText(
					ResourcesUtils.getId(context, "notification_content"), message.getContent());
            
            String link = message.getLink();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            PendingIntent contentIntent = PendingIntent.getActivity(
            			context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            notify = new Notification(
					ResourcesUtils.getDrawableId(context, "icon_notification"), title, System.currentTimeMillis());
            notify.contentView = bigView;  
            notify.contentIntent=contentIntent;
            notify.flags = Notification.FLAG_AUTO_CANCEL;
            manager.notify(Integer.valueOf(message.getId()), notify);
            
           if(!bitmap.isRecycled())
           {
               bitmap.recycle();
           }
        }
	}

}
