package com.scoop.crawler.weibo.parser;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 用户关注信息解析器
 * 
 * @author taofucheng
 * 
 */
public class FollowParser extends UserRelationParser {

	public FollowParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void fetchFollows(User u, WebDriver driver, DefaultHttpClient client) {
		Logger.log("开始解析用户[" + u + "]的关注信息……");
		fetch(u, driver, client, FailedNode.FOLLOWS);
		Logger.log("用户[" + u + "]的关注信息解析完毕！");
	}

}
