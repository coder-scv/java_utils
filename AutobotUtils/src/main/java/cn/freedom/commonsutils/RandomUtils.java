package cn.freedom.commonsutils;

import java.util.Random;

public class RandomUtils {
	private static Random random = new Random();

	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}

	public static int nextInt(int start, int end) {
		int bound = end - start;
		return start + random.nextInt(bound);
	}
}
