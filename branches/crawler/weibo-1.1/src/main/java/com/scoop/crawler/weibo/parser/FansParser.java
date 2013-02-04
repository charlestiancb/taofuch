package com.scoop.crawler.weibo.parser;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;

/**
 * 用户粉丝信息解析器。
 * 
 * @author taofucheng
 * 
 */
public class FansParser extends UserRelationParser {

	public FansParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void fetchFans(User u, WebDriver driver, DefaultHttpClient client) {
		fetch(u, driver, client, FailedNode.FANS);
	}
}
