package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.runnable.MysqlRunnable;
import com.scoop.crawler.weibo.runnable.WeiboCommentRunnable;
import com.scoop.crawler.weibo.util.ThreadUtils;

public abstract class Parser {
	protected DefaultHttpClient client;
	protected DataSource dataSource;
	private RequestFailedHandler handler;
	private static ThreadLocal<String> query = new InheritableThreadLocal<String>();

	/**
	 * 保存用户在URL中查询的具体词语！。
	 * 
	 * @param url
	 */
	public static void saveQuery(String url) {
		query.set(url);
	}

	/**
	 * 获取用户给定的当前URL。
	 * 
	 * @return
	 */
	public static String getQuery() {
		return query.get();
	}

	public RequestFailedHandler getHandler() {
		return handler;
	}

	public void setHandler(RequestFailedHandler handler) {
		this.handler = handler;
	}

	public Parser(DefaultHttpClient client, DataSource dataSource) {
		this.client = client;
		this.dataSource = dataSource;
	}

	/**
	 * 解析每一条微博的信息。
	 * 
	 * @param weiboUrl
	 *            微博的具体地址
	 * @param publishTime
	 *            微博的发布时间
	 * @throws IOException
	 */
	protected void parseWeibo(String weiboUrl, String publishTime, DefaultHttpClient client, DataSource dataSource) throws IOException {
		OneWeiboInfo weibo = new OneWeiboInfo(weiboUrl, client);
		weibo.setHandler(handler);
		if (!weibo.isValid()) {
			// 如果不正确，则说明该微博是假的，给出提示，然后路过。
			System.err.println("weiboUrl[" + weiboUrl + "] 对应的微博内容不正确！");
			return;
		}
		weibo.setPublishTime(publishTime);
		dataSource.saveWeibo(weibo);
		// 处理评论与转发的信息、以及评论者的个人信息。
		WeiboCommentRunnable run = new MysqlRunnable(dataSource, weibo);
		ThreadUtils.execute(run);
	}

	/**
	 * 解析每条微博的发布时间。
	 * 
	 * @param node
	 * @return
	 */
	protected static String parseMsgPublishTime(Element node) {
		Elements eles = node.getElementsByAttribute("date");
		if (eles != null && eles.size() > 0) {
			String date = eles.get(eles.size() - 1).attr("title");
			if (StringUtils.isBlank(date)) {
				date = eles.get(eles.size() - 1).attr("date");
			}
			return StringUtils.trim(date);
		}
		return "";
	}
}
