package cn.cmgame.migu_im_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import cn.cmgame.miguimsdk.MiguIM;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MiguIM.initialize(this, MsgIntentService.class);

		findViewById(R.id.bt_connect).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MiguIM.ConnectArgs args = new MiguIM.ConnectArgs();
				args.appId = "1234567890";
				args.appKey = "1234567890";
				args.exts = new HashMap<>();
				args.exts.put("contentId", "12345678901");
				args.exts.put("channelId", "3456789012345");

				MiguIM.connect(args);
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

	}

}
