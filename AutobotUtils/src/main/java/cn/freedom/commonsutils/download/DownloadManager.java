package cn.freedom.commonsutils.download;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.freedom.commonsutils.ThreadUtil;

public class DownloadManager {

	private  int maxCount;
	
	private  Set<DownloadExec> set = new HashSet<DownloadExec>();

	private  ExecutorService threadPool;
	
	
	
	public static DownloadManager init(int maxSize) {
		DownloadManager result= new DownloadManager();
		result.maxCount = maxSize;
		 result.threadPool = Executors.newFixedThreadPool(maxSize + 15);
		 return result;
	}
	
	public synchronized void addDownload(DownloadExec exec) {
		
		while( getRunCount() >= maxCount) {
			System.out.println("运行中过多 " + getRunCount() + "/ "+maxCount);
			ThreadUtil.sleep(3);
		}
		exec.setManager(this);
		set.add(exec);
		threadPool.execute(exec);
	}

	private  int getRunCount() {
		
		return set.size();
	}

	public void removeExec(DownloadExec downloadExec) {
		System.out.println("一个工作执行完成");
		set.remove(downloadExec);
		
	}
}
