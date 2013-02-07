package com.scoop.crawler.weibo.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

/**
 * 解析类似这样：http://gov.weibo.com/profile.php?uid=sciencenet&ref=链接形式中的微博内容。<br>
 * 这个内容的组织方式是HTML的，如：加V或官方网站方式的微博
 * 
 * @author taofucheng
 * 
 */
public class CompanyWeiboParser extends WeiboParser {

	public CompanyWeiboParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void parse(Elements eles) throws IOException {
		for (int i = 0; i < eles.size(); i++) {
			// 一条条的微博进行处理，解析每条微博的信息
			System.out.println("解析当前微博第" + getCurPage() + "页，第" + (i + 1) + "条微博！");
			parseWeibo(	StringUtils.trim(parseMsgUrlFromHtmlStyle(eles.get(i))),
						StringUtils.trim(parseMsgPublishTime(eles.get(i))),
						getClient(),
						dataSource);
			System.out.println("当前微博第" + getCurPage() + "页，第" + (i + 1) + "条微博解析完毕！");
		}
	}

	/**
	 * 解析Html形式的微博中的消息URL
	 * 
	 * @param node
	 * @return
	 */
	private static String parseMsgUrlFromHtmlStyle(Element ele) {
		// 取出所有strong标签，因为链接就在发布时间的那个strong标签上面。
		Elements eles = ele.getElementsByTag("strong");
		if (eles == null || eles.size() == 0) {
			return "";
		} else {
			for (int i = 0; i < eles.size(); i++) {
				String tmp = eles.get(i).attr("date");
				if (StringUtils.isNotBlank(tmp)) {
					return eles.get(i).parent().attr("href");
				}
			}
		}
		return "";
	}

	/**
	 * 解析指定页面及其下一页。
	 * 
	 * @param tmpUrl
	 */
	public void parse(String tmpUrl) {
		WebDriver driver = null;
		try {
			driver = ExploreRequest.getDriver(tmpUrl);
			if (driver == null) {
				System.out.println("页面启动失败，中止抓取！");
				System.exit(0);
				return;
			}
			String html = driver.getPageSource();
			setCurPage(1);
			while (StringUtils.isNotBlank(html)) {
				goEnd(driver);
				Document doc = Jsoup.parse(html);
				// 将一条条的微博根据标签的特征统一取出来
				Elements eles = doc.getElementsByClass("MIB_feed_c");
				if (eles.size() > 0) {
					parse(eles);
				}
				// 如果有下一页，则点击下一页
				List<WebElement> webeles = driver.findElements(By.className("btn_numWidth"));
				if (webeles != null && webeles.size() > 0) {
					webeles.get(webeles.size() - 1).click();
					Thread.sleep(2 * 1000);// 等待5秒，等页面加载完毕！
					html = driver.getPageSource();
					setCurPage(getCurPage() + 1);
				} else {
					html = null;
					System.out.println("没有下一页了，抓取完毕！" + driver.getPageSource());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}
}
