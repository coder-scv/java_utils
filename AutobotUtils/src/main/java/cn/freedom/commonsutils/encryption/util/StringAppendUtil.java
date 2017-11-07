package cn.freedom.commonsutils.encryption.util;

public class StringAppendUtil {

	/**
	 * 
	* @Title: appendStr 
	* @Description: 16进制字符串 补足8 字节的整数倍
	* @param dataHex  数据
	* @param appendStr  补位字符串   补位字符串为 16进制 2字符 字符串  第一个字符用于结尾 第二个字符用于 补齐
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String appendStr(String dataHex, String appendStr) {
		String apStr1 = appendStr.substring(0, 2);
		String apStr2 = appendStr.substring(appendStr.length() - 2);

		int len = dataHex.length();
		if (len % 16 != 0) {
			int s = len % 16;
			dataHex = dataHex + apStr1;
			for (int i = s + 2; i < 16; i = i + 2) {
				dataHex = dataHex + apStr2;
			}
		}
		dataHex = dataHex.replace(" ", "");
		return dataHex;
	}

	/**
	 * 
	* @Title: removeAppend 
	* @Description: 移除16进制字符串的补位字符串  
	* @param dataHex  数据
	* @param appendStr   补位字符串   补位字符串为 16进制 2字符 字符串  第一个字符用于结尾 第二个字符用于 补齐
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String removeAppend(String dataHex, String appendStr) {
		String result = dataHex;
		String apStr1 = appendStr.substring(0, 2);
		String apStr2 = appendStr.substring(appendStr.length() - 2);
		if (dataHex.contains(apStr1)) {
			while (true) {
				if (result.endsWith(apStr1)) {
					result = result.substring(0, (result.length() - 2));
					break;
				} else if (result.endsWith(apStr2)) {
					result = result.substring(0, (result.length() - 2));
					continue;
				} else {
					break;
				}
			}
			if (result.length() % 2 != 0) {
				result = dataHex;
			}
		}
		return result;
	}

}
