package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.parser.httpclient.FansParserHttpclient;
import com.scoop.crawler.weibo.parser.httpclient.FollowParserHttpclient;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.Logger;
import com.scoop.crawler.weibo.util.ThreadUtils;

/**
 * 微博用户的关系信息，如粉丝、关注等。
 * 
 * @author taofucheng
 * 
 */
public class WeiboUserRelationRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected FailedHandler handler;

	public WeiboUserRelationRunnable(DataSource dataSource,
			FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		FansParserHttpclient fansp = new FansParserHttpclient(dataSource,
				handler);
		FollowParserHttpclient followP = new FollowParserHttpclient(dataSource,
				handler);
		Logger.log("开始解析所有用户粉丝与关注信息……");
		try {
			// 循环获取用户信息
			for (User u = dataSource.getOneUnfetchedUser(); u != null; u = dataSource
					.getOneUnfetchedUser()) {
				try {
					Thread.sleep(ThreadUtils.nextSleepInterval());
					DefaultHttpClient client = ThreadUtils.allocateHttpClient();
					fansp.fetchFans(u, null, client);
					followP.fetchFollows(u, null, client);
					Thread.sleep(ThreadUtils.nextSleepInterval());
				} catch (Exception e) {
					System.err.println("解析用户信息[" + u + "]出错！");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThreadUtils.freeThread();
		ThreadUtils.finishUserRelation();
		Logger.log("所有用户粉丝与关注信息解析完毕！");
	}
}
