package cn.cmgame.miguim.core.packet;

/**
 * Created by Amuro on 2017/10/27.
 */

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.cmgame.miguim.utils.ByteArrayUtils;
import cn.cmgame.miguim.utils.gson.Gson;
import cn.cmgame.miguim.utils.gson.GsonBuilder;
import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * 数据长度 报文头长度+数据长度 4
 * 报文头 报文头长度 1
 * 		 协议版本号 1
 * 		 消息类型 1
 * 		 消息序号 4
 * 		 回执标记 1
 * 		 压缩标记 1
 * 	数据
 */
public abstract class AbsSocketPacket
{
	public static final byte PROTOCOL_VER = 1;

	public static final byte TYPE_SYSTEM = 0;
	public static final byte TYPE_BIZ = 1;

	public class BaseBean
	{
		public String operate;
		public JsonObject params;
		public JsonObject exts;
	}

	protected byte dataLengthBytes[];

	protected byte headLength;
	protected byte protocolVer;
	protected byte msgType;
	protected byte serialBytes[];
	protected byte replyMark;
	protected byte compressMark;
	protected byte dataHeadBytes[];

	protected byte msgBodyBytes[];

	protected int serial;
	protected String msgBody;
	protected int msgLength;
	protected int dataLength;

	protected Gson gson;

	public AbsSocketPacket(int serial)
	{
		this(serial, null, null);
	}

	public AbsSocketPacket(int serial, JsonObject params)
	{
		this(serial, params, null);
	}

	public AbsSocketPacket(int serial, JsonObject params, JsonObject exts)
	{
		this.serial = serial;

		gson = new GsonBuilder().create();

		BaseBean bean = new BaseBean();
		bean.operate = getOperate();
		bean.params = params;
		bean.exts = exts;

		this.msgBody = gson.toJson(bean);

		setDataHead();
		setDataBody();
		setDataLength();
	}

	protected abstract String getOperate();

	protected void setDataHead()
	{
		this.headLength = 9;
		this.dataHeadBytes = new byte[9];
		setHeadLength();
		setProtocolVersion();
		setMsgType();
		setSerialBytes();
		setReplyMark();
		setCompressMark();

	}

	protected void setDataBody()
	{
		try
		{
			msgLength = msgBody.getBytes("utf-8").length;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		msgBodyBytes = msgBody.getBytes();
	}

	protected void setDataLength()
	{
		this.dataLength = msgLength + headLength;
		this.dataLengthBytes = ByteArrayUtils.intToBytes(dataLength);
	}

	public void setHeadLength()
	{
		this.dataHeadBytes[0] = headLength;
	}

	public void setProtocolVersion()
	{
		this.protocolVer = PROTOCOL_VER;
		this.dataHeadBytes[1] = protocolVer;
	}

	protected void setMsgType()
	{
		this.msgType = getMsgType();
		this.dataHeadBytes[2] = msgType;
	}

	/**
	 * 0 system
	 * 1 biz
	 * 2 reply
	 */
	protected abstract byte getMsgType();

	protected void setSerialBytes()
	{
		this.serialBytes = ByteArrayUtils.intToBytes(serial);
		System.arraycopy(serialBytes, 0, dataHeadBytes, 3, serialBytes.length);
	}

	protected void setReplyMark()
	{
		this.replyMark = getReplyMark();
		this.dataHeadBytes[7] = replyMark;
	}

	/**
	 * 0 no need for reply
	 * 1 need reply on receiving
	 */
	protected byte getReplyMark()
	{
		return 1;
	}

	/**
	 * 0 no need
	 * 1 google protobuf
	 * 2 gzip
	 */
	protected void setCompressMark()
	{
		this.compressMark = 0;
		this.dataHeadBytes[8] = compressMark;
	}

	public byte[] generateFinalData()
	{
		byte[] result = new byte[dataLength + 4];

		System.arraycopy(
				dataLengthBytes, 0, result, 0, dataLengthBytes.length);
		System.arraycopy(
				dataHeadBytes, 0, result, dataLengthBytes.length, dataHeadBytes.length);
		System.arraycopy(
				msgBodyBytes, 0, result,
				dataLengthBytes.length + dataHeadBytes.length, msgBodyBytes.length);

		return result;
	}

	public int getSerial()
	{
		return serial;
	}

}
































