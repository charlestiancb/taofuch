package com.scoop.crawler.weibo.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;

/**
 * json格式的页面解析器
 * 
 * @author taofucheng
 * 
 */
public abstract class JsonStyleParser extends WeiboParser {

	public JsonStyleParser(DataSource dataSource, RequestFailedHandler handler) {
		super(dataSource, handler);
	}

	/** 微博内容的JSON结束标志 */
	protected static final String contentEnd = ")</script>";

	/** 是否属于自己的处理范围 */
	public abstract boolean isBelong(String html);

	/**
	 * 解析微博的URL
	 * 
	 * @param node
	 * @return
	 */
	protected static String parseMsgUrlFromJSONStyle(Element node) {
		Elements eles = node.getElementsByAttributeValue("node-type", "feed_list_item_date");
		if (eles != null && eles.size() > 0) {
			String url = eles.get(0).attr("href");
			url = url.startsWith("/") ? url : "/" + url;
			return url.startsWith("/http://weibo.com") ? url.substring(1) : "http://weibo.com" + url;
		}
		return "";
	}
}
