package cn.cmgame.miguim.core.packet;

import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * Created by Amuro on 2017/11/13.
 */

public class UploadInfoPacket extends SystemPacket
{
	public UploadInfoPacket(int serial, JsonObject params, JsonObject exts)
	{
		super(serial, params, exts);
	}

	@Override
	protected String getOperate()
	{
		return "attrs";
	}
}
