package cn.cmgame.migu_im_demo;

import cn.cmgame.migu_im_demo.utils.ToastUtils;
import cn.cmgame.miguimsdk.IMIntentService;
import cn.cmgame.miguimsdk.MiguIM;

/**
 * Created by Amuro on 2017/11/20.
 */

public class MsgIntentService extends IMIntentService
{

	@Override
	protected void onInit(boolean success)
	{
		ToastUtils.show(this, "初始化成功");
	}

	@Override
	protected void onConnect(int result, String msg)
	{
		if(result == MiguIM.ErrorCode.CONNECT_SUCCESS)
		{
			ToastUtils.show(this, "连接成功");
		}
		else
		{
			ToastUtils.show(this, "连接失败：" + result + " -> " + msg);
		}
	}

	@Override
	protected void onDisconnect()
	{
		ToastUtils.show(this, "断开连接");
	}

	@Override
	protected void onNewMsg(String json)
	{
		ToastUtils.show(this, "收到消息: " + json);
	}
}
