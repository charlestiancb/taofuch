package com.scoop.crawler.weibo.parser;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.repository.DataSource;

public abstract class JsonStyleParser extends Parser {
	public JsonStyleParser(DefaultHttpClient client, DataSource dataSource) {
		super(client, dataSource);
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
