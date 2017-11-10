package cn.freedom.commonsutils.number;

/** 提供各种二的指数次进制 编码字符串转换
 * 
 * 
 * */
public class NumberStringUtils {
	public static enum Format {
		BASE2("01"), BASE8("01234567"), BASE4("0123"), BASE16("0123456789ABCDEF"), BASE32("0123456789ABCDEFGHIJKLMNOPQRSTUV");

		String baseStr;
		int zhishu;

		private Format(String baseStr) {
			this.baseStr = baseStr;

			int test = 1;
			for (int i = 1; true; i++) {
				test = test * 2;
				if (baseStr.length() == test) {
					zhishu = i;
					break;
				}
			}
		}

		public FormatNumber pack(String str) {
			str = str.toUpperCase();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (!this.baseStr.contains(String.valueOf(c))) {
					throw new RuntimeException("待打包数据有值超出量程");
				}
			}
			while (str.startsWith(String.valueOf(baseStr.charAt(0)))) {
				str = str.substring(1);
			}

			return new FormatNumber(str, this);
		}
	}

	public static class FormatNumber {
		public String numStr;
		Format format;

		public FormatNumber(String numStr, Format format) {
			super();
			this.numStr = numStr;
			this.format = format;
		}

		@Override
		public String toString() {
			return String.format("FormatNumber(format:%02d) " + numStr + "", format.baseStr.length());
		}

	}

	public static void main(String[] args) {

		FormatNumber formatNumber1 = Format.BASE8.pack("13700");
		formatNumber1 = parse(formatNumber1, Format.BASE2);
		System.out.println(formatNumber1);
		formatNumber1 = parse(formatNumber1, Format.BASE16);
		System.out.println(formatNumber1);

		FormatNumber formatNumber = Format.BASE16.pack("17C0");
		formatNumber = parse(formatNumber, Format.BASE2);
		System.out.println(formatNumber);

		formatNumber = parse(formatNumber, Format.BASE8);
		System.out.println(formatNumber);

		formatNumber = parse(formatNumber, Format.BASE16);
		System.out.println(formatNumber);

		formatNumber = parse(formatNumber, Format.BASE32);
		System.out.println(formatNumber);
		formatNumber = parse(formatNumber, Format.BASE2);
		System.out.println(formatNumber);
	}

	public static FormatNumber parse(FormatNumber fromNumber, Format toFromat) {
		Format fromFormat = fromNumber.format;
		String fromStr = fromNumber.numStr;
		int toFormatLen = toFromat.baseStr.length();

		if (fromFormat == Format.BASE2 || toFromat == Format.BASE2) {
			if (fromFormat.baseStr.length() > toFromat.baseStr.length()) {
				int p = fromFormat.zhishu;

				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < fromStr.length(); i++) {
					char c = fromStr.charAt(i);
					String newStr = "";
					int index = fromFormat.baseStr.indexOf(c);
					while (index >= toFormatLen) {
						int g = index % toFormatLen;
						newStr = toFromat.baseStr.charAt(g) + newStr;
						index = index / toFormatLen;
					}
					newStr = toFromat.baseStr.charAt(index) + newStr;

					while (newStr.length() < p) {
						newStr = toFromat.baseStr.charAt(0) + newStr;
					}

					sb.append(newStr);
				}
				return toFromat.pack(sb.toString());
			} else {
				int key = toFromat.zhishu;
				String result = "";
				int fromFormatLen = fromFormat.baseStr.length();
				while (fromStr.length() > key) {
					String str = fromStr.substring(fromStr.length() - key, fromStr.length());

					int weizhi = 0;
					for (int i = 0; i < str.length(); i++) {

						int len = (str.length() - 1 - i);

						int test = 1;
						for (int j = 0; j < len; j++) {
							test = test * fromFormatLen;
						}
						weizhi += fromFormat.baseStr.indexOf(str.charAt(i)) * test;
					}

					result = (toFromat.baseStr.charAt(weizhi)) + result;
					fromStr = fromStr.substring(0, fromStr.length() - key);
				}
				if (fromStr.length() > 0) {
					String str = fromStr;
					while (str.length() < key) {
						str = toFromat.baseStr.charAt(0) + str;
					}
					int weizhi = 0;
					for (int i = 0; i < str.length(); i++) {

						int len = (str.length() - 1 - i);

						int test = 1;
						for (int j = 0; j < len; j++) {
							test = test * fromFormatLen;
						}
						weizhi += fromFormat.baseStr.indexOf(str.charAt(i)) * test;
					}

					result = (toFromat.baseStr.charAt(weizhi)) + result;
				}

				return toFromat.pack(result);
			}
		} else {
			return parse(parse(fromNumber, Format.BASE2), toFromat);

		}

	}

	public static String binaryStrToHexStr(String binaryStr) {

		if (binaryStr == null || binaryStr.equals("") || binaryStr.length() % 4 != 0) {
			return null;
		}

		StringBuffer sbs = new StringBuffer();
		// 二进制字符串是4的倍数，所以四位二进制转换成一位十六进制
		for (int i = 0; i < binaryStr.length() / 4; i++) {
			String subStr = binaryStr.substring(i * 4, i * 4 + 4);
			String hexStr = Integer.toHexString(Integer.parseInt(subStr, 2));
			sbs.append(hexStr);
		}

		return sbs.toString();
	}
}
