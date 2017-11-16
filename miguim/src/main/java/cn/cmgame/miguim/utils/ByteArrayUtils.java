package cn.cmgame.miguim.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Amuro on 2017/7/13.
 */

public class ByteArrayUtils
{
    public static byte[] intToBytesReverse(int value)
    {
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[2] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    public static byte[] intToBytes(int value)
    {
        byte[] byte_src = new byte[4];
        byte_src[0] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[1] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[2] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[3] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    public static int bytesToIntReverse(byte[] ary, int offset)
    {
        int value;
        value = ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    public static int bytesToInt(byte[] ary, int offset)
    {
        int value;
        value = ((ary[offset + 3] & 0xFF)
                | ((ary[offset + 2] << 8) & 0xFF00)
                | ((ary[offset + 1] << 16) & 0xFF0000)
                | ((ary[offset + 0] << 24) & 0xFF000000));
        return value;
    }

    public static byte[] shortToBytesReverse(short value)
    {
        byte[] byte_src = new byte[2];
        byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    public static byte[] shortToBytes(short value)
    {
        byte[] byte_src = new byte[2];
        byte_src[0] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[1] = (byte) ((value & 0x000000FF));
        return byte_src;
    }


    public static short bytesToShortReverse(byte[] ary, int offset)
    {
        short value;
        value = (short)((ary[offset] & 0xFF) | ((ary[offset + 1] << 8) & 0xFF00));
        return value;
    }

    public static short bytesToShort(byte[] ary, int offset)
    {
        short value;
        value = (short)((ary[offset + 1] & 0xFF) | ((ary[offset] << 8) & 0xFF00));
        return value;
    }

    public static byte[] bytesFromBytes(byte[] origin, int offset, int length)
    {
        byte[] result = new byte[length];
        System.arraycopy(origin, offset, result, 0, length);
        return result;
    }

    public static String bytesToStringUTF8(byte[] originalData, int offset, int length)
    {
        String content = null;
        try
        {
            content = new String(
                    ByteArrayUtils.bytesFromBytes(originalData, offset, length), "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return content;
    }
}
