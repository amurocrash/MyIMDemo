package cn.cmgame.miguim.core.tokenmanager;

import java.util.Map;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.utils.DeviceUtils;
import cn.cmgame.miguim.utils.HttpUtils;
import cn.cmgame.miguim.utils.HttpUtils.IHttpListener;
import cn.cmgame.miguim.utils.Logger;

/**
 * Created by Amuro on 2017/10/25.
 */

public class ConfigManager
{
	public interface ITokenListener
	{
		void onSuccess();
		void onFailed();
	}

	private String mTokenUrl;
	private boolean mIsMock = false;
	private TokenBean mTokenBean;
	private long timeOfTokenFetched;
	private Logger logger;

	public ConfigManager(String tokenUrl)
	{
		this.mTokenUrl = tokenUrl;
		this.logger = SocketCore.logger;
	}

	public void setMock(boolean isMock)
	{
		this.mIsMock = isMock;
	}

	public void requestToken(Map<String, String> params, final ITokenListener tokenListener)
	{
		HttpUtils.request(
				mTokenUrl,
				params,
				TokenBean.class,
				new IHttpListener<TokenBean>()
				{
					@Override
					public void onSuccess(TokenBean bean)
					{
						logger.v("token fetch succeed: " + bean.toString());
						timeOfTokenFetched = System.currentTimeMillis();
						mTokenBean = bean;
						if(tokenListener != null)
						{
							tokenListener.onSuccess();
						}
					}

					@Override
					public void onFailed()
					{
						logger.v("token fetch failed");
						if(tokenListener != null)
						{
							tokenListener.onFailed();
						}
					}
				},
				mIsMock,
				mIsMock ? getMockJson() : null
		);
	}

	public String getServerIp()
	{
		return mTokenBean.getServer();
	}

	public int getPort()
	{
		return mTokenBean.getPort();
	}

	public int getTimeOut()
	{
		return mTokenBean.getTimeout();
	}

	public long getHeartBeatInterval()
	{
		return mTokenBean.getHeartbeat() * 1000;
	}

	public long getSendMsgTimeout()
	{
		return 60 * 1000;
	}

	public String getToken()
	{
		return mTokenBean.getToken();
	}

	public boolean isTokenValid()
	{
		long interval = System.currentTimeMillis() - timeOfTokenFetched;
		if(interval > mTokenBean.getExpire() * 1000)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private String getMockJson()
	{
		return "{\n" +
				"status:0, \n" +
				"message:\"\", \n" +
				"token:\"1234567890\", \n" +
				"server:\"192.168.1.5\", \n" +
				"port:5222, \n" +
				"expire:7200, \n" +
				"heartbeat:60, \n" +
				"timeout:200 \n" +
				"}";
	}
}































