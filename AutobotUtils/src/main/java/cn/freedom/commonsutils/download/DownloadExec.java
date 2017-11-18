package cn.freedom.commonsutils.download;

import java.util.HashMap;
import java.util.Map;

import cn.freedom.commonsutils.http.JsoupUtil;



public abstract class DownloadExec implements Runnable{
	private String srcPath;
	private String savePath;
	private boolean caver;
	
	public Map<String,String> cookies = new HashMap<String,String>();
	
	
	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isCaver() {
		return caver;
	}

	public void setCaver(boolean caver) {
		this.caver = caver;
	}

	public boolean isRunning = false;
	private DownloadManager manager;
	
	public DownloadExec(String srcPath, String savePath, boolean caver) {
		super();
		this.srcPath = srcPath;
		this.savePath = savePath;
		this.caver = caver;
	}

	public void run() {
		isRunning = true;
		try {
			download();
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRunning = false;
			manager.removeExec(this);
		}
	}
	
	public abstract void download() throws Exception;
	
	public byte[] getUrlBytes(String url ) {
		return JsoupUtil.getData(url, cookies, false, null);
	}

	public void setManager(DownloadManager downloadManager) {
		this.manager = downloadManager;
		
	}
	

}
