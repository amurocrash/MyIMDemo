package cn.cmgame.miguim.utils;

import java.io.File;

/**
 * Created by Amuro on 2017/11/21.
 */

public class FileUtils
{
	public static File getDir(String dir)
	{
		File fileDir = new File(dir);
		if(!fileDir.exists())
		{
			fileDir.mkdirs();
		}

		return fileDir;
	}
}
