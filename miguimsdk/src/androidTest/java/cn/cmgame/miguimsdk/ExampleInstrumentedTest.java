package cn.cmgame.miguimsdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
	private static final String SP_TAG = "miguim";
	static Context context;

	@Test
	public void useAppContext() throws Exception
	{
		context = InstrumentationRegistry.getTargetContext();
		MiguIM.ConnectArgs args = new MiguIM.ConnectArgs();
		args.appId = "1234567890";
		args.appKey = "1234567890123456";
		args.exts = new HashMap<>();
		args.exts.put("contentId", "1234567890c");
		args.exts.put("channelId", "1234567890ch");

		saveArgs(args);
		getArgsFromSp();

	}

	private void saveArgs(MiguIM.ConnectArgs args)
	{
		SharedPreferences sp = context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("appId", args.appId);
		editor.putString("appKey", args.appKey);

		if(args.exts != null && args.exts.size() > 0)
		{
			Set<String> set = new LinkedHashSet<>();
			for (Map.Entry<String, String> entry : args.exts.entrySet())
			{
				String str = entry.getKey() + ":" + entry.getValue();
				set.add(str);
			}
			editor.putStringSet("ext", set);
		}

		editor.commit();
	}

	private MiguIM.ConnectArgs getArgsFromSp()
	{
		MiguIM.ConnectArgs connectArgs = new MiguIM.ConnectArgs();
		SharedPreferences sp = context.getSharedPreferences(SP_TAG, Context.MODE_PRIVATE);
		String appId = sp.getString("appId", "");
		String appKey = sp.getString("appKey", "");
		connectArgs.appId = appId;
		connectArgs.appKey = appKey;

		Set<String> extSet = sp.getStringSet("ext", null);

		if(extSet != null && extSet.size() > 0)
		{
			connectArgs.exts = new HashMap<>();

			for(Iterator<String> itor = extSet.iterator(); itor.hasNext();)
			{
				String str = itor.next();
				String key = str.split(":")[0];
				String value = str.split(":")[1];

				connectArgs.exts.put(key, value);
			}
		}

		return connectArgs;
	}
}
