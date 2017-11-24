package cn.freedom.commonsutils.encryption.util;

import java.io.UnsupportedEncodingException;

import cn.freedom.commonsutils.RandomUtils;

public class HexStrUtils {

	private static String baseStr = "0123456789ABCDEF";

	public static String getRandomHexStr(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(baseStr.charAt(RandomUtils.nextInt(baseStr.length())));
		}
		return sb.toString();
	}

	/**
	 * 
	* @Title: bcdhex_to_aschex 
	* @Description: byte[]数组 转换为 16进制字符串形式
	*  @param bcdhex
	*  @return    参数描述
	*  String    返回类型描述
	* @throws
	 */
	public static String byteArray2HexStr(byte[] bcdhex) {
		byte[] aschex =
		{ 0, 0 };
		String res = "";
		String tmp = "";
		for (int i = 0; i < bcdhex.length; i++) {
			aschex[1] = hexLowToAsc(bcdhex[i]);
			aschex[0] = hexHighToAsc(bcdhex[i]);
			tmp = new String(aschex);
			res += tmp;
		}
		return res;
	}

	public static byte hexLowToAsc(byte xxc) {
		xxc &= 0x0f;
		if (xxc < 0x0a) xxc += '0';
		else xxc += 0x37;
		return (byte) xxc;
	}

	public static byte hexHighToAsc(int xxc) {
		xxc &= 0xf0;
		xxc = xxc >> 4;
		if (xxc < 0x0a) xxc += '0';
		else xxc += 0x37;
		return (byte) xxc;
	}

	/**
	 * 
	* @Title: aschex_to_bcdhex 
	* @Description: 16进制字符串转换为 byte[]数组形式
	*  @param aschex
	*  @return    参数描述
	*  byte[]    返回类型描述
	* @throws
	 */
	public static byte[] hexStr2ByteArray(String aschex) {
		byte[] aschexByte = aschex.getBytes();
		int j = 0;
		if (aschexByte.length % 2 == 0) {
			j = aschexByte.length / 2;
			byte[] resTmp = new byte[j];
			for (int i = 0; i < j; i++) {
				resTmp[i] = ascToHex(aschexByte[2 * i], aschexByte[2 * i + 1]);
			}
			return resTmp;

		} else {
			j = aschexByte.length / 2 + 1;
			byte[] resTmp = new byte[j];
			for (int i = 0; i < j - 1; i++) {
				resTmp[i] = ascToHex((byte) aschexByte[2 * i], (byte) aschexByte[2 * i + 1]);
			}
			resTmp[j - 1] = ascToHex((byte) aschexByte[2 * (j - 1)], (byte) 0);
			return resTmp;
		}
	}

	public static byte ascToHex(byte ch1, byte ch2) {
		byte ch;
		if (ch1 >= 'A') ch = (byte) ((ch1 - 0x37) << 4);
		else ch = (byte) ((ch1 - '0') << 4);
		if (ch2 >= 'A') ch |= (byte) (ch2 - 0x37);
		else ch |= (byte) (ch2 - '0');
		return ch;
	}

	/**
	 * 
	* @Title: str2HexStr 
	* @Description: 将字符串的 iso8859-1 字节数组 以16进制形式输出
	*  @param str
	*  @return 字符串的16机制编码形式
	*  @throws UnsupportedEncodingException    参数描述
	*  String    
	* @throws
	 */
	public static String str2HexStr(String str) throws UnsupportedEncodingException {
		return byteArray2HexStr(str.getBytes("iso-8859-1"));

	}

	/**
	 * @throws UnsupportedEncodingException 
	 * 
	* @Title: hexStr2Str 
	* @Description: 将16进制字符串 转换为 byte数组并 编码为 iso8859-1字符串
	* @param 16进制字符串
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String hexStr2Str(String str) throws UnsupportedEncodingException {
		return new String(hexStr2ByteArray(str), "iso-8859-1");

	}

	/**
	 * 
	* @Title: positionChange 
	* @Description: 16进制 字符串  字节高地位交换  奇数时结尾字节低位补F
	* @param str
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String positionChange(String str) {
		if (str.length() % 2 == 1) {
			str = str + "F";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i += 2) {
			sb.append(str.charAt(i + 1));
			sb.append(str.charAt(i));
		}
		return sb.toString();
	}

	/**
	 * 
	* @Title: flashback 
	* @Description: 16进制数据取反
	* @param str
	* @return    参数描述
	* String    返回类型描述
	* @throws
	 */
	public static String flashback(String str) {

		byte[] data0 = hexStr2ByteArray(str);
		// byte [] data1 = hexStr2ByteArray("FFFFFFFFFFFFFFFF");

		byte[] result = new byte[data0.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (data0[i] ^ 255);
		}
		return byteArray2HexStr(result);
	}
	
	public static void main(String[] args) {
		for (int i = 1; i < 11; i++) {
			System.out.println("索引 " + i);
			System.out.println(getRandomHexStr(32));
			System.out.println();
		}
	}
}
