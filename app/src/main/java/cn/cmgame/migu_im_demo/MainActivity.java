package cn.cmgame.migu_im_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import cn.cmgame.miguim.MiguIM;
import cn.cmgame.miguim.utils.ToastUtils;

public class MainActivity extends AppCompatActivity
{
	private class ConnectionListener implements MiguIM.IConnectionListener
	{

		@Override
		public void onSuccess()
		{
			ToastUtils.show("connect success");
		}

		@Override
		public void onFailed(int errorCode, String errorMsg)
		{
			ToastUtils.show("connect failed: " + errorCode + " -> " + errorMsg);
		}

		@Override
		public void onDisconnected()
		{
			ToastUtils.show("disconnect");
		}
	}

	private class MsgListener implements MiguIM.IMsgListener
	{

		@Override
		public void onNewMsg(String msg)
		{
			ToastUtils.show("received a msg:\n" + msg);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MiguIM.initialize(this);

		findViewById(R.id.bt_connect).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MiguIM.ConnectArgs args = new MiguIM.ConnectArgs();
				args.appId = "1234567890";
				args.exts = new HashMap<>();
				args.exts.put("contentId", "12345678901");
				args.exts.put("channelId", "3456789012345");

				MiguIM.connect(args, new ConnectionListener(), new MsgListener());
			}
		});

		findViewById(R.id.bt_disconnect).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MiguIM.disconnect();
			}
		});

		findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setAction("cn.cmgame.miguim.invoke");
				sendBroadcast(intent);
			}
		});
	}

}
