package com.cloudtech.ebusi.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.component.Downloader;
import net.vidageek.crawler.exception.CrawlerException;
import net.vidageek.crawler.page.ErrorPage;
import net.vidageek.crawler.page.OkPage;
import net.vidageek.crawler.page.RejectedMimeTypePage;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class WebDownloader implements Downloader {

	private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C; .NET4.0E)";

	private final ConcurrentLinkedQueue<String> mimeTypesToInclude;

	private final Logger log = Logger.getLogger(WebDownloader.class);

	private final ConcurrentLinkedQueue<Cookie> cookies;

	public WebDownloader(final List<String> mimeTypesToInclude) {
		this(mimeTypesToInclude, new ArrayList<Cookie>());
	}

	public WebDownloader(final List<String> mimeTypesToInclude, final List<Cookie> cookies) {
		this.cookies = new ConcurrentLinkedQueue<Cookie>(cookies);
		this.mimeTypesToInclude = new ConcurrentLinkedQueue<String>(mimeTypesToInclude);
	}

	public WebDownloader() {
		this(Arrays.asList("text/html"));
	}

	public Page get(final String url) {
		try {
			Thread.sleep(1000);// 停个1秒钟，然后再抓取，模拟人的操作有间歇
		} catch (Exception e) {
			e.printStackTrace();
		}
		DefaultHttpClient client = new DefaultHttpClient();
		for (Cookie cookie : cookies) {
			String name = cookie.getName();
			String value = cookie.getValue();
			log.debug("Creating cookie [" + name + " = " + value + "] " + cookie.getDomain());
			BasicClientCookie clientCookie = new BasicClientCookie(name, value);
			clientCookie.setPath(cookie.getPath());
			clientCookie.setDomain(cookie.getDomain());
			client.getCookieStore().addCookie(clientCookie);
		}
		client.getParams().setIntParameter("http.socket.timeout", 15000);
		return get(client, url);
	}

	public Page get(final DefaultHttpClient client, final String url) {
		try {
			// 设置客户端的信息，防止被屏蔽。
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);

			String encodedUrl = encode(url);
			log.debug("Requesting url: [" + encodedUrl + "]");
			HttpGet method = new HttpGet(encodedUrl);

			try {
				HttpResponse response = client.execute(method);
				Status status = Status.fromHttpCode(response.getStatusLine().getStatusCode());

				if (!acceptsMimeType(response.getLastHeader("Content-Type"))) {
					return new RejectedMimeTypePage(url, status, response.getLastHeader("Content-Type").getValue());
				}

				if (Status.OK.equals(status)) {
					List<Cookie> cs = client.getCookieStore().getCookies();
					if (cs != null && !cs.isEmpty()) {
						cookies.clear();
						cookies.addAll(cs);
					}
					CharsetDetector detector = new CharsetDetector();
					detector.setText(read(response.getEntity().getContent()));
					CharsetMatch match = detector.detect();

					log.debug("Detected charset: " + match.getName());

					String content = match.getString();
					CharBuffer buffer = CharBuffer.wrap(content.toCharArray());
					Charset utf8Charset = Charset.forName("UTF-8");
					String utf8Content = new String(utf8Charset.encode(buffer).array(), "UTF-8");

					return new OkPage(url, utf8Content);
				}
				return new ErrorPage(url, status);
			} finally {
				method.abort();
			}

		} catch (IOException e) {
			throw new CrawlerException("Could not retrieve data from " + url, e);
		}
	}

	private boolean acceptsMimeType(final Header header) {
		final String value = header.getValue();
		if (value == null) {
			return false;
		}

		for (String mimeType : mimeTypesToInclude) {
			if (value.contains(mimeType)) {
				return true;
			}
		}
		return false;
	}

	private byte[] read(final InputStream inputStream) {
		byte[] bytes = new byte[1000];
		int i = 0;
		int b;
		try {
			while ((b = inputStream.read()) != -1) {
				bytes[i++] = (byte) b;
				if (bytes.length == i) {
					byte[] newBytes = new byte[(bytes.length * 3) / 2 + 1];
					for (int j = 0; j < bytes.length; j++) {
						newBytes[j] = bytes[j];
					}
					bytes = newBytes;
				}
			}
		} catch (IOException e) {
			new CrawlerException("There was a problem reading stream.", e);
		}

		byte[] copy = Arrays.copyOf(bytes, i);

		return copy;
	}

	private String encode(final String url) {
		String res = "";
		for (char c : url.toCharArray()) {
			if (!":/.?&#=".contains("" + c)) {
				try {
					res += URLEncoder.encode("" + c, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new CrawlerException(	"There is something really wrong with your JVM. It could not find UTF-8 encoding.",
												e);
				}
			} else {
				res += c;
			}
		}

		return res;
	}
}
