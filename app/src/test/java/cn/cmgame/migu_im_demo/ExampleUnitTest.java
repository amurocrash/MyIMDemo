package cn.cmgame.migu_im_demo;

import android.util.Log;

import org.junit.Test;

import cn.cmgame.miguim.utils.HttpUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest
{
	@Test
	public void addition_isCorrect() throws Exception
	{
		String url = "http://www.weather.com.cn/data/sk/101010100.html";

		HttpUtils.request(
				url,
				null,
				Object.class,
				new HttpUtils.IHttpListener<Object>()
				{
					@Override
					public void onSuccess(Object result)
					{
						System.out.println(result);
						Log.v("test", result + "");
					}

					@Override
					public void onFailed()
					{
						System.out.println("failed");
					}
				}
		);

	}
}