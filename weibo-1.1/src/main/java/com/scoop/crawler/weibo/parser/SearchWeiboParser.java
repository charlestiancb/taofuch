package com.scoop.crawler.weibo.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.scoop.crawler.weibo.fetch.FetchSina;
import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.ExploreRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.RegUtils;

/**
 * 微博搜索结果方式的微博，如：http://s.weibo.com/weibo/%25E7%25AF%25AE%25E7%2590%2583?topnav
 * =1&wvr=4&k=1，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class SearchWeiboParser extends JsonStyleParser {
	private String replace = "#REPLACE#";
	private String common = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + replace + "\",";
	private String weiboStart = common.replaceAll(replace, "pl_weibo_feedlist");
	private String userStart = common.replaceAll(replace, "pl_user_feedlist");

	public SearchWeiboParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(weiboStart) > -1 || html.indexOf(userStart) > -1;
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
				System.out.println("浏览器打开失败！停止运行！");
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

	private void parseHtmlToWeibo(WebDriver driver) {
		String html = driver.getPageSource();
		Document doc = Jsoup.parse(html);
		// 判断是否没有查询结果！
		Elements noresult = doc.getElementsByAttributeValue("class", "search_noresult");
		if (noresult != null && noresult.size() > 0) {
			return;
		}

		Element weibo = doc.getElementById("pl_weibo_feedlist");
		Element user = doc.getElementById("pl_user_feedlist");

		if (weibo != null) {
			parseWeibo(weibo);
		} else if (user != null) {
			parseUser(user);
		}
		// 解析下一页
		WebElement ele = null;
		try {
			ele = driver.findElement(By.className("search_page_M"));
			if (ele != null) {
				ele = ele.findElement(By.linkText("下一页"));
			}
		} catch (Throwable e) {
		}
		if (ele != null && ele.isEnabled()) {
			ele.click();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			parseHtmlToWeibo(driver);
		}
	}

	/**
	 * 解析微博
	 * 
	 * @param targetContentList
	 * @throws ParserException
	 */
	private void parseWeibo(Element doc) {
		Elements eles = doc.getElementsByClass("feed_list");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				saveQuery(RegUtils.parseToQuery(FetchSina.getBaseUrl()));
				try {
					// 一条条的微博进行处理，解析每条微博的信息
					parseWeibo(	StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
								StringUtils.trim(parseMsgPublishTime(eles.get(i))),
								getClient(),
								dataSource);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解析查找用户的搜索结果
	 * 
	 * @param targetContentList
	 * @throws ParserException
	 */
	private void parseUser(Element doc) {
		Elements eles = doc.getElementsByClass("person_pic");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				try {
					String userUrl = eles.get(0).child(0).attr("href");
					// 解析用户信息。
					FetchSinaWeibo.fetch(getClient(), dataSource, userUrl, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据指定的文件中每行指定的词进行搜索！
	 * 
	 * @param wordsFiles
	 */
	@SuppressWarnings("resource")
	public void parse(String[] wordsFiles) {
		// 循环读取每行中的词，如果词不为空，则读取并进行查询！
		if (wordsFiles != null && wordsFiles.length > 0) {
			WebDriver driver = ExploreRequest.getDriver("http://s.weibo.com/weibo/AI");
			if (driver == null) {
				System.out.println("打开浏览器失败！停止工作！");
			}
			for (String file : wordsFiles) {

				file = StringUtils.trim(file);
				if (StringUtils.isEmpty(file)) {
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
					for (String word = br.readLine(); word != null; word = br.readLine()) {
						if (StringUtils.isBlank(word)) {
							continue;
						} else {
							// 输入到输入框中，然后点击查询，并开始解析！
							driver.findElements(By.className("searchInp_form")).get(0).clear();
							driver.findElements(By.className("searchInp_form")).get(0).sendKeys(word);
							driver.findElements(By.className("searchBtn")).get(0).click();
							Thread.sleep(2000);
							parseHtmlToWeibo(driver);
						}
					}
				} catch (Throwable t) {
					// t.printStackTrace();
				}
			}
		}
	}
}
