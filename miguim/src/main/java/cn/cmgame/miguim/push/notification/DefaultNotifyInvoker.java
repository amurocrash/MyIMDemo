package cn.cmgame.miguim.push.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import cn.cmgame.miguim.push.PushBean;
import cn.cmgame.miguim.utils.ResourcesUtils;

/**
 * ImagePath中
 * @author Amuro
 *
 */
public class DefaultNotifyInvoker extends AbstractNotifyInvoker
{

	public DefaultNotifyInvoker(Context context, PushBean message)
	{
		super(context, message, null);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	public void doNotify()
	{
		try
		{
			NotificationManager manager = (NotificationManager)
					context.getSystemService(Context.NOTIFICATION_SERVICE);
			String title = message.getTitle();
			
			if (!TextUtils.isEmpty(title))
			{
				Notification notify = null;
				if (Build.VERSION.SDK_INT > 15)
				{
					notify = new Notification.Builder(context)
							.setSmallIcon(ResourcesUtils.getDrawableId(context, "icon_notification"))
							.setPriority(Notification.PRIORITY_MAX)
							.setWhen(System.currentTimeMillis())
							.setContentTitle(title)
							.setContentText(message.getContent())
							.build();
				}
				else
				{
					notify = new Notification(
							ResourcesUtils.getDrawableId(context, "icon_notification"),
							title, System.currentTimeMillis());
				}

				notify.flags = Notification.FLAG_AUTO_CANCEL;
				String link = message.getLink();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
				PendingIntent contentIntent = PendingIntent.getActivity(
								context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
				notify.contentIntent = contentIntent;
				manager.notify(Integer.valueOf(message.getId()), notify);
			}
		}
		catch (Exception e)
		{
		}
	}

}
