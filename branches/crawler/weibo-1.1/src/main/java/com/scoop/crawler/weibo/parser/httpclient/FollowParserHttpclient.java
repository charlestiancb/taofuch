package com.scoop.crawler.weibo.parser.httpclient;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.parser.UserRelationParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 用户关注信息解析器
 * 
 * @author taofucheng
 * 
 */
public class FollowParserHttpclient extends UserRelationParser {

	public FollowParserHttpclient(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void fetchFollows(User u, WebDriver driver, DefaultHttpClient client) {
		Logger.log("开始解析用户[" + u + "]的关注信息……");
		WeiboPersonInfo person = new WeiboPersonInfo(u.getUrl(), client);
		List<WeiboPersonInfo> follows = person.getFollows();
		Logger.log("用户[" + u + "]共有关注：" + u.getFollowNum() + "个！实际解析出：" + follows.size() + "个");
		dataSource.saveFollows(person.getId(), follows);
		Logger.log("用户[" + u + "]的关注信息解析完毕！");
	}

}
