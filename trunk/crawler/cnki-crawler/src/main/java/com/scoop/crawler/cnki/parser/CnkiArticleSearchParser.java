package com.scoop.crawler.cnki.parser;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.scoop.crawler.cnki.entity.SearchRecord;
import com.scoop.crawler.cnki.export.CsvExport;
import com.scoop.crawler.cnki.export.Export;

/**
 * CNKI搜索入口程序
 * 
 * @author taofucheng
 * 
 */
public class CnkiArticleSearchParser {
	public static final String ENC = "UTF-8";
	private static WebDriver driver = null;
	private static File exportDir = new File("D:/cnki");// 结果文件存放的目录，注意：每次运行会清空上次运行结果！
	private static Export export;// 输出类

	public static void main(String[] args) {
		fetchFirstPageUrl("g250", 2011, 2011);
	}

	/**
	 * 获取查询的第一页数据
	 * 
	 * @param refContent
	 *            引用文献的内容
	 * @param fromYear
	 *            从哪一年开始
	 * @param endYear
	 *            到哪一年结束
	 */
	public static void fetchFirstPageUrl(String refContent, int fromYear,
			int endYear) {
		openResultPage(refContent, fromYear, endYear);
		export = new CsvExport(exportDir, "GBK");
		fetchSearchList(refContent, fromYear, endYear);
		export.close();
		driver.quit();
	}

	private static void openResultPage(String refContent, int fromYear,
			int endYear) {
		String searchDataUrl = "http://epub.cnki.net/KNS/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.21&PageName=ASP.brief_result_aspx&DbPrefix=CRLD&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%bc%95%e6%96%87%e6%95%b0%e6%8d%ae%e5%ba%93&ConfigFile=CRLD.xml&db_opt=%E4%B8%AD%E5%9B%BD%E5%BC%95%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93&db_value=%E4%B8%AD%E5%9B%BD%E5%BC%95%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93&base_special1=%25&magazine_special1=%25&year_from="
				+ fromYear
				+ "&year_to="
				+ endYear
				+ "&yearB_type=echar&txt_4_sel=CLC&txt_4_value1="
				+ refContent
				+ "&txt_4_logical=and&txt_4_relation=%23CNKI_AND&txt_4_special1=%3D&his=0&__=Sat%20Oct%2026%202013%2022%3A40%3A56%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
		driver = new FirefoxDriver();
		driver.get(searchDataUrl);
		String dataListurl = "http://epub.cnki.net/kns/brief/brief.aspx?pagename="
				+ Jsoup.parse(driver.getPageSource()).text()
				+ "&t="
				+ new Date().getTime() + "&keyValue=&S=1";
		driver.get(dataListurl);
	}

	public static void fetchSearchList(String refContent, int fromYear,
			int endYear) {
		List<WebElement> trs = null;
		try {
			WebElement table = driver.findElement(By
					.cssSelector(".GridTableContent"));
			if (table == null) {
				return;
			}
			trs = table.findElements(By.tagName("tr"));
		} catch (Exception e) {
			driver.navigate().refresh();
			String html = driver.getPageSource();
			if (html.startsWith("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head><body><p><label>验证码：</label><input type=\"text\" name=\"CheckCode\" id=\"CheckCode\" /><input type=\"button\" onclick=\"javascript:CheckCodeSubmit()\" value=\"提交\" /></p><p>")) {
				System.err.println("需要输入验证码！请输入并提交！");
				try {
					Thread.sleep(30000L);// 等待用户30秒钟
				} catch (InterruptedException e1) {
				}
				fetchSearchList(refContent, fromYear, endYear);
			} else {
				throw new RuntimeException("页面有问题，页面内容为：\n"
						+ driver.getPageSource());
			}
		}
		if (trs != null && trs.size() > 1) {
			for (int i = 1; i < trs.size(); i++) {// 从第二开始，因为第一行是标题
				List<WebElement> tds = trs.get(i)
						.findElements(By.tagName("td"));
				if (tds != null && tds.size() > 5) {
					SearchRecord line = new SearchRecord();
					line.setIndex(tds.get(0).getText());
					line.setTitle(tds.get(1).getText());// 这是标题行
					line.setAuthor(tds.get(2).getText());// 这是被引作者
					line.setOrigin(tds.get(3).getText());// 这是被引文献来源
					line.setPublishTime(tds.get(4).getText());// 这是发表时间
					line.setCount(tds.get(5).getText());// 这是被引次数
					WebElement link = null;
					try {
						link = tds.get(5).findElement(By.tagName("a"));
					} catch (Exception e) {
					}
					if (link != null) {
						String url = link.getAttribute("href");
						line.setRefs(CnkiReferenceParser.parseReference(url));
					}
					// 直接写到文件中
					export.write(line);
				}
			}
		}
		// 获取下一页链接
		try {
			gotoNextPage(refContent, fromYear, endYear);
		} catch (UnhandledAlertException e) {
			driver.switchTo().alert().accept();// 弹出框点击下一页
			String targetUrl = driver.getCurrentUrl();
			driver.quit();
			openResultPage(refContent, fromYear, endYear);
			driver.get(targetUrl);
			gotoNextPage(refContent, fromYear, endYear);
		} catch (Exception e) {
			System.out.println("没有下一页了，已经解析完成！");
			e.printStackTrace();
		}
	}

	private static void gotoNextPage(String refContent, int fromYear,
			int endYear) {
		WebElement pages = driver
				.findElement(By.cssSelector(".pageBar_bottom"));
		WebElement nextPage = pages.findElement(By.linkText("下一页"));
		nextPage.click();
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
		}
		fetchSearchList(refContent, fromYear, endYear);
	}

}
