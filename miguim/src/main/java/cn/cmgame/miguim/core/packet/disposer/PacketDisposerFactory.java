package cn.cmgame.miguim.core.packet.disposer;

import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.core.packet.HeartbeatPacket;
import cn.cmgame.miguim.core.packet.UnifyRespPacket;
import cn.cmgame.miguim.core.packet.UploadInfoPacket;
import cn.cmgame.miguim.core.packet.VerifyPacket;

/**
 * Created by Amuro on 2017/11/7.
 */

public class PacketDisposerFactory
{
	public static AbsPacketDisposer getPacketDisposer(
			AbsSocketPacket packet, SocketCore core)
	{
		AbsPacketDisposer packetDisposer = null;

		if(VerifyPacket.class.equals(packet.getClass()))
		{
			packetDisposer = new VerifyPacketDisposer((VerifyPacket) packet, core);
		}
		else if(UploadInfoPacket.class.equals(packet.getClass()))
		{
			packetDisposer = new UploadInfoPacketDisposer((UploadInfoPacket)packet, core);
		}
		else if(HeartbeatPacket.class.equals(packet.getClass()))
		{
			packetDisposer = new HeartbeatPacketDisposer((HeartbeatPacket)packet, core);
		}
		else if(UnifyRespPacket.class.equals(packet.getClass()))
		{
			packetDisposer = new UnifyRespDisposer((UnifyRespPacket) packet, core);
		}

		return packetDisposer;
	}
}
