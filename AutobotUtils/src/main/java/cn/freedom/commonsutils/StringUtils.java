package cn.freedom.commonsutils;

import java.util.Date;

import cn.freedom.commonsutils.encryption.util.HexStrUtils;
import cn.freedom.commonsutils.encryption.util.StringAppendUtil;

public class StringUtils {
	/**
	 * 
	* @Title: isEmpty 
	* @Description: 判断字符串是否为空
	* @param str
	* @return    参数描述
	* boolean    返回类型描述
	* @throws
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 
	* @Title: isNotEmpty 
	* @Description: 判断字符串是否不为空
	* @param str
	* @return    参数描述
	* boolean    返回类型描述
	* @throws
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 
	* @Title: join 
	* @Description: 数组拼接为字符串  以  splitString 分割
	* @param row
	* @param string
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String join(String[] array, String splitString) {
		StringBuffer sb = new StringBuffer();
		for (String string2 : array) {
			sb.append(string2);
			sb.append(splitString);
		}
		String result = sb.toString();
		result = result.substring(result.length() - splitString.length());
		return result;
	}

	/**
	 * 
	* @Title: isAllNumber 
	* @Description: 判断字符串是否全为 数字
	* @param str
	* @return    参数描述
	* boolean    返回类型描述
	* @throws
	 */
	public static boolean isAllNumber(String str) {
		try {
			char[] cs = str.toCharArray();
			for (int i = 0; i < cs.length; i++) {
				Integer.parseInt(String.valueOf(cs[i]));
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String now() {
		return TimeUtils.format(new Date());
	}
	
	private static String baseChars = "1234567890zxcvbmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP";

	public static String getRandomStr(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(baseChars.charAt(RandomUtils.nextInt(baseChars.length())));
		}
		return sb.toString();
	}

	public static String getRandomHexStr(int len) {
		return HexStrUtils.getRandomHexStr(len);
	}

	public static String getRandomFileName() {
		return TimeUtils.now("yyyyMMddHHmmss") + getRandomStr(6);
	}
	
	public static boolean isAllEmpty(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			if (isNotEmpty(strings[i])) {
				return false;
			}
		}
		return true;
	}

	public static String[] splitString(String str) {
		str = str.trim().replace("\t", " ").toUpperCase();
		while (str.contains("  ")) {
			str = str.replace("  ", " ");
		}
		str = str.replace(" ", ",");
		String[] headStrs = str.split(",");
		return headStrs;
	}
}
