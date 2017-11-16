package cn.cmgame.miguim;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import cn.cmgame.miguim.utils.gson.Gson;
import cn.cmgame.miguim.utils.gson.GsonBuilder;
import cn.cmgame.miguim.utils.gson.JsonDeserializationContext;
import cn.cmgame.miguim.utils.gson.JsonDeserializer;
import cn.cmgame.miguim.utils.gson.JsonElement;
import cn.cmgame.miguim.utils.gson.JsonObject;
import cn.cmgame.miguim.utils.gson.JsonParseException;
import cn.cmgame.miguim.utils.gson.reflect.TypeToken;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
	private class Fxxk
	{
		JsonObject params;
	}

	@Test
	public void gsonTest() throws Exception
	{
		String json = "{\n" +
				"params :{\n" +
				"sequence:0, " +
				"status:0, " +
				"message:\"\"\n" +
				"} }";


		Gson gson = new GsonBuilder().create();
		Fxxk fxxk = gson.fromJson(json, Fxxk.class);
		System.out.println(fxxk.params.get("sequence").getAsInt());

//		Gson gson = new GsonBuilder().create();
//		BaseBean bean = new BaseBean();
//		bean.operate = "check";
//
//		bean.params = new HashMap<>();
//		bean.params.put("device", "1234567890");
//
//		System.out.println(gson.toJson(bean));
	}

	private class BaseBean
	{
		String operate;
		Map<String, Object> params;
		Map<String, Object> exts;

		public void putParams(Map<String, Object> params)
		{
			this.params = params;
		}

		public void putExts(Map<String, Object> exts)
		{
			this.exts = exts;
		}


	}
}