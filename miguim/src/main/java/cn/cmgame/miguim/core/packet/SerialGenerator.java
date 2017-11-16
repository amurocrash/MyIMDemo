package cn.cmgame.miguim.core.packet;

/**
 * Created by Amuro on 2017/7/18.
 */

public class SerialGenerator
{
    int serial = 0;

    public synchronized int get()
    {
        return serial;
    }

    public synchronized int addAndGet()
    {
        if(serial == Integer.MAX_VALUE)
        {
            serial = 1;
            return serial;
        }
        else
        {
            return ++serial;
        }
    }
}
