package cn.freedom.commonsutils.geo;

import java.text.DecimalFormat;
import java.util.BitSet;

import cn.freedom.commonsutils.number.NumberStringUtils;
import cn.freedom.commonsutils.number.NumberStringUtils.Format;

public class GeoHashUtil {
	private int numbits = 6;
	private double[] start =
	{ 27.8, 116.6 };
	private double[] end =
	{ 28.8, 117.5 };

	private Format format;

	public static GeoHashUtil init(int numbits, double[] startLatLng, double[] endLatLng, Format format) {
		GeoHashUtil geoHashUtil = new GeoHashUtil();
		geoHashUtil.numbits = numbits;
		geoHashUtil.start = startLatLng;
		geoHashUtil.end = endLatLng;
		geoHashUtil.format = format;
		return geoHashUtil;
	}

	public String getMaxGeoHash() {
		String s = encode(end[0], end[1]);
		System.out.println("max " + s);
		maxStrLen = s.length();
		return s;
	}

	public String[][] getSplitArray() {
		int len = 2 << numbits;
		String[][] array = new String[len][len];
		double latPart = (end[0] - start[0]) / ((double) (len * 2));
		double lngPart = (end[1] - start[1]) / ((double) (len * 2));

		System.out.println(latPart + "  " + latPart);

		for (int i = 0; i < len; i++) {
			double lat1 = start[0] + (double) (i * 2 + 1) * latPart;
			for (int j = 0; j < array.length; j++) {
				double lng1 = start[1] + (double) (j * 2 + 1) * lngPart;
				array[i][j] = encode(lat1, lng1);
			}
		}
		return array;

	}

	public static void main(String[] args) {
		double[] start =
		{ 27.8, 116.6 };
		double[] end =
		{ 28.8, 117.5 };
		GeoHashUtil e = GeoHashUtil.init(30, start, end, Format.BASE16);

		String s = e.encode(28.27684916, 117.0343119);
		System.out.println(s);
		double[] latlon = e.decode(s);
		DecimalFormat df = new DecimalFormat("0.00000");
		System.out.println(df.format(latlon[0]) + ", " + df.format(latlon[1]));

	}

	private int maxStrLen = 0;

	private GeoHashUtil() {
		super();
	}

	/**
	 * 将Geohash字串解码成经纬值
	 * 
	 * @param geohash
	 *            待解码的Geohash字串
	 * @return 经纬值数组
	 */
	public double[] decode(String geohash) {

		geohash = NumberStringUtils.parse(format.pack(geohash), format.BASE2).numStr;
		System.out.println(geohash);
		StringBuilder buffer = new StringBuilder(geohash);

		BitSet lonset = new BitSet();
		BitSet latset = new BitSet();

		// even bits
		int j = 0;
		for (int i = 0; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length()) isSet = buffer.charAt(i) == '1';
			lonset.set(j++, isSet);
		}

		// odd bits
		j = 0;
		for (int i = 1; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length()) isSet = buffer.charAt(i) == '1';
			latset.set(j++, isSet);
		}

		double lat = decode(latset, start[0], end[0]);
		double lon = decode(lonset, start[1], end[1]);

		DecimalFormat df = new DecimalFormat("0.00000");
		return new double[]
		{ Double.parseDouble(df.format(lat)), Double.parseDouble(df.format(lon)) };
	}

	/**
	 * 根据二进制编码串和指定的数值变化范围，计算得到经/纬值
	 * 
	 * @param bs
	 *            经/纬二进制编码串
	 * @param floor
	 *            下限
	 * @param ceiling
	 *            上限
	 * @return 经/纬值
	 */
	private double decode(BitSet bs, double floor, double ceiling) {

		double mid = 0;
		for (int i = 0; i < bs.length(); i++) {
			mid = (floor + ceiling) / 2;
			if (bs.get(i)) floor = mid;
			else ceiling = mid;
		}
		return mid;
	}

	/**
	 * 根据经纬值得到Geohash字串
	 * 
	 * @param lat
	 *            纬度值
	 * @param lon
	 *            经度值
	 * @return Geohash字串
	 */

	public String encode(double lat, double lon) {
		BitSet latbits = getBits(lat, start[0], end[0]);
		BitSet lonbits = getBits(lon, start[1], end[1]);
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < numbits; i += 2) {
			StringBuffer sb1 = new StringBuffer();
			sb1.append((lonbits.get(i)) ? '1' : '0');
			sb1.append((latbits.get(i)) ? '1' : '0');
			sb1.append((lonbits.get(i + 1)) ? '1' : '0');
			sb1.append((latbits.get(i + 1)) ? '1' : '0');

			String byteStr = sb1.toString();
			buffer.append(byteStr);
		}

		String result = buffer.toString();
		System.out.println(result);
		result = NumberStringUtils.parse(format.BASE2.pack(result), format).numStr;
		return result;
	}

	/**
	 * 得到经/纬度对应的二进制编码
	 * 
	 * @param lat
	 *            经/纬度
	 * @param floor
	 *            下限
	 * @param ceiling
	 *            上限
	 * @return 二进制编码串
	 */
	private BitSet getBits(double lat, double floor, double ceiling) {
		BitSet buffer = new BitSet(numbits);
		for (int i = 0; i < numbits; i++) {
			double mid = (floor + ceiling) / 2;
			if (lat >= mid) {
				buffer.set(i);
				floor = mid;
			} else {
				ceiling = mid;
			}
		}
		return buffer;
	}

	public static String hexStrToBinaryStr(String hexString) {

		if (hexString == null || hexString.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		// 将每一个十六进制字符分别转换成一个四位的二进制字符
		for (int i = 0; i < hexString.length(); i++) {
			String indexStr = hexString.substring(i, i + 1);
			String binaryStr = Integer.toBinaryString(Integer.parseInt(indexStr, 16));
			while (binaryStr.length() < 4) {
				binaryStr = "0" + binaryStr;
			}
			sb.append(binaryStr);
		}

		return sb.toString();
	}

}
