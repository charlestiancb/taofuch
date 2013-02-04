package com.scoop.crawler.weibo.runnable;

import com.scoop.crawler.weibo.parser.CommentParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

public class WeiboCommentRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected FailedHandler handler;

	public WeiboCommentRunnable(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		new CommentParser(dataSource, handler).fetchWeiboComments();
	}
}
