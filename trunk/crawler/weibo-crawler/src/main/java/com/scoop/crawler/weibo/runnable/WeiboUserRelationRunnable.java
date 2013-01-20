package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

public class WeiboUserRelationRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected DefaultHttpClient client;
	protected FailedHandler handler;

	public WeiboUserRelationRunnable(DataSource dataSource, DefaultHttpClient client, FailedHandler handler) {
		this.dataSource = dataSource;
		this.client = client;
		this.handler = handler;
	}

	public void run() {
		// 循环获取用户信息
		for (User u = dataSource.getOneUnfetchedUser(); u != null; u = dataSource.getOneUnfetchedUser()) {

		}
	}
}
