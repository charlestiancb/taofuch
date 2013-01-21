package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
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

	public WeiboUserRelationRunnable(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		// 循环获取用户信息
		for (User u = dataSource.getOneUnfetchedUser(); u != null; u = dataSource.getOneUnfetchedUser()) {
			try {
				WeiboPersonInfo person = new WeiboPersonInfo(u.getUrl(), client);
				dataSource.saveFans(person.getId(), person.getFans());
				dataSource.saveFollows(person.getId(), person.getFollows());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ThreadUtils.freeThread();
				ThreadUtils.finishUserRelation();
			}
		}
	}
}
