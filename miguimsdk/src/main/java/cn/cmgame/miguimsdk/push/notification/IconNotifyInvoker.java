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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.cmgame.miguim.utils.ResourcesUtils;
import cn.cmgame.miguimsdk.push.PushBean;

public class IconNotifyInvoker extends AbstractNotifyInvoker
{

	public IconNotifyInvoker(Context context, PushBean message, Bitmap bitmap)
	{
		super(context, message, bitmap);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doNotify()
	{
		try
		{
			NotificationManager manager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			String title = message.getTitle();

			if (!TextUtils.isEmpty(title))
			{
				// 自定义界面
				DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				RemoteViews bigView = new RemoteViews(
						context.getPackageName(), ResourcesUtils.getLayout(context, "notification_message_icon"));
				bigView.setImageViewBitmap(
						ResourcesUtils.getId(context, "custom_icon"), bitmap);
				bigView.setTextViewText(
						ResourcesUtils.getId(context, "tv_custom_title"), title);
				bigView.setTextViewText(
						ResourcesUtils.getId(context, "tv_custom_content"), message.getContent());
				bigView.setTextViewText(ResourcesUtils.getId(context, "tv_custom_time"),
						formatter.format(calendar.getTime()));

				Notification notify = new Notification(
						ResourcesUtils.getDrawableId(context, "icon_notification"),
						title, System.currentTimeMillis());
				notify.contentView = bigView;
				notify.flags = Notification.FLAG_AUTO_CANCEL;
				String link = message.getLink();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
				notify.contentIntent = contentIntent;
				
				manager.notify(
						Integer.valueOf(message.getId()), notify);

				if (!bitmap.isRecycled())
				{
					bitmap.recycle();
				}
			}
		}
		catch (Exception e)
		{
			
		}
	}

}
