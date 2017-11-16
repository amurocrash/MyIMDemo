package cn.cmgame.miguim.core.packet.disposer;

import cn.cmgame.miguim.MiguIM;
import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.SocketManager;
import cn.cmgame.miguim.core.packet.VerifyPacket;

/**
 * Created by Amuro on 2017/11/7.
 */

public class VerifyPacketDisposer extends AbsPacketDisposer<VerifyPacket>
{

	public VerifyPacketDisposer(VerifyPacket packet, SocketCore core)
	{
		super(packet, core);
	}

	@Override
	public void sendPacket() throws Exception
	{
		super.sendPacket();
		logger.v("send verify msg succeed");
	}

	@Override
	public void onTimeOut()
	{
		logger.v("verify timeout");
		socketCore.getSocketManager().publishResult(
				SocketManager.RESULT_CONNECT_FAILED,
				MiguIM.IConnectionListener.ErrorCode.CONNECT_TIMEOUT, "");
		socketCore.disconnect();
	}

	@Override
	protected void onRespSuccess()
	{
		logger.v("verify success");
		socketCore.getSocketManager().publishResult(
						SocketManager.RESULT_CONNECT_SUCCESS);
		socketCore.startHeartbeat();
		socketCore.uploadDeviceInfo();
	}

	@Override
	protected void onRespFailed(int status, String msg)
	{
		logger.v("verify failed: " + status + ", " + msg);
		socketCore.getSocketManager().publishResult(
					SocketManager.RESULT_CONNECT_FAILED, status, msg);
		socketCore.disconnect();
	}

}
