package cn.freedom.commonsutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.codec.digest.DigestUtils;

public class FileUtils {
	public static File createTempFile(String dirPath) {
		try {
			File dir = new File(dirPath);
			if (dir.exists() && dir.isFile()) {
				return null;
			}
			dir.mkdirs();
			File tempFile = null;
			do {
				tempFile = new File(dir.getAbsolutePath() + "/" + StringUtils.getRandomFileName());
			} while (tempFile.exists());
			tempFile.createNewFile();
			return tempFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public static File createTempFileAsSource(String file) {
		return createTempFile( new File(file).getParentFile().getAbsolutePath());
	}

	public static String getFileTypeName(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index > 0 && index < fileName.length() - 1) {
			return fileName.substring(index + 1);
		}
		return "";
	}

	public static String getFileNameNoType(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index > 0 && index < fileName.length() - 1) {
			return fileName.substring(0, index);
		}
		return "";
	}

	public static File createNewFile(String dirPath, String fileName) {
		return createNewFile(dirPath, fileName, true);
	}

	public static File createNewFile(String dirPath, String fileName, boolean cover) {
		try {
			File dir = new File(dirPath);
			if (dir.exists() && dir.isFile()) {
				return null;
			}
			dir.mkdirs();

			File tempFile = new File(dir.getAbsolutePath() + "/" + fileName);
			int index = 0;
			String realName = getFileNameNoType(fileName);
			String typeName = getFileTypeName(fileName);
			while (tempFile.exists() && !cover) {
				index++;
				tempFile = new File(dir.getAbsolutePath() + "/" + realName + "(" + index + ")." + typeName);
			}
			tempFile.createNewFile();
			return tempFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String getFileMd5(File file) {
		try {
			InputStream in = new FileInputStream(file);
			String md5 = DigestUtils.md5Hex(in);
			in.close();
			return md5;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileNameFromeUrl(String url) {
		url = url.replace("http://", "");
		int endIndex = url.lastIndexOf("/");
		if (endIndex != -1) {
			url = url.substring(endIndex);
		}
		return url;
	}

	private static String[] pathWords1 =
	{ " " };
	private static String[] pathWords2 =
	{ "/", "%", "\\", "." };
	private static String[] htmlWords3 =
	{ "<br>", "<br/>", "<br />", "br" };

	public static String replacePathWord(String str) {
		for (int i = 0; i < pathWords1.length; i++) {
			str = str.replace(pathWords1[i], "");
		}
		for (int i = 0; i < pathWords2.length; i++) {
			str = str.replace(pathWords2[i], "_");
		}
		for (int i = 0; i < pathWords2.length; i++) {
			str = str.replace(pathWords2[i], "\n");
		}
		str = str.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5]", "_");
		return str;
	}

	public static String tempHTMLBR(String str) {
		for (int i = 0; i < htmlWords3.length; i++) {
			str = str.replace(htmlWords3[i], "(BR)");
		}
		return str;
	}

	public static String replaceHTMLWord(String str) {
		str = str.replace("(BR)", "\r\n");
		return str;
	}

	public static boolean deleteFile(String tempFile) {
		File target = new File(tempFile);
		if (target.exists()) {
			target.delete();
		}
		return true;
	}

	public static boolean deleteFile(File tempFile) {
		if (tempFile.exists()) {
			tempFile.delete();
		}
		return true;
	}
	public static void delDir(String f) {
		delDir(new File(f));
	}
	public static void delDir(File f) {
	    // 判断是否是一个目录, 不是的话跳过, 直接删除; 如果是一个目录, 先将其内容清空.
	    if(f.isDirectory()) {
	        // 获取子文件/目录
	        File[] subFiles = f.listFiles();
	        // 遍历该目录
	        for (File subFile : subFiles) {
	            // 递归调用删除该文件: 如果这是一个空目录或文件, 一次递归就可删除. 如果这是一个非空目录, 多次
	            // 递归清空其内容后再删除
	            delDir(subFile);
	        }
	    }
	    // 删除空目录或文件
	    f.delete();
	}

	static String[] needReplaceStr =
	{ ".", ":", "~", "·", "、", "：", ".", ":", "↑", "[", "]", " ", "—", "-", "（", "）", " ", "/", "\\", "+", "*", "__", "__",

	};

	public static String replaceNotMeanWord(String source) {
		String result = source;
		for (int j = 0; j < needReplaceStr.length; j++) {
			result = result.replace(needReplaceStr[j], "_");
		}
		while (result.startsWith("_")) {
			result = result.substring(1);
		}
		return result.trim();
	}

	public static void byte2image(byte[] data, String path) {
		if (data.length < 3 || path.equals("")) return;
		try {
			FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
			System.out.println("Make Picture success,Please find image in " + path);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}
	}

	public static void writeStringArray(String file, List<String> list) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (int i = 0; i < list.size(); i++) {
				bw.write(list.get(i));
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {}
	}

	public static String read(String path, char commentsToken, String LineMarking) {
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == commentsToken) {
					continue;
				} else {
					sb.append(line);
					sb.append(LineMarking);
				}
			}
			sb.setLength(sb.length() - LineMarking.length());
			br.close();
			String result = sb.toString();

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String readLine(String path, int lineIndex) {
		try {
			File file = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			String result = "";
			int index = 0;
			while ((line = br.readLine()) != null) {
				if (index == lineIndex) {
					result = line;
					break;
				}
				index++;
			}
			br.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public static boolean hasFile(String path) {
		return new File(path).exists();
	}

}
