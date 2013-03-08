package com.scoop.crawler.weibo.parser.httpclient;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.parser.FansParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 用户粉丝信息解析器。
 * 
 * @author taofucheng
 * 
 */
public class FansParserHttpclient extends FansParser {

	public FansParserHttpclient(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void fetchFans(User u, WebDriver driver, DefaultHttpClient client) {
		Logger.log("开始解析用户[" + u + "]的粉丝信息……");
		WeiboPersonInfo person = new WeiboPersonInfo(u.getUrl(), client);
		List<WeiboPersonInfo> fans = person.getFans();
		Logger.log("记录的用户[" + u + "]共有粉丝：" + u.getFansNum() + "个！实际解析出：" + fans.size() + "个");
		dataSource.saveFans(person.getId(), fans);
		afterSave(u, FailedNode.FANS, fans.size());
		Logger.log("用户[" + u + "]的粉丝信息解析完毕！");
	}
}
