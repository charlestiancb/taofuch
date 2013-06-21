package com.scoop.crawler.weibo.runnable;

import com.scoop.crawler.weibo.parser.httpclient.CommentParserHttpclient;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.Logger;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class WeiboCommentRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected FailedHandler handler;

	public WeiboCommentRunnable(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		CommentParserHttpclient cp = new CommentParserHttpclient(dataSource, handler);
		Logger.log("开始解析所有评论信息……");
		try {
			// 整个线程使用一个webDriver，即一个浏览器界面。防止过多的登录被新浪封闭
			for (Weibo w = dataSource.getOneUnfetchedWeibo(); w != null; w = dataSource.getOneUnfetchedWeibo()) {
				try {
					cp.fetchWeiboComments(null, w, ThreadUtils.allocateHttpClient());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 将线程释放
		ThreadUtils.freeThread();
		ThreadUtils.finishComment();
		Logger.log("所有评论信息解析完毕！");
	}
}
