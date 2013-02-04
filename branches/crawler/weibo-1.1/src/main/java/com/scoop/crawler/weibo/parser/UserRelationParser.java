package com.scoop.crawler.weibo.parser;

import java.util.Iterator;

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
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;

public class UserRelationParser extends Parser {
	public UserRelationParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	protected Document parseToDoc(String html, String contentPart) {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + contentPart + "\",";
		String tmp = cut(html, detailStart);
		return StringUtils.isBlank(tmp) ? null : Jsoup.parse(tmp);
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
				saveRelations(client, driver, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void saveRelations(DefaultHttpClient client, WebDriver driver, FailedNode node) {
		String html = driver.getPageSource();
		Document doc = null;
		if (FailedNode.FANS.compareTo(node) == 0) {
			doc = parseToDoc(html, "pl_relation_hisFans");
		} else {
			doc = parseToDoc(html, "pl_relation_hisFollow");
		}
		Elements eles = doc.getElementsByAttributeValue("node-type", "userListBox");
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
			dataSource.savePerson(person);
		}
		// 获取下一页的消息
		WebElement ele = driver.findElement(By.className("W_pages_comment"));
		if (ele != null) {
			ele = ele.findElement(By.linkText("下一页"));
		}
		if (ele != null) {
			ele.click();
			try {
				Thread.sleep(1000);// 等待1s，让页面加载完毕！
			} catch (InterruptedException e) {
			}
			saveRelations(client, driver, node);
		}
	}
}
