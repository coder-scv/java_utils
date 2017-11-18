package cn.freedom.commonsutils.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.freedom.commonsutils.FileUtils;
import cn.freedom.commonsutils.http.HttpMethodUtil;

public class Mp4DownloadExec extends DownloadExec {

	public Mp4DownloadExec(String srcPath, String savePath, boolean caver) {
		super(srcPath, savePath, caver);

	}

	public Mp4DownloadExec(String srcPath, String savePath, Map<String, String> cookies) {
		super(srcPath, savePath, false);
		this.cookies = cookies;

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
			File tempFile = new File(getSavePath() + ".tmp");
			targetFile.getParentFile().mkdirs();

			HttpGet httpGet = HttpMethodUtil.createHttpGet(getSrcPath());
			HttpClient client = new DefaultHttpClient();

			client.getParams().setIntParameter("http.socket.timeout", 30000);
			HttpResponse httpResponse = client.execute(httpGet);
			is = httpResponse.getEntity().getContent();
			byte[] bs = new byte[512 * 1024];
			int len;
			os = new FileOutputStream(tempFile);
			if (tempFile.exists()) {
				long tempLen = 0;
				tempLen = tempFile.length();
				is.skip(tempLen);
			}
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.flush();
			os.close();
			is.close();

			tempFile.renameTo(targetFile);

		} catch (Exception e) {
			System.err.println("err " + getSrcPath());
			e.printStackTrace();

		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e2) {
			}
			try {
				if (os != null)
					os.close();
			} catch (Exception e2) {
			}

		}

	}

}
