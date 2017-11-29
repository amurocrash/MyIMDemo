package cn.cmgame.miguim.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Amuro on 2017/11/22.
 */

public class ResourcesUtils
{
	public static int getLayout(Context context, String name)
	{
		Resources resources = context.getResources();

		return resources == null ? 0 : 
				resources.getIdentifier(name, "layout", context.getPackageName());
	}

	public static int getDrawableId(Context context, String name)
	{
		Resources resources = context.getResources();

		return resources == null ? 0 :
				resources.getIdentifier(name, "drawable", context.getPackageName());
	}

	public static int getId(Context context, String name)
	{
		Resources resources = context.getResources();
		return resources == null ? 0 :
				resources.getIdentifier(name, "id", context.getPackageName());
	}
}
