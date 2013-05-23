package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.Query;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

public abstract class WeiboParser extends Parser {
	private int curPage = 1;
	private static ThreadLocal<Query> query = new InheritableThreadLocal<Query>();

	public WeiboParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	/**
	 * 保存用户查询的具体词语！。
	 * 
	 * @param queryStr
	 *            查询内容
	 * @param colName
	 *            查询内容所在的集合名称
	 */
	public static void saveQuery(String queryStr, String colName) {
		query.set(new Query(queryStr, colName));
	}

	/**
	 * 清除查询内容
	 */
	public static void clearQuery() {
		query.set(null);
	}

	/**
	 * 获取用户给定的查询词语。
	 * 
	 * @return
	 */
	public static Query getQuery() {
		return query.get();
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
		weibo.setHandler(getHandler());
		if (!weibo.isValid()) {
			// 如果不正确，则说明该微博是假的，给出提示，然后路过。
			System.err.println("weiboUrl[" + weiboUrl + "] 对应的微博内容不正确！");
			return;
		}
		weibo.setPublishTime(publishTime);
		dataSource.saveWeibo(weibo);
		// // 处理评论与转发的信息、以及评论者的个人信息。
		// WeiboCommentRunnable run = new WeiboCommentRunnable(dataSource,
		// weibo.getHandler());
		// ThreadUtils.executeCommnet(run);
		// // 重启线程专门存储用户关系
		// WeiboUserRelationRunnable userRun = new
		// WeiboUserRelationRunnable(dataSource, weibo.getHandler());
		// ThreadUtils.executeUserRelation(userRun);
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

	/**
	 * 到页面的最下方，使页面中的微博自动加载！
	 * 
	 * @param driver
	 */
	protected void goEnd(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(1,10000)");
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
}
