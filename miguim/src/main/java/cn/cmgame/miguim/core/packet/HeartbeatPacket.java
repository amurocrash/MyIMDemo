package cn.cmgame.miguim.core.packet;

import cn.cmgame.miguim.core.packet.SystemPacket;

/**
 * Created by Amuro on 2017/11/14.
 */

public class HeartbeatPacket extends SystemPacket
{
	public HeartbeatPacket(int serial)
	{
		super(serial);
	}

	@Override
	protected String getOperate()
	{
		return "heartbeat";
	}
}
