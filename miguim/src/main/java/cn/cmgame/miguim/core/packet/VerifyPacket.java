package cn.cmgame.miguim.core.packet;

import java.util.Map;

import cn.cmgame.miguim.MiguIM;
import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * Created by Amuro on 2017/11/7.
 */

public class VerifyPacket extends SystemPacket
{

	public VerifyPacket(int serial, JsonObject params)
	{
		super(serial, params);
	}

	@Override
	protected String getOperate()
	{
		return "check";
	}
}
