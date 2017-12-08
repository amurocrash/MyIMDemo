package cn.cmgame.miguim.core.packet.disposer;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.UnifyRespPacket;
import cn.cmgame.miguim.core.packet.receiver.ReceiveMsg;

/**
 * Created by Amuro on 2017/12/7.
 */
public class UnifyRespDisposer extends AbsPacketDisposer<UnifyRespPacket>
{
	public UnifyRespDisposer(UnifyRespPacket packet, SocketCore core)
	{
		super(packet, core);
	}

	@Override
	public void disposeResp(ReceiveMsg receiveMsg)
	{
		//no resp for resp 囧
	}

	@Override
	public void onTimeOut()
	{
		//TODO need resend
	}
}
