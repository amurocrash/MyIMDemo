package cn.cmgame.miguim.utils;

import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;


public class DeviceUtils
{
//	public static int NETWORK_TYPE = -1;
//	public static int NETWORK_SUBTYPE = -1;
//	public static String NETWORK_EXTRA_INFO = "";

	public static String getIMEI(Context context)
	{
		String _imei = "";
		try
		{
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null)
			{
				_imei = telephonyManager.getDeviceId();
			}
		}
		catch (Exception e)
		{
		}
		return _imei;
	}

	public static String getIMSI(Context context)
	{
		String _imsi = "";
		try
		{
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null)
			{
				_imsi = telephonyManager.getSubscriberId();
			}
		}
		catch (Exception e)
		{
		}

		return _imsi;
	}

	public static String getTel(Context context)
	{
		String _tel = "";
		try
		{
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null)
			{
				_tel = telephonyManager.getLine1Number();
			}
		}
		catch (Exception e)
		{
		}

		return _tel;
	}

	public static String getICCID(Context context)
	{
		String _iccid = "";
		try
		{
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null)
			{
				_iccid = telephonyManager.getSimSerialNumber();
			}
		}
		catch (Exception e)
		{
		}

		return _iccid;
	}

//	public static String getOperator(Context context)
//	{
//		String _operator = LogBaseConstant.Operator.Other;
//		try
//		{
//			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			if (telephonyManager != null)
//			{
//
//				String _networkOperator = telephonyManager.getNetworkOperator();
//				_operator = getOperatorFromNetworkOperator(_networkOperator);
//
//				if (LogBaseConstant.Operator.Other.equals(_operator))
//				{
//					_operator = getOperatorFormNetworkExtra();
//				}
//
//				if (LogBaseConstant.Operator.Other.equals(_operator))
//				{
//					String _iccid = telephonyManager.getSimSerialNumber();
//					_operator = getOperatorFromIccid(_iccid);
//				}
//			}
//		}
//		catch (Exception e)
//		{
//		}
//
//		return _operator;
//	}

	public static String getMacAddress(Context context)
	{
		String _macAddress = getMacAddressFromJava(context);
		_macAddress = _macAddress.replaceAll(":", "");
		return _macAddress;
	}

//	public static String getNetworkType()
//	{
//		String _networkType = LogBaseConstant.NetworkType.Network_NONE;
//
//		if (NETWORK_TYPE == ConnectivityManager.TYPE_WIFI)
//		{
//			_networkType = LogBaseConstant.NetworkType.Network_WIFI;
//		}
//		else if (NETWORK_TYPE == ConnectivityManager.TYPE_MOBILE)
//		{
//
//			switch (NETWORK_SUBTYPE)
//			{
//				case TelephonyManager.NETWORK_TYPE_GPRS:
//				case TelephonyManager.NETWORK_TYPE_EDGE:
//				case TelephonyManager.NETWORK_TYPE_CDMA:
//				case TelephonyManager.NETWORK_TYPE_1xRTT:
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_IDEN:
//					_networkType = LogBaseConstant.NetworkType.Network_2G;
//					break;
//				case TelephonyManager.NETWORK_TYPE_UMTS:
//				case TelephonyManager.NETWORK_TYPE_EVDO_0:
//				case TelephonyManager.NETWORK_TYPE_EVDO_A:
//				case TelephonyManager.NETWORK_TYPE_HSDPA:
//				case TelephonyManager.NETWORK_TYPE_HSUPA:
//				case TelephonyManager.NETWORK_TYPE_HSPA:
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_EVDO_B:
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_EHRPD:
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_HSPAP:
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_TD_SCDMA:
//					_networkType = LogBaseConstant.NetworkType.Network_3G;
//					break;
//				case LogBaseConstant.NetworkSubType.NETWORK_TYPE_LTE:
//					_networkType = LogBaseConstant.NetworkType.Network_4G;
//					break;
//				default:
//					_networkType = LogBaseConstant.NetworkType.Network_UNKNOWN;
//					break;
//			}
//		}
//
//		return _networkType;
//	}

//	public static String getAppVersion(Context context)
//	{
//		String _appVersion = "";
//		String _packageName = "";
//		try
//		{
//			_packageName = context.getPackageName();
//			PackageManager manager = context.getPackageManager();
//			PackageInfo packageInfo = manager.getPackageInfo(_packageName, 0);
//			_appVersion = packageInfo.versionName;
//		}
//		catch (Exception e)
//		{
//			LogBaseLog.e(e);
//		}
//		return _appVersion;
//	}
//
//	public static String getAppLabel(Context context)
//	{
//		String _label = "";
//		try
//		{
//			_label = String.valueOf(context.getPackageManager().getApplicationLabel(context.getApplicationInfo()));
//		}
//		catch (Exception e)
//		{
//			LogBaseLog.e(e);
//		}
//		return _label;
//	}
//
//
//	public static int getScreenHeight(Context context)
//	{
//		int _height = 0;
//		try
//		{
//			DisplayMetrics _displayMetrics = new DisplayMetrics();
//			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			display.getMetrics(_displayMetrics);
//			_height = _displayMetrics.heightPixels;
//		}
//		catch (Exception e)
//		{
//			LogBaseLog.e(e);
//		}
//
//		return _height;
//	}
//
//	public static int getScreenWidth(Context context)
//	{
//		int _width = 0;
//		try
//		{
//			DisplayMetrics _displayMetrics = new DisplayMetrics();
//			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			display.getMetrics(_displayMetrics);
//			_width = _displayMetrics.widthPixels;
//		}
//		catch (Exception e)
//		{
//			LogBaseLog.e(e);
//		}
//
//		return _width;
//	}
//
//	public static int getScreenDpi(Context context)
//	{
//		int _dpi = 0;
//		try
//		{
//			DisplayMetrics _displayMetrics = new DisplayMetrics();
//			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			display.getMetrics(_displayMetrics);
//			_dpi = _displayMetrics.densityDpi;
//		}
//		catch (Exception e)
//		{
//			LogBaseLog.e(e);
//		}
//
//		return _dpi;
//	}
//
	public static String getBrand()
	{
		return Build.BRAND;
	}

	public static String getModel()
	{
		return Build.MODEL;
	}

//	public static String getSystemName()
//	{
//		return LogBaseConstant.Constant.PHONE_SYSTEM;
//	}
//
//	public static String getSystemVersion()
//	{
//		return Build.VERSION.RELEASE;
//	}
//
//	public static String getTimeZone()
//	{
//		String _timeZoneShort = "";
//
//		TimeZone _timeZone = TimeZone.getDefault();
//		String _timeZoneStr = _timeZone.getDisplayName(false, TimeZone.SHORT);
//		Pattern _timeZonePattern = Pattern.compile("[+-][0-9]{1,2}:?[0-9]{1,2}");
//		Matcher _matcher = _timeZonePattern.matcher(_timeZoneStr);
//		String findStr;
//		if (_matcher.find())
//		{
//			findStr = _matcher.group();
//			_timeZoneShort = findStr.replace(":", "");
//		}
//		else
//		{
//			_timeZoneShort = "+0000";
//		}
//
//		return _timeZoneShort;
//	}
//
//	public static String getInvokeTime()
//	{
//		return String.valueOf(System.currentTimeMillis());
//	}
//
//	public static String getRandomCode()
//	{
//		String uuid = UUID.randomUUID().toString().replace("-", "");
//		return uuid;
//	}
//
	public static String getDeviceId(Context context)
	{
		String _deviceId = "";

		_deviceId = getDeviceIdFromInner(context);

		if (TextUtils.isEmpty(_deviceId))
		{
			_deviceId = getDeviceIdFromSd();
		}

		if (TextUtils.isEmpty(_deviceId))
		{
			String _androidId = Settings.Secure.getString(
					context.getContentResolver(), Settings.Secure.ANDROID_ID);
			_deviceId = Build.SERIAL + getMacAddress(context) + _androidId;
			setDeviceIdToInner(_deviceId, context);
			setDeviceIdToSd(_deviceId);
		}

		return _deviceId;
	}
//
//	public static String getNetwork()
//	{
//		String _network = "";
//		if (NETWORK_TYPE == -1)
//		{
//			_network = LogBaseConstant.ApnType.NONE;
//		}
//		else if (NETWORK_TYPE == 1)
//		{
//			_network = LogBaseConstant.ApnType.WIFI;
//		}
//		else
//		{
//			String _apnType = NETWORK_EXTRA_INFO.toUpperCase();
//			if (_apnType.contains(LogBaseConstant.ApnType.CMWAP))
//			{
//				_network = LogBaseConstant.ApnType.CMWAP;
//			}
//			else if (_apnType.contains(LogBaseConstant.ApnType.CMNET))
//			{
//				_network = LogBaseConstant.ApnType.CMNET;
//			}
//			else if (_apnType.contains(LogBaseConstant.ApnType.CTWAP))
//			{
//				_network = LogBaseConstant.ApnType.CTWAP;
//			}
//			else if (_apnType.contains(LogBaseConstant.ApnType.CTNET))
//			{
//				_network = LogBaseConstant.ApnType.CTNET;
//			}
//			else if (_apnType.contains(LogBaseConstant.ApnType.UNINET))
//			{
//				_network = LogBaseConstant.ApnType.UNINET;
//			}
//			else if (_apnType.contains(LogBaseConstant.ApnType.UNIWAP))
//			{
//				_network = LogBaseConstant.ApnType.UNIWAP;
//			}
//			else
//			{
//				_network = LogBaseConstant.ApnType.OTHER;
//			}
//		}
//		return _network;
//	}
//
//	public static String getNetworkApnType()
//	{
//		String _apnType = "";
//		if (NETWORK_TYPE == -1)
//		{
//			_apnType = LogBaseConstant.ApnType.NONE;
//		}
//		else if (NETWORK_TYPE == 1)
//		{
//			_apnType = LogBaseConstant.ApnType.WIFI;
//		}
//		else
//		{
//			_apnType = NETWORK_EXTRA_INFO;
//		}
//		return _apnType;
//	}


	private static String getMacAddressFromAndroid(Context context)
	{
		String address = "";
		try
		{
			WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo != null)
			{
				address = wifiInfo.getMacAddress();
			}
		}
		catch (Exception e)
		{
		}
		return address;
	}
//
	private static String getMacAddressFromJava(Context context)
	{
		String addressStr = "";
		try
		{
			Enumeration networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements())
			{
				NetworkInterface networkInterface = (NetworkInterface) networkInterfaceEnumeration.nextElement();
				if (("wlan0".equals(networkInterface.getName())) || ("eth0".equals(networkInterface.getName())))
				{
					byte[] addressBytes = networkInterface.getHardwareAddress();
					if (addressBytes != null && addressBytes.length != 0)
					{
						StringBuilder addressStrBuilder = new StringBuilder();
						for (byte b : addressBytes)
						{
							addressStrBuilder.append(String.format("%02X:", b));
						}
						if (addressStrBuilder.length() > 0)
						{
							addressStrBuilder.deleteCharAt(addressStrBuilder.length() - 1);
						}
						addressStr = addressStrBuilder.toString().toLowerCase(Locale.getDefault());
						break;
					}
				}
			}
			if (TextUtils.isEmpty(addressStr))
			{
				addressStr = getMacAddressFromAndroid(context);
			}

		}
		catch (Exception e)
		{
		}
		return addressStr;
	}
//
//	// 当前注册的运营商代号
//	private static String getOperatorFromNetworkOperator(String networkOperator)
//	{
//		String _operator = LogBaseConstant.Operator.Other;
//
//		if (!TextUtils.isEmpty(networkOperator))
//		{
//			if ("46000".equals(networkOperator) || "46002".equals(networkOperator) || "46007".equals(networkOperator))
//			{
//				_operator = LogBaseConstant.Operator.CMCC;
//			}
//			else if ("46001".equals(networkOperator) || "46006".equals(networkOperator))
//			{
//				_operator = LogBaseConstant.Operator.CUCC;
//			}
//			else if ("46003".equals(networkOperator) || "46005".equals(networkOperator))
//			{
//				_operator = LogBaseConstant.Operator.CTCC;
//			}
//			else
//			{
//				_operator = LogBaseConstant.Operator.Other;
//			}
//		}
//
//		return _operator;
//	}
//
//	//连接的网络类型
//	private static String getOperatorFormNetworkExtra()
//	{
//		String _operator = LogBaseConstant.Operator.Other;
//		if (!TextUtils.isEmpty(NETWORK_EXTRA_INFO))
//		{
//
//			String _networkExtraInfoUpperCase = NETWORK_EXTRA_INFO.toUpperCase();
//
//			if (_networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.CMNET)
//					|| _networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.CMWAP))
//			{
//				_operator = LogBaseConstant.Operator.CMCC;
//			}
//			else if (_networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.UNINET)
//					|| _networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.UNIWAP)
//					|| _networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.GNET)
//					|| _networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.GWAP))
//			{
//				_operator = LogBaseConstant.Operator.CUCC;
//			}
//			else if (_networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.CTNET)
//					|| _networkExtraInfoUpperCase.contains(LogBaseConstant.ApnType.CTWAP))
//			{
//				_operator = LogBaseConstant.Operator.CTCC;
//			}
//		}
//
//		return _operator;
//	}
//
//	private static String getOperatorFromIccid(String iccid)
//	{
//		String _operator = LogBaseConstant.Operator.Other;
//
//		if (!TextUtils.isEmpty(iccid))
//		{
//			if (iccid.startsWith("898600") || iccid.startsWith("898602"))
//			{
//				_operator = LogBaseConstant.Operator.CMCC;
//			}
//			else if (iccid.startsWith("898601") || iccid.startsWith("898609"))
//			{
//				_operator = LogBaseConstant.Operator.CUCC;
//			}
//			else if (iccid.startsWith("898603") || iccid.startsWith("898606"))
//			{
//				_operator = LogBaseConstant.Operator.CTCC;
//			}
//		}
//
//		return _operator;
//	}
//
	private static String getDeviceIdFromSd()
	{
		String _filePath = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "Download/data/cn.cmgame.sdk/" + "deviceId2";
		String _deviceId = readStr(_filePath);
		return _deviceId;
	}

	private static String getDeviceIdFromInner(Context context)
	{
		String _filePath = context.getFilesDir().toString() + File.separator + "deviceId2";
		String _deviceId = readStr(_filePath);
		return _deviceId;
	}

	private static void setDeviceIdToSd(String deviceId)
	{
		String _filePath = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "Download/data/cn.cmgame.sdk/" + "deviceId2";
		writeStr(_filePath, deviceId);
	}

	private static void setDeviceIdToInner(String deviceId, Context context)
	{
		String _filePath = context.getFilesDir().toString() + File.separator + "deviceId2";
		writeStr(_filePath, deviceId);
	}

	private static String readStr(String filePath)
	{
		String _str = "";
		FileReader fileReader = null;
		try
		{
			if (TextUtils.isEmpty(filePath))
			{
				return "";
			}
			fileReader = new FileReader(filePath);
			BufferedReader bReader = new BufferedReader(fileReader);
			StringBuilder sb = new StringBuilder();
			String s = "";
			while ((s = bReader.readLine()) != null)
			{
				sb.append(s);
			}
			_str = sb.toString();

		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				if (fileReader != null)
				{
					fileReader.close();
				}
			}
			catch (Exception e)
			{
			}
		}
		return _str;
	}

	private static void writeStr(String filePath, String str)
	{

		FileWriter fileWriter = null;
		try
		{
			if (TextUtils.isEmpty(filePath))
			{
				return;
			}

			fileWriter = new FileWriter(filePath);
			fileWriter.write(str);
			fileWriter.flush();
		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				if (fileWriter != null)
				{
					fileWriter.close();
				}
			}
			catch (Exception e)
			{
			}

		}
	}
}
