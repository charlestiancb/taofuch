package com.scoop.crawler.cnki.request;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

public class Http {
	private static DefaultHttpClient client = new DefaultHttpClient();
	private static long preTime = System.currentTimeMillis();
	/** 是否已经判断过代理情况！ */
	private static boolean hasProcProxy = false;

	public static String getPageContent(String url, String defaultEncode) {
		if (System.currentTimeMillis() - preTime > 10 * 60 * 1000) {
			try {
				client = new DefaultHttpClient();
				Thread.sleep(1 * 60 * 1000);// 等待一分钟
			} catch (InterruptedException e) {
			}
			preTime = System.currentTimeMillis();
		}
		url = url == null ? "" : url.trim();
		procProxy();
		//
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			String charset = ContentType.getOrDefault(response.getEntity()).getCharset().name();
			if (response.getStatusLine().getStatusCode() != 200) {
				return "";
			}
			charset = charset == null ? defaultEncode : charset;
			//
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new IllegalArgumentException("HTTP entity may not be null");
			}
			InputStream instream = entity.getContent();
			if (instream == null) {
				return "";
			}
			if (entity.getContentLength() > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
			}
			// Gzip
			Header header = entity.getContentEncoding();
			if (header != null) {
				for (HeaderElement ele : header.getElements()) {
					if (ele != null && ele.getName().equalsIgnoreCase("gzip")) {
						instream = new GZIPInputStream(entity.getContent());
						break;
					}
				}
			}
			int i = (int) entity.getContentLength();
			if (i < 0) {
				i = 4096;
			}
			Reader reader = new InputStreamReader(instream, charset);
			CharArrayBuffer buffer = new CharArrayBuffer(i);
			try {
				char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
			} finally {
				reader.close();
			}
			return buffer.toString();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 判断是否需要代理访问网络！因为我是在公司开发，公司访问网络是需要代理的！
	 */
	private static void procProxy() {
		if (hasProcProxy) {
			return;
		}
		try {
			HttpResponse response = client.execute(new HttpGet("http://www.cnki.net/"));
			String charset = "UTF-8";
			String html = EntityUtils.toString(response.getEntity(), charset);
			if (response.getStatusLine().getStatusCode() == 403
					&& html.indexOf("http://oa.vemic.com/system_support/trouble_ticket_add.php") > -1) {
				// 代理方式访问网络
				client.getCredentialsProvider().setCredentials(	new AuthScope("192.168.16.187", 8080),
																new UsernamePasswordCredentials("taofucheng",
																								"taofuchok"));
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("192.168.16.187", 8080));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		hasProcProxy = true;
	}
}
