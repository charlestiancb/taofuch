package com.scoop.crawler.weibo;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.JdbcDataSource;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.runnable.WeiboCommentRunnable;
import com.scoop.crawler.weibo.runnable.WeiboUserRelationRunnable;

/**
 * 抓取评论与用户的入口
 * 
 * @author taofucheng
 * 
 */
public class CommentAndUserMain {
	protected static DefaultHttpClient client = null;
	protected static DataSource dataSource = null;
	protected static RequestFailedHandler handler;

	public static void main(String[] args) {

		client = SinaWeiboRequest.getHttpClient("sszcgfss@gmail.com", "jmi2009095");
		if (dataSource == null) {
			dataSource = new JdbcDataSource();
		}
		if (handler == null) {
			handler = new RequestFailedHandler(client, dataSource);
		}
		handler.reTry();
		// 微博抓取完毕之后同时抓取评论与用户信息。
		// 处理评论与转发的信息、以及评论者的个人信息。
		WeiboCommentRunnable run = new WeiboCommentRunnable(dataSource, handler);
		run.run();
		// ThreadUtils.executeCommnet(run);
		// 重启线程专门存储用户关系
		WeiboUserRelationRunnable userRun = new WeiboUserRelationRunnable(dataSource, handler);
		userRun.run();
		handler.reTry();
		// ThreadUtils.executeUserRelation(userRun);
	}
}
