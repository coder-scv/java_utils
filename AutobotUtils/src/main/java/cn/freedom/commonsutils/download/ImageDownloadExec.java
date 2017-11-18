package cn.freedom.commonsutils.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import cn.freedom.commonsutils.FileUtils;

public class ImageDownloadExec extends DownloadExec {

	public ImageDownloadExec(String srcPath, String savePath, boolean caver) {
		super(srcPath, savePath, caver);

	}

	public ImageDownloadExec(String srcPath, String savePath, Map<String, String> cookies) {
		super(srcPath, savePath, false);
		this.cookies = cookies;

	}

	@Override
	public void download() throws Exception {
		OutputStream os = null;
		try {
			if (FileUtils.hasFile(getSavePath())) {
				return;
			}
			File targetFile = new File(getSavePath());
			File tempFile = new File(getSavePath() + ".tmp");
			targetFile.getParentFile().mkdirs();

			byte[] bs = getUrlBytes(getSrcPath());
			if (bs != null && bs.length > 0) {

				os = new FileOutputStream(targetFile);
				os.write(bs);
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
