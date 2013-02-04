package com.scoop.crawler.weibo.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.ExploreRequest;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.JSONUtils;

/**
 * 普通方式的微博，如：http://weibo.com/u/2675686781，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class WeiboCommonParser extends JsonStyleParser {
	/**
	 * 普通方式的微博，如：http://weibo.com/u/2675686781，这种页面的微博内容是以JSON格式组织的。
	 * 
	 * @param dataSource
	 * @param handler
	 */
	public WeiboCommonParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	private String curUrl;

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}

	private String contentStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"pl_content_hisFeed\",";

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(contentStart) > -1;
	}

	public void parse(String url) throws IOException {
		url = StringUtils.trim(url);
		if (StringUtils.isEmpty(url)) {
			return;
		}
		WebDriver driver = null;
		try {
			driver = ExploreRequest.getDriver(url);
			parseHtmlToWeibo(driver);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}

	private void parseHtmlToWeibo(WebDriver driver) throws IOException {
		String html = driver.getPageSource();
		int idx = html.indexOf(contentStart);
		if (idx == -1) {
			return;
		}
		System.out.println("解析用户主页的微博");
		String weiboList = html.substring(idx + contentStart.length());
		weiboList = weiboList.substring(0, weiboList.indexOf(contentEnd));
		weiboList = "{" + weiboList;// 补齐为JSON格式
		// 解析出用户编号！
		String uid = "$CONFIG['oid'] = '";
		uid = html.substring(html.indexOf(uid)) + uid.length();
		uid = uid.substring(0, uid.indexOf("';"));
		uid = uid.substring(uid.lastIndexOf("'") + 1);
		// 将map中的html内容拿出来！
		weiboList = JSONUtils.getSinaHtml(weiboList);
		Document doc = Jsoup.parse(weiboList);
		Elements eles = doc.getElementsByAttributeValue("action-type", "feed_list_item");
		if (eles != null && eles.size() > 0) {
			// 如果这样的格式存在，则说明是那种HTML格式的
			for (int i = 0; i < eles.size(); i++) {
				System.out.println("解析用户主页上的每一条微博");
				// 一条条的微博进行处理，解析每条微博的信息
				parseWeibo(	StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
							StringUtils.trim(parseMsgPublishTime(eles.get(i))),
							getClient(),
							dataSource);
			}
			// TODO 加载下一屏的内容

		}
	}

	/**
	 * 解析给定的元素中的mid属性值
	 * 
	 * @param e
	 * @return
	 */
	private String parseId(Element e) {
		try {
			return e.attr("mid");
		} catch (Exception e1) {
			return null;
		}
	}

	/**
	 * 用于对指定URL的重试！
	 * 
	 * @param client
	 * @param url
	 * @param handler
	 * @param node
	 */
	public void reTry(DefaultHttpClient client, String url, FailedHandler handler, FailedNode node) {
		int idx = url.lastIndexOf("&uid=");
		if (idx == -1) {
			return;
		}
		String uid = StringUtils.trimToEmpty(url.substring(idx + "&uid=".length()));
		String json = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.USER_WEIBO);
		String _html = (String) JSON.parseObject(json, HashMap.class).get("data");
		Elements eles = Jsoup.parse(_html).getElementsByAttributeValue("action-type", "feed_list_item");
		while (eles != null && eles.size() > 0) {
			List<String> ids = new ArrayList<String>();
			// 如果这样的格式存在，则说明是那种HTML格式的
			for (int i = 0; i < eles.size(); i++) {
				System.out.println("解析用户主页上的每一条微博");
				String tmp = parseId(eles.get(i));
				if (StringUtils.isNotBlank(tmp)) {
					ids.add(tmp);
				}
				// 一条条的微博进行处理，解析每条微博的信息
				try {
					parseWeibo(	StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
								StringUtils.trim(parseMsgPublishTime(eles.get(i))),
								client,
								dataSource);
				} catch (IOException e) {
				}
			}
			// 加载下一屏的内容，并进行处理
			eles = loadNextPage(uid, ids);
		}
	}
}
