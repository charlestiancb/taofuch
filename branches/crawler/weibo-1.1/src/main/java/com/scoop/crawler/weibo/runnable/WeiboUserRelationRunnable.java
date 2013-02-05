package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.parser.FansParser;
import com.scoop.crawler.weibo.parser.FollowParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.ExploreRequest;
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
		FansParser fansp = new FansParser(dataSource, handler);
		FollowParser followP = new FollowParser(dataSource, handler);
		try {
			WebDriver driver = ExploreRequest.getDriver("http://weibo.com/");
			// 循环获取用户信息
			for (User u = dataSource.getOneUnfetchedUser(); u != null; u = dataSource.getOneUnfetchedUser()) {
				try {
					fansp.fetchFans(u, driver, client);
					followP.fetchFollows(u, driver, client);
				} catch (Exception e) {
					System.err.println("解析用户信息[" + u + "]出错！");
					e.printStackTrace();
				}
			}
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThreadUtils.freeThread();
		ThreadUtils.finishUserRelation();
	}
}
