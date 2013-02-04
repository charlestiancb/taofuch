package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.parser.CommentParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class WeiboCommentRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected FailedHandler handler;

	public WeiboCommentRunnable(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		CommentParser cp = new CommentParser(dataSource, handler);
		for (Weibo w = dataSource.getOneUnfetchedWeibo(); w != null; w = dataSource.getOneUnfetchedWeibo()) {
			cp.fetchWeiboComments(w, client);
		}
		// 将线程释放
		ThreadUtils.freeThread();
		ThreadUtils.finishComment();
	}
}
