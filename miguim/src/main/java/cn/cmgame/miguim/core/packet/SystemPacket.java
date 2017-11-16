package cn.cmgame.miguim.core.packet;

import java.util.Map;

import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * Created by Amuro on 2017/11/7.
 */

public abstract class SystemPacket extends AbsSocketPacket
{

	public SystemPacket(int serial)
	{
		super(serial);
	}

	public SystemPacket(int serial, JsonObject params)
	{
		super(serial, params);
	}

	public SystemPacket(int serial, JsonObject params, JsonObject exts)
	{
		super(serial, params, exts);
	}

	@Override
	protected byte getMsgType()
	{
		return TYPE_SYSTEM;
	}
}
