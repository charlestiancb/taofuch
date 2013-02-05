package com.scoop.crawler.weibo.fetch;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.parser.TempUrl;
import com.scoop.crawler.weibo.parser.WeiboUserParser;
import com.scoop.crawler.weibo.parser.WeiboCompanyParser;
import com.scoop.crawler.weibo.parser.WeiboSearchParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.JdbcDataSource;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.util.ThreadUtils;

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
	public static boolean fetch(String sinaUserName, String password, String weiboBaseUrl, String[] wordsFiles) {
		if (StringUtils.isBlank(weiboBaseUrl)) {
			return false;
		}
		// 然后不停滴读取页面中的微博信息
		client = SinaWeiboRequest.getHttpClient(sinaUserName, password);
		try {
			if (dataSource == null) {
				dataSource = new JdbcDataSource();
			}
			if (handler == null) {
				handler = new RequestFailedHandler(client, dataSource);
			}
			handler.reTry();
			if (wordsFiles != null && wordsFiles.length > 0) {
				for (String wordsFile : wordsFiles) {
					Set<String> urls = parseUrls(weiboBaseUrl, wordsFile);
					for (String url : urls) {
						saveBaseUrl(new TempUrl(weiboBaseUrl, url));
						fetch(client, dataSource, url);
						saveBaseUrl(null);
					}
				}
			} else {
				System.out.println("没有词文件指定文件，使用指定的url");
				saveBaseUrl(new TempUrl(weiboBaseUrl, weiboBaseUrl));
				fetch(client, dataSource, weiboBaseUrl);
				saveBaseUrl(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dataSource != null) {
					dataSource.close();
				}
				ThreadUtils.getRunnaleExecutor().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
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
	protected static Set<String> parseUrls(String weiboBaseUrl, String wordsFile) throws IOException {
		// 判断有没有指定文件，同时给定的网址中有没有变量，都有的话，进行循环
		Set<String> urls = new HashSet<String>();
		boolean hasVar = Pattern.compile("\\{\\d+\\}").matcher(weiboBaseUrl).find();
		if (hasVar) {
			File file = new File(wordsFile);
			boolean hasFile = file.isFile();
			if (!hasFile) {
				System.out.println("没有词文件指定文件，使用指定的url");
				urls.add(weiboBaseUrl);
				return urls;
			}
			// 读取一行，然后将该行删除！
			List<String> lines = FileUtils.readLines(file, "GBK");
			if (lines == null || lines.isEmpty()) {
				System.out.println("文件内容为空，使用指定的url");
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
				System.out.println("解析出需要抓取的url：" + url);
				urls.add(url);
			}
			return urls;
		} else {
			System.out.println("指定的url中没有变量信息，不使用文件中指定的词");
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
			System.out.println("指定的抓取url为空！不处理！");
			return;
		}
		System.out.println("正在抓取页面：" + tmpUrl);
		String html = SinaWeiboRequest.request(client, tmpUrl, handler, FailedNode.MAIN);
		// 评论数：评论详细信息列表，包括评论者id,评论者姓名，评论者评论时间，评论者评论内容，评论者个人资料
		// 写入csv文件
		if (StringUtils.isBlank(html)) {
			System.out.println("抓取的页面[" + tmpUrl + "]内容为空，不处理！");
			return;
		}
		// 将内容构建成一个HtmlParser可解析的对象
		try {
			Document doc = Jsoup.parse(html);
			// 将一条条的微博根据标签的特征统一取出来
			Elements eles = doc.getElementsByClass("MIB_feed_c");
			if (eles.size() > 0 || html.indexOf("</html><!-- 以上是企业微博的iframe -->") > -1) {
				// 如果这样的格式存在，则说明是那种HTML格式的，如：http://gov.weibo.com/profile.php?uid=sciencenet&ref=
				WeiboCompanyParser htmlParser = new WeiboCompanyParser(dataSource, handler);
				htmlParser.parse(tmpUrl);
			} else {
				// 否则就是那种js的json格式的内容方式，使用json的方式进行解析
				WeiboUserParser commonParser = new WeiboUserParser(dataSource, handler);
				WeiboSearchParser searchParser = new WeiboSearchParser(dataSource, handler);
				if (searchParser.isBelong(html)) {
					searchParser.parse(tmpUrl);
				} else if (commonParser.isBelong(html)) {
					commonParser.setCurUrl(weiboUrl);
					commonParser.parse(tmpUrl);
				} else {
					System.out.println("内容不符合预定的解析规则");
					return;
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
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
		if (weiboUrl.indexOf("?") == -1) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		// 如果URL中有分页信息，则直接添加到URL最后，否则默认为page=1
		if (StringUtils.isNotEmpty(pageInfo)) {
			sb.append(pageInfo);
		} else {
			sb.append("page=1");
		}
		return sb.substring(1);
	}
}