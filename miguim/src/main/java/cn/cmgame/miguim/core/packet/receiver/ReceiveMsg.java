package cn.cmgame.miguim.core.packet.receiver;

import cn.cmgame.miguim.core.packet.AbsSocketPacket;
import cn.cmgame.miguim.utils.gson.JsonElement;

/**
 * Created by Amuro on 2017/7/18.
 */

public class ReceiveMsg
{
	public class UnifyRespCode
	{
		public static final int MSG_RESP_SUCCESS = 0;
	}

	private byte protocolVer;
    private byte msgType;
	private int serverSerial;
	private byte replyMark;
	private byte compressMark;
	private String baseJson;
    private AbsSocketPacket.BaseBean baseBean;

	public byte getProtocolVer()
	{
		return protocolVer;
	}

	public void setProtocolVer(byte protocolVer)
	{
		this.protocolVer = protocolVer;
	}

	public byte getMsgType()
	{
		return msgType;
	}

	public void setMsgType(byte msgType)
	{
		this.msgType = msgType;
	}

	public int getServerSerial()
	{
		return serverSerial;
	}

	public void setServerSerial(int serverSerial)
	{
		this.serverSerial = serverSerial;
	}

	public byte getReplyMark()
	{
		return replyMark;
	}

	public void setReplyMark(byte replyMark)
	{
		this.replyMark = replyMark;
	}

	public byte getCompressMark()
	{
		return compressMark;
	}

	public void setCompressMark(byte compressMark)
	{
		this.compressMark = compressMark;
	}

	public String getBaseJson()
	{
		return baseJson;
	}

	public void setBaseJson(String baseJson)
	{
		this.baseJson = baseJson;
	}

	public void setBaseBean(AbsSocketPacket.BaseBean baseBean)
	{
		this.baseBean = baseBean;
	}

	public String getOperate()
	{
		return baseBean.operate;
	}

	public JsonElement getParam(String key)
	{
		return baseBean.params.get(key);
	}

	public JsonElement getExt(String key)
	{
		return baseBean.exts.get(key);
	}
}
