package cn.cmgame.miguim.utils;

import java.io.OutputStream;

/**
 * Created by Amuro on 2017/12/7.
 */

public class StreamUtils
{
	public static void sendPacket(OutputStream outputStream, byte[] data, int bufferSize) throws Exception
	{
		if (outputStream == null)
		{
			return;
		}

//		byte[] data = packetForSend.generateFinalData();
//		int bufferSize = socketClient.getSendBufferSize();

		if (data.length <= bufferSize)
		{
			outputStream.write(data);
			outputStream.flush();
		}
		else
		{
			int totalLength = data.length;
			int sendLength = 0;

			while (sendLength < totalLength)
			{
				int count;
				if (totalLength - sendLength >= bufferSize)
				{
					count = bufferSize;
				}
				else
				{
					count = totalLength - sendLength;
				}

				sendLength += count;
				outputStream.write(data, sendLength, count);
				outputStream.flush();
			}
		}
	}
}
