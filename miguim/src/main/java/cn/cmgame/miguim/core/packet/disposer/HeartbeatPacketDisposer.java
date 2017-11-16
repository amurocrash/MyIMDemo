package cn.cmgame.miguim.core.packet.disposer;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.HeartbeatPacket;

/**
 * Created by Amuro on 2017/11/14.
 */

public class HeartbeatPacketDisposer extends AbsPacketDisposer<HeartbeatPacket>
{
	public HeartbeatPacketDisposer(HeartbeatPacket packet, SocketCore core)
	{
		super(packet, core);
	}

	@Override
	public void sendPacket() throws Exception
	{
		super.sendPacket();
		logger.v("send heartbeat packet succeed");
	}

	@Override
	public void onTimeOut()
	{
		logger.v("heartbeat send timeout");
		socketCore.disconnect();
	}

	@Override
	protected void onRespSuccess()
	{
		logger.v("heartbeat resp success");
	}

	@Override
	protected void onRespFailed(int status, String msg)
	{
		logger.v("heartbeat resp failed: " + status + ", " + msg);
		socketCore.disconnect();
	}
}
