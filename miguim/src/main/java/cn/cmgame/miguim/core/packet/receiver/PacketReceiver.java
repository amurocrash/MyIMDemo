package cn.cmgame.miguim.core.packet.receiver;

import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.utils.ByteArrayUtils;
import cn.cmgame.miguim.utils.gson.Gson;
import cn.cmgame.miguim.utils.gson.GsonBuilder;

public class PacketReceiver
{
	public static ReceiveMsg parsePacket(byte[] originalData) throws Exception
	{
		byte protocolVer = originalData[5];
		if (protocolVer != AbsSocketPacket.PROTOCOL_VER)
		{
			throw new Exception("can not parse the data protocol not matches!");
		}

		ReceiveMsg receiveMsg = new ReceiveMsg();

		byte msgType = originalData[6];

		int serverSerial = ByteArrayUtils.bytesToInt(
				ByteArrayUtils.bytesFromBytes(originalData, 7, 4), 0
		);

		byte replyMark = originalData[11];
		byte compressMark = originalData[12];//TODO 压缩的处理

		receiveMsg.setProtocolVer(protocolVer);
		receiveMsg.setMsgType(msgType);
		receiveMsg.setServerSerial(serverSerial);
		receiveMsg.setReplyMark(replyMark);
		receiveMsg.setCompressMark(compressMark);

		int dataLength = ByteArrayUtils.bytesToInt(
				ByteArrayUtils.bytesFromBytes(originalData, 0, 4), 0);
		byte dataHeadLength = originalData[4];
		Gson gson = new GsonBuilder().create();
		byte[] msgBodyBytes = ByteArrayUtils.bytesFromBytes(
				originalData, 4 + dataHeadLength, dataLength - dataHeadLength);

		String json = new String(msgBodyBytes, "utf-8");
		receiveMsg.setBaseJson(json);

		AbsSocketPacket.BaseBean baseBean =
				gson.fromJson(json, AbsSocketPacket.BaseBean.class);
		receiveMsg.setBaseBean(baseBean);

		return receiveMsg;
	}
}