package cn.cmgame.miguim.core.packet.receiver;

import android.os.Bundle;

import java.io.Serializable;

import cn.cmgame.miguim.SocketManager;
import cn.cmgame.miguim.core.SocketCore;
import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.core.packet.UnifyRespPacket;
import cn.cmgame.miguim.core.packet.disposer.AbsPacketDisposer;
import cn.cmgame.miguim.utils.ByteArrayUtils;
import cn.cmgame.miguim.utils.gson.Gson;
import cn.cmgame.miguim.utils.gson.GsonBuilder;
import cn.cmgame.miguim.utils.gson.JsonObject;

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

	public static void parsePacketNew(byte[] originalData, SocketCore core) throws Exception
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

		SocketCore.logger.v("receive a msg: " + receiveMsg.toString());
		dispose(receiveMsg, core);
	}

	private static void dispose(ReceiveMsg receiveMsg, SocketCore socketCore) throws Exception
	{
		if(AbsSocketPacket.TYPE_SYSTEM == receiveMsg.getMsgType())
		{
			if("response".equals(receiveMsg.getOperate()))
			{
				socketCore.disposeUnifyResp(receiveMsg);
			}
			else
			{
				//目前服务端还没有其他的系统消息，先留着
				throw new Exception("fxxking system msg with operate: " +
						receiveMsg.getOperate() + " which can't be disposed.");
			}
		}
		else if(AbsSocketPacket.TYPE_BIZ == receiveMsg.getMsgType())
		{
			//push
			if("pushMsg".equals(receiveMsg.getOperate()))
			{
				String notifyType = receiveMsg.getParam("notifyType").getAsString();
				String body = receiveMsg.getParam("body").getAsString();
				//直接推送
				if("1".equals(notifyType))
				{
					push(body, socketCore);
				}
				//透传json
				else if("0".equals(notifyType))
				{
					passThrough(body, socketCore);
				}
				//都要
				else
				{
					push(body, socketCore);
					passThrough(body, socketCore);
				}

				socketCore.responseToServerWithUnifyResp(receiveMsg.getServerSerial());

			}
			else
			{
				throw new Exception(
						"can't dispose the biz msg which operator is " + receiveMsg.getOperate()
				);
			}


		}
		else
		{
			throw new Exception(
					"can't dispose the msg which type is " + receiveMsg.getMsgType());
		}

	}

	private static void push(String body, SocketCore socketCore) throws Exception
	{
		Class pushBeanClazz = Class.forName("cn.cmgame.miguimsdk.push.PushBean");
		Gson gson = new GsonBuilder().create();
		Serializable pushBean = (Serializable) gson.fromJson(body, pushBeanClazz);

		Bundle data = new Bundle();
		data.putSerializable("pushBean", pushBean);
		socketCore.getSocketManager().publishResult(
				SocketManager.RESULT_NEW_MSG_PUSH, data);
	}

	private static void passThrough(String body, SocketCore socketCore) throws Exception
	{
		Bundle data = new Bundle();
		data.putString("msg", body);
		socketCore.getSocketManager().publishResult(
				SocketManager.RESULT_NEW_MSG_PUSH_PASS_THROUGH, data);
	}
}