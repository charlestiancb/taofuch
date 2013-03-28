package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.ExploreRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 普通用户的微博，如：http://weibo.com/u/2675686781，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class UserWeiboParser extends JsonStyleParser {
	/**
	 * 普通用户的微博，如：http://weibo.com/u/2675686781，这种页面的微博内容是以JSON格式组织的。
	 * 
	 * @param dataSource
	 * @param handler
	 */
	public UserWeiboParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	private String curUrl;

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}

	private String contentStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"pl_content_hisFeed\",";

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(contentStart) > -1;
	}

	public void parse(String url) throws IOException {
		url = StringUtils.trim(url);
		if (StringUtils.isEmpty(url)) {
			return;
		}
		WebDriver driver = null;
		try {
			driver = ExploreRequest.getDriver(url);
			if (driver == null) {
				Logger.log("浏览器打开失败！停止运行！");
				System.exit(0);
			}
			parseHtmlToWeibo(driver);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}

	private void parseHtmlToWeibo(WebDriver driver) throws IOException {
		Logger.log("解析用户主页的微博");
		// 先将所有的页面加载完毕！至少点击三次下拉，这样使所有微博加载完毕！
		try {
			for (int i = 0; i < 3; i++) {
				goEnd(driver);
				Thread.sleep(2000);// 等待两秒，让其加载完毕！
			}
		} catch (Throwable e) {
		}
		String html = ExploreRequest.getPageHtml(driver);
		Document doc = Jsoup.parse(html);
		Element weibos = doc.getElementById("pl_content_hisFeed");
		if (weibos == null) {
			return;
		}
		// 解析出用户编号！
		String uid = "$CONFIG['oid'] = '";
		uid = html.substring(html.indexOf(uid)) + uid.length();
		uid = uid.substring(0, uid.indexOf("';"));
		uid = uid.substring(uid.lastIndexOf("'") + 1);
		// 将map中的html内容拿出来！
		Elements eles = weibos.getElementsByAttributeValue("action-type", "feed_list_item");
		if (eles != null && eles.size() > 0) {
			// 如果这样的格式存在，则说明是那种HTML格式的
			for (int i = 0; i < eles.size(); i++) {
				Logger.log("解析用户主页上的每一条微博");
				// 一条条的微博进行处理，解析每条微博的信息
				parseWeibo(StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
						StringUtils.trim(parseMsgPublishTime(eles.get(i))), getClient(), dataSource);
			}
			// 加载下一屏的内容
			WebElement ele = null;
			try {
				ele = driver.findElement(By.cssSelector("div.W_pages > a.W_btn_c > span"));
			} catch (Exception e) {
			}
			if (ele != null && ele.isEnabled()) {
				ele.click();
				try {
					Thread.sleep(1000);// 等数据加载完成！
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				parseHtmlToWeibo(driver);
			}
		}
	}

	/**
	 * 用于对指定URL的重试！
	 * 
	 * @param client
	 * @param url
	 * @param handler
	 * @param node
	 */
	public void reTry(DefaultHttpClient client, String url, FailedNode node) {
		int idx = url.lastIndexOf("&uid=");
		if (idx == -1) {
			return;
		}
		setClient(client);
		try {
			parse(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
