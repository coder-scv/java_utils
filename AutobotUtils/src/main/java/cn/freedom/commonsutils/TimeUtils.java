package cn.freedom.commonsutils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String format(Date date) {
		return sdf.format(date);
	}

	public static String format(Date date, String formater) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formater);
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format(date);
	}

	public static String now(String formater) {
		return TimeUtils.format(new Date(), formater);
	}

	public static String now() {
		return TimeUtils.format(new Date());
	}
}
