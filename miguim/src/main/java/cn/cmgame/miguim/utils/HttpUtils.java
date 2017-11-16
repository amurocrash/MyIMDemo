package cn.cmgame.miguim.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.cmgame.miguim.utils.gson.Gson;
import cn.cmgame.miguim.utils.gson.GsonBuilder;

/**
 * Created by Amuro on 2017/10/20.
 */

public class HttpUtils
{
	private static final String TAG = "http";
	private static final int HTTP_CONNECT_TIMEOUT = 10 * 1000;
	private static final int HTTP_READ_TIMEOUT = 10 * 1000;
	public static final String GET = "GET";
	public static final String POST = "POST";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static Logger logger = Logger.getLogger(TAG);

	public interface IHttpListener<T>
	{
		void onSuccess(T result);
		void onFailed();
	}

	public static<T> void request(
			final String url, final Map<String, String> params,
			final Class<T> classOfT, final IHttpListener<T> listener)
	{
		request(url, POST, DEFAULT_ENCODING, null, params, classOfT, listener, false, null);
	}

	public static<T> void request(
			final String url, final Map<String, String> params,
			final Class<T> classOfT, final IHttpListener<T> listener,
			boolean isMock, String mockJson)
	{
		request(url, POST, DEFAULT_ENCODING, null, params, classOfT, listener, isMock, mockJson);
	}

	public static<T> void request(
			final String url, final String method, final String encoding,
			final Map<String, String> headers, final Map<String, String> params,
			final Class<T> classOfT, final IHttpListener<T> listener,
			final boolean isMock, final String mockJson)
	{
		final ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.execute(new Runnable()
		{
			@Override
			public void run()
			{
				if(isMock)
				{
					try
					{
						Thread.sleep(new Random().nextInt(3));
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}

					Gson gson = new GsonBuilder().create();
					T result = gson.fromJson(mockJson, classOfT);;
					deliverToMainThread(result, listener);
				}
				else
				{
					final T result = syncRequest(
							url, method, encoding, headers, params, classOfT);
					deliverToMainThread(result, listener);
				}

				thread.shutdown();
			}
		});
	}

	private static<T> void deliverToMainThread(
			final T result, final IHttpListener<T> listener)
	{
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				if(result == null)
				{
					listener.onFailed();
				}
				else
				{
					listener.onSuccess(result);
				}

			}
		});
	}

	public static<T> T syncRequest(
			String url, String method, String encoding,
			Map<String, String> headers, Map<String, String> params, Class<T> classOfT)
	{
		HttpURLConnection urlConnection = null;
		T result = null;

		try
		{
			urlConnection = createUrlConnection(url);
			urlConnection.setRequestMethod(method);
			setRequestHeaders(urlConnection, headers);
			setRequestParams(urlConnection, encoding, params);

			int responseCode = urlConnection.getResponseCode();
			if(responseCode != 200)
			{
				throw new Exception("response code not 200. It it " + responseCode);
			}
			else
			{
				InputStream inputStream = urlConnection.getInputStream();
				if (inputStream == null)
				{
					throw new Exception("input stream is null");
				}
				else
				{

					// 内存流
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					byte[] data = new byte[1024];
					int len = 0;
					String resultStr = null;

					while ((len = inputStream.read(data)) != -1)
					{
						byteArrayOutputStream.write(data, 0, len);
					}
					resultStr = new String(byteArrayOutputStream.toByteArray(), encoding);

					Gson gson = new GsonBuilder().create();
					result = gson.fromJson(resultStr, classOfT);
				}
			}
		}
		catch (Exception e)
		{
			logger.e("http exception: " + e.getMessage());
		}
		finally
		{
			if (urlConnection != null)
			{
				urlConnection.disconnect();
			}
		}

		return result;

	}

	private static HttpURLConnection createUrlConnection(String url) throws Exception
	{
		URL newURL = new URL(url);
		URLConnection urlConnection = newURL.openConnection();
		urlConnection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
		urlConnection.setReadTimeout(HTTP_READ_TIMEOUT);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		return (HttpURLConnection) urlConnection;
	}

	private static void setRequestHeaders(
			HttpURLConnection connection, Map<String, String> headers)
	{
		if(headers == null)
		{
			return;
		}

		Set<String> headersKeys = headers.keySet();
		for (String headerName : headersKeys)
		{
			connection.addRequestProperty(
					headerName, headers.get(headerName));
		}
	}

	private static void setRequestParams(
			HttpURLConnection connection, String encoding, Map<String, String> params) throws Exception
	{

		byte[] body = null;
		if (params != null && params.size() > 0)
		{
			body = encodeParameters(params, encoding);
		}
		if (body != null)
		{
			connection.setDoOutput(true);
			connection.addRequestProperty(
					"Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);
			DataOutputStream dataOutputStream =
					new DataOutputStream(connection.getOutputStream());
			dataOutputStream.write(body);
			dataOutputStream.close();
		}
	}

	private static byte[] encodeParameters(
			Map<String, String> params, String paramsEncoding) throws Exception
	{
		StringBuilder encodedParams = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			encodedParams.append(
					URLEncoder.encode(entry.getKey(), paramsEncoding));
			encodedParams.append('=');
			encodedParams.append(
					URLEncoder.encode(entry.getValue(), paramsEncoding));
			encodedParams.append('&');
		}
		return encodedParams.toString().getBytes(paramsEncoding);
	}

}
