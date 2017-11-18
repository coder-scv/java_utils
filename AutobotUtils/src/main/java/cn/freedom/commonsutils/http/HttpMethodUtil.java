package cn.freedom.commonsutils.http;

import org.apache.http.client.methods.HttpGet;

public class HttpMethodUtil {
	public static HttpGet createHttpGet(String sourceUrl) {
		HttpGet httpGet = new HttpGet(sourceUrl);
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Cache-Control", "no-cache");
		httpGet.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");

		return httpGet;
	}

}
