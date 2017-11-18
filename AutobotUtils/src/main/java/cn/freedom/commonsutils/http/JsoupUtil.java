
package cn.freedom.commonsutils.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtil {
	public static Connection getConnect(String url) {

		String host = "";
		if (url.startsWith("http://")) {
			host = url.substring("http://".length());
		} else if (url.startsWith("https://")) {
			host = url.substring("https://".length());
		}
		int index = host.indexOf("/");
		if (index > 0) {
			host = host.substring(0, index);
		}
		Connection connection = Jsoup.connect(url)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate, sdch").header("Accept-Language", "zh-CN,zh;q=0.8")
				.header("Connection", "keep-alive").header("Host", host).header("Referer", url)

				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
		

		connection.timeout(30000);//

		return connection;
	}

	public static Document getHtml(String url) {
		return getHtml(url, null, "UTF-8");
	}

	public static Document getHtml(String url, Map<String, String> cookies) {

		return getHtml(url, cookies, "UTF-8");
	}

	public static Document getHtml(String url, Map<String, String> cookies, String encode) {
		Document doc = null;
		doc = Jsoup.parse(getHtmlString(url, cookies, encode));
		return doc;
	}

	public static String getHtmlString(String url, Map<String, String> cookies, String encode) {
		byte[] bodybyte = getData(url, cookies, false, null);
		String htmlStr = null;
		try {
			htmlStr = new String(bodybyte, encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return htmlStr;
	}

	public static byte[] getData(String url, Map<String, String> cookies, boolean isPost, Map<String, String> map
		) {
		// Document doc = null;
		int retryTime = 5;
		do {
			try {
				Connection connection = getConnect(url);
				if (cookies != null) {
					connection.cookies(cookies);
				}

				Response res = null;
				if (isPost) {
					res = connection.ignoreContentType(true).method(Method.POST).data(map).execute();

				} else {

					res =connection.ignoreContentType(true).execute();
				}
				if (cookies != null) {

					cookies.putAll(res.cookies());
				}
				byte[] bodybyte = res.bodyAsBytes();
				return bodybyte;
			} catch (java.net.UnknownHostException e1) {
				try {
					System.err.println("java.net.UnknownHostException " + e1.getMessage());
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				retryTime--;
				continue;
			} catch (java.net.SocketException se) {
				System.err.println(se.getMessage() + "  == " + url);
				retryTime--;
				continue;
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;
		} while (retryTime > 0);
		return null;
	}

	public static Document postHtml(String url, Map<String, String> cookies, String encode, Map<String, String> map) {

		Document doc = Jsoup.parse(postUrl(url, cookies, encode, map));

		return doc;
	}

	public static String postUrl(String url, Map<String, String> cookies, String encode, Map<String, String> map) {
		byte[] bodybyte = getData(url, cookies, true, map);
		String htmlStr = null;
		try {
			htmlStr = new String(bodybyte, encode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return htmlStr;
	}
}
