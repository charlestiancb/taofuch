package com.scoop.crawler.weibo.fetch;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.parser.HtmlStyleParser;
import com.scoop.crawler.weibo.parser.JsonStyle4CommonParser;
import com.scoop.crawler.weibo.parser.JsonStyle4SearchParser;
import com.scoop.crawler.weibo.parser.TempUrl;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.MysqlDataSource;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;

/**
 * 抓取新浪微博的个人页面的微博信息
 * 
 * @author taofucheng
 * 
 */
public class FetchSinaWeibo extends FetchSina {
	protected static DefaultHttpClient client = null;
	protected static DataSource dataSource = null;
	protected static RequestFailedHandler handler;

	private FetchSinaWeibo() {
	}

	/**
	 * 抓取微博中的内容
	 * 
	 * @param sinaUserName
	 *            新浪微博账号
	 * @param password
	 *            微博密码
	 * @param weiboBaseUrl
	 *            需要抓取的起始微博网址，如果有变量，请使用{0}、{1}等方式指定
	 * @param wordsFile
	 *            需要批量操作的词文件，与网址中对应的词保持一致
	 * @return
	 */
	public static boolean fetch(String sinaUserName, String password, String weiboBaseUrl, String wordsFile) {
		if (StringUtils.isBlank(weiboBaseUrl)) {
			return false;
		}
		// 然后不停滴读取页面中的微博信息
		client = SinaWeiboRequest.getHttpClient(sinaUserName, password);
		try {
			if (dataSource == null) {
				dataSource = new MysqlDataSource();
			}
			if (handler == null) {
				handler = new RequestFailedHandler(client, dataSource);
			}
			handler.reTry();
			List<String> urls = parseUrls(weiboBaseUrl, wordsFile);
			for (String url : urls) {
				saveBaseUrl(new TempUrl(weiboBaseUrl, url));
				fetch(client, dataSource, url);
				saveBaseUrl(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dataSource != null) {
				dataSource.close();
			}
		}
		return true;
	}

	/**
	 * 将变量等内容组装成苦干个网址
	 * 
	 * @param weiboBaseUrl
	 * @param wordsFile
	 * @return
	 * @throws IOException
	 */
	protected static List<String> parseUrls(String weiboBaseUrl, String wordsFile) throws IOException {
		// 判断有没有指定文件，同时给定的网址中有没有变量，都有的话，进行循环
		List<String> urls = new ArrayList<String>();
		boolean hasVar = Pattern.compile("\\{\\d+\\}").matcher(weiboBaseUrl).find();
		if (hasVar) {
			File file = new File(wordsFile);
			boolean hasFile = file.isFile();
			if (!hasFile) {
				urls.add(weiboBaseUrl);
				return urls;
			}
			// 读取一行，然后将该行删除！
			List<String> lines = FileUtils.readLines(file, "GBK");
			if (lines == null || lines.isEmpty()) {
				urls.add(weiboBaseUrl);
			}
			for (String line : lines) {
				if (StringUtils.isBlank(line)) {
					continue;
				}
				String url = weiboBaseUrl;
				String[] s = line.trim().split(",");
				for (int i = 0; i < s.length; i++) {
					url = url.replaceAll("\\{" + i + "\\}", URLEncoder.encode(s[i].trim(), "UTF-8"));
				}
				urls.add(url);
			}
			return urls;
		} else {
			urls.add(weiboBaseUrl);
		}
		return urls;
	}

	/**
	 * 循环抓取微博内容。
	 * 
	 * @param client
	 * @param csvWriter
	 * @param weiboUrl
	 * @param csvFile
	 */
	public static void fetch(DefaultHttpClient client, DataSource dataSource, String weiboUrl) {
		String tmpUrl = getRealUrl(weiboUrl);
		if (tmpUrl.isEmpty()) {
			return;
		}
		int idx = tmpUrl.lastIndexOf("page=");
		int curPage = idx == -1 ? 1 : NumberUtils.toInt(StringUtils.trim(tmpUrl.substring(idx + "page=".length())), 1);
		while (maxLimit == -1 || curPage <= maxLimit) {
			boolean hasParam = weiboUrl.indexOf("?") > -1;// 判断链接中是否有参数
			tmpUrl = weiboUrl + (hasParam ? "&" : "?") + "page=" + (curPage++);
			String content = SinaWeiboRequest.request(client, tmpUrl, handler, FailedNode.MAIN);
			parseHtml(client, dataSource, content, tmpUrl);
		}
	}

	/**
	 * 看指定的URL中有没有page信息，如果有，则确定其是否在最后。
	 * 
	 * @param weiboUrl
	 * @return
	 */
	private static String getRealUrl(String weiboUrl) {
		weiboUrl = StringUtils.trimToEmpty(weiboUrl);
		if (StringUtils.isBlank(weiboUrl)) {
			return weiboUrl;
		}
		String[] parts = weiboUrl.split("&");
		StringBuffer sb = new StringBuffer();
		String pageInfo = "";
		for (String p : parts) {
			p = StringUtils.trim(p);
			if (p.startsWith("page=")) {
				pageInfo = p;
			} else {
				sb.append("&");
				sb.append(p);
			}
		}
		sb.append("&");
		// 如果URL中有分页信息，则直接添加到URL最后，否则默认为page=1
		if (StringUtils.isNotEmpty(pageInfo)) {
			sb.append(pageInfo);
		} else {
			sb.append("page=1");
		}
		return sb.substring(1);
	}

	/**
	 * 解析每页微博中的内容。
	 * 
	 * @param html
	 * @param csvFile
	 */
	protected static void parseHtml(DefaultHttpClient client, DataSource dataSource, String html, String weiboUrl) {
		// 评论数：评论详细信息列表，包括评论者id,评论者姓名，评论者评论时间，评论者评论内容，评论者个人资料
		// 写入csv文件
		if (StringUtils.isBlank(html)) {
			return;
		}
		// 将内容构建成一个HtmlParser可解析的对象
		try {
			Document doc = Jsoup.parse(html);
			// 将一条条的微博根据标签的特征统一取出来
			Elements eles = doc.getElementsByClass("MIB_feed_c");
			if (eles.size() > 0) {
				// 如果这样的格式存在，则说明是那种HTML格式的，如：http://gov.weibo.com/profile.php?uid=sciencenet&ref=
				new HtmlStyleParser(client, dataSource).parse(eles);
			} else {
				// 否则就是那种js的json格式的内容方式，使用json的方式进行解析
				JsonStyle4CommonParser commonParser = new JsonStyle4CommonParser(client, dataSource);
				JsonStyle4SearchParser searchParser = new JsonStyle4SearchParser(client, dataSource);
				if (searchParser.isBelong(html)) {
					searchParser.parse(html);
				} else if (commonParser.isBelong(html)) {
					commonParser.setCurUrl(weiboUrl);
					commonParser.parse(html);
				} else {
					return;
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}