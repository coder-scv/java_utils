package cn.freedom.commonsutils;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		
		Thumbnails.of("d:/save_1.jpg").scale(1).rotate(-180).toFile("d:/save_1.jpg");
	}
}
