package cn.cmgame.miguim.core.packet.disposer;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.UploadInfoPacket;

/**
 * Created by Amuro on 2017/11/13.
 */

public class UploadInfoPacketDisposer extends AbsPacketDisposer<UploadInfoPacket>
{
	public UploadInfoPacketDisposer(UploadInfoPacket packet, SocketCore core)
	{
		super(packet, core);
	}

	@Override
	public void sendPacket() throws Exception
	{
		super.sendPacket();
		logger.v("send upload msg succeed");
	}

	@Override
	public void onTimeOut()
	{

	}

	@Override
	protected void onRespSuccess()
	{
		logger.v("upload device info succeed");
	}

	@Override
	protected void onRespFailed(int status, String msg)
	{
		logger.v("upload device info failed: " + status + ", " + msg);
	}
}
