package cn.cmgame.miguim.core.packet;

import cn.cmgame.miguim.utils.gson.JsonObject;

/**
 * Created by Amuro on 2017/12/7.
 */

/**
 * {
 		operate:"response", // 消息、操作回执标记
 		params :{
 			sequence:0 // 被响应消息的序号
 			status:0, //枚举方式见统一回执码附表
 			message:""
 		}
 	}
 */
public class UnifyRespPacket extends SystemPacket
{
	public UnifyRespPacket(int serial, JsonObject params)
	{
		super(serial, params);
	}

	@Override
	protected String getOperate()
	{
		return "response";
	}
}
