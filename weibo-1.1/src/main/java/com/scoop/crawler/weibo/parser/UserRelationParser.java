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
import com.scoop.crawler.weibo.util.JSONUtils;
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
					Thread.sleep(200);// 等待0.2s，让链接完毕！
				} catch (InterruptedException e) {
				}
				String currentUrl = driver.getCurrentUrl();
				// 判断是否是pageNotFound，如果是，则使用页面点击方式进入！
				if (currentUrl.startsWith("http://weibo.com/sorry")) {
					Logger.log("链接[" + url + "]无法直接访问[" + currentUrl + "]，启用浏览器访问！");
					driver.navigate().to(url.substring(0, url.lastIndexOf("/")));
					try {
						Thread.sleep(2000);// 等待1s，让页面加载完毕！
					} catch (InterruptedException e) {
					}
					List<WebElement> elements = driver.findElements(By.className("user_atten_l"));
					WebElement element = null;
					if (elements != null && elements.size() > 0) {
						elements = elements.get(0).findElements(By.tagName("li"));
						if (elements != null && elements.size() > 0) {
							if (FailedNode.FANS.compareTo(node) == 0) {
								element = elements.get(1).findElement(By.className("S_func1"));
							} else {
								element = elements.get(0).findElement(By.className("S_func1"));
							}
						}
					}
					if (element != null) {
						element.click();
						try {
							Thread.sleep(2000);// 等待1s，让页面加载完毕！
						} catch (InterruptedException e) {
						}
					}
				} else {
					try {
						Thread.sleep(1800);// 等待1s，让页面加载完毕！
					} catch (InterruptedException e) {
					}
				}
				saveRelations(u, client, driver, node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void saveRelations(User u, DefaultHttpClient client, WebDriver driver, FailedNode node) {
		if (node == null) {
			node = FailedNode.FANS;
		}
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
			if (relations == null || StringUtils.isBlank(relations.text())) {
				Logger.log("对当前用户[" + u.getUserId() + ":" + u.getName() + "]进行json方式解析。");
				html = JSONUtils.unEscapeHtml(html);
				if (FailedNode.FANS.compareTo(node) == 0) {
					doc = parseToDoc(html, "pl_relation_hisFans");
				} else {
					doc = parseToDoc(html, "pl_relation_hisFollow");
				}
				if (doc != null) {
					relations = doc.append("");
				}
			}
			if (relations == null) {
				Logger.log("当前用户[" + u.getUserId() + ":" + u.getName() + "]没有" + node.name() + "信息");
				return;
			}
			Elements eles = relations.getElementsByAttributeValue("node-type", "userListBox");
			if (eles.isEmpty()) {
				Logger.log("当前用户[" + u.getUserId() + ":" + u.getName() + "]没有" + node.name() + "信息");
				return;
			}
			eles = eles.first().getElementsByTag("li");
			if (eles.isEmpty()) {
				Logger.log("当前用户[" + u.getUserId() + ":" + u.getName() + "]没有" + node.name() + "信息");
				return;
			}
			Iterator<Element> es = eles.iterator();
			while (es.hasNext()) {
				String userId = es.next().attr("action-data");
				userId = userId.substring("uid=".length(), userId.indexOf("&"));
				WeiboPersonInfo person = new WeiboPersonInfo("http://weibo.com/" + userId + "/info", client);
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
					return;
				}
			} catch (Throwable e) {
			}
			if (ele != null && ele.isEnabled()) {
				try {
					Logger.log("当前URL[" + driver.getCurrentUrl() + "]，进入当前用户[" + u.getUserId() + ":" + u.getName()
							+ "]的" + node.name() + "下一页……");
					ele.click();
					Thread.sleep(1000);// 等待1s，让页面加载完毕！
				} catch (Exception e) {
					return;
				}
			} else {
				return;
			}
		}
	}

	private Document parseToDoc(String html, String contentPart) {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + contentPart + "\",";
		String tmp = cut(html, detailStart);
		return StringUtils.isBlank(tmp) ? null : Jsoup.parse(tmp);
	}
}
