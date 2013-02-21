package com.scoop.crawler.weibo.parser;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.Fans;
import com.scoop.crawler.weibo.repository.mysql.Follow;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.Logger;

public class UserRelationParser extends Parser {
	public UserRelationParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	protected void fetch(User u, WebDriver driver, DefaultHttpClient client, FailedNode node) {
		if (driver == null) {
			return;
		}
		try {
			if (StringUtils.isNotBlank(u.getUserId())) {
				String url = "http://weibo.com/" + u.getUserId() + "/";
				if (FailedNode.FANS.compareTo(node) == 0) {
					url += "fans";
				} else {
					url += "follow";
				}
				driver.navigate().to(url);
				try {
					Thread.sleep(2000);// 等待1s，让页面加载完毕！
				} catch (InterruptedException e) {
				}
				saveRelations(u, client, driver, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void saveRelations(User u, DefaultHttpClient client, WebDriver driver, FailedNode node) {
		// 这里使用循环而不使用递归，是因为递归有次数限制！
		while (true) {
			String html = driver.getPageSource();
			Document doc = Jsoup.parse(html);
			Element relations = null;
			if (FailedNode.FANS.compareTo(node) == 0) {
				relations = doc.getElementById("pl_relation_hisFans");
			} else {
				relations = doc.getElementById("pl_relation_hisFollow");
			}
			if (relations == null) {
				return;
			}
			Elements eles = relations.getElementsByAttributeValue("node-type", "userListBox");
			if (eles.isEmpty()) {
				return;
			}
			eles = eles.first().getElementsByTag("li");
			if (eles.isEmpty()) {
				return;
			}
			Iterator<Element> es = eles.iterator();
			while (es.hasNext()) {
				String userId = es.next().attr("action-data");
				userId = userId.substring("uid=".length(), userId.indexOf("&"));
				WeiboPersonInfo person = new WeiboPersonInfo("http://weibo.com/u/" + userId, client);
				person.setId(userId);
				person.setHandler(getHandler());
				person.setNeedFetchRelation(false);
				dataSource.savePerson(person);// 保存粉丝或关注信息
				if (FailedNode.FANS.compareTo(node) == 0) {
					Fans fans = new Fans();
					fans.setUserId(u.getUserId());
					fans.setFansId(userId);
					dataSource.saveFans(fans);// 保存用户与粉丝的关系信息
				} else {
					Follow follow = new Follow();
					follow.setUserId(u.getUserId());
					follow.setFollowId(userId);
					dataSource.saveFollows(follow);// 保存用户与关注的关系信息
				}
				Logger.log("保存用户信息[" + person.getId() + ":" + person.getName() + "]成功！");
			}
			// 获取下一页的消息
			WebElement ele = null;
			try {
				List<WebElement> pages = driver.findElements(By.className("W_pages_comment"));
				if (pages != null && pages.size() > 0) {
					ele = pages.get(0).findElement(By.linkText("下一页"));
				} else {
					break;
				}
			} catch (Throwable e) {
			}
			if (ele != null && ele.isEnabled()) {
				ele.click();
				try {
					Thread.sleep(1000);// 等待1s，让页面加载完毕！
				} catch (InterruptedException e) {
				}
			} else {
				break;
			}
		}
	}
}
