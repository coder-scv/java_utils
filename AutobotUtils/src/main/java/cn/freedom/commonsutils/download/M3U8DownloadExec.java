package cn.freedom.commonsutils.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.freedom.commonsutils.FileUtils;
import cn.freedom.commonsutils.StringUtils;
import cn.freedom.commonsutils.http.JsoupUtil;

public class M3U8DownloadExec extends DownloadExec {

	public M3U8DownloadExec(String srcPath, String savePath, boolean caver) {
		super(srcPath, savePath, caver);

	}

	public M3U8DownloadExec(String srcPath, String savePath, Map<String, String> cookies) {
		super(srcPath, savePath, false);
		this.cookies = cookies;

	}

	private int getTmpFileCount(File path) {
		String[] fs = path.list();
		int count = 0;
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].endsWith(".tmp")) {
				count++;
			}
		}
		return count;
	}

	private void removeTmpFile(File path) {
		FileUtils.delDir(path);
	}
	
	private File [] tempFiles(int count) {
		File [] result = new File [count];
		for (int i = 0; i < count; i++) {
			File tempFile = new File(getSavePath() + "_/" + i + "_.tmp");
			result[i] = tempFile;
			
		}
		return result;
	}

	@Override
	public void download() throws Exception {
		OutputStream os = null;
		InputStream is = null;
		try {
			if (FileUtils.hasFile(getSavePath())) {
				return;
			}
			File targetFile = new File(getSavePath());
			List<String> tsList = getTSList(getSrcPath());
			int count = tsList.size();
			if(count == 0) {
				return;
			}
			targetFile.getParentFile().mkdirs();

			int tryTime = 0;
			File tempPart =null;
			
			File [] tempFiles = tempFiles(count);
			tempPart = tempFiles[0].getParentFile();
			tempPart.mkdirs();
			
			do {
				for (int i = 0; i < count; i++) {
					File tempFile = tempFiles[i];
					if(FileUtils.hasFile(tempFile.getAbsolutePath())) {
						continue;
					}
					byte[] bs = getUrlBytes(tsList.get(i));
					if (bs != null && bs.length > 0) {
						os = new FileOutputStream(tempFile);
						os.write(bs);
						os.flush();
						os.close();
					}
				}
				int hasDownLen = getTmpFileCount(tempPart);

				if (hasDownLen >= count) {

					BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(targetFile));

					for (int i = 0; i < count; i++) {
						File inFile = tempFiles[i];
						BufferedInputStream bi = new BufferedInputStream(new FileInputStream(inFile));
						byte[] bs = new byte[1024 * 1024];
						int len;
						while ((len = bi.read(bs)) != -1) {
							bo.write(bs, 0, len);
						}
						// 完毕，关闭所有链接
						bo.flush();
						bi.close();

						// inFile.delete();
					}

					bo.flush();
					bo.close();

					removeTmpFile(tempPart);

					System.out.println(targetFile.getAbsolutePath() + "开始合并 成功 删除临时 ts ");
					break;
				} else {
					System.out.println("没有完成全部ts文件 --- " + hasDownLen + " / " + count);

				}

				tryTime++;
			} while (tryTime < 10);

			// out.flush();
			// out.close();
			// temp.renameTo(f);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<String> getTSList(String url) {
		System.out.println(url);
		List<String> result = new ArrayList<String>();
		try {

			String m3u8Base = url.substring(0, url.lastIndexOf("/") + 1);

			String mainIndex = new String(JsoupUtil.getData(url, cookies, false, null));
			String realIndex = m3u8Base + mainIndex.split("\n")[2];

			m3u8Base = realIndex.substring(0, realIndex.lastIndexOf("/") + 1);

			String[] tsIndexs = new String(JsoupUtil.getData(realIndex, cookies, false, null)).split("\n");

			for (int i = 0; i < tsIndexs.length; i++) {
				if (!tsIndexs[i].startsWith("#")) {
					// System.out.println(tsIndexs[i]);
					result.add(m3u8Base + tsIndexs[i]);
				}
				// System.out.println();
			}
		} catch (Exception e) {
			System.err.println("获取数据出错   --- " + url);
			e.printStackTrace();
		}
		return result;
	}

}
