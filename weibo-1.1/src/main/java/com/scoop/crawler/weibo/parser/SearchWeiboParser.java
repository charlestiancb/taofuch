package com.scoop.crawler.weibo.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.scoop.crawler.weibo.entity.Query;
import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.ExploreRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.Logger;
import com.scoop.crawler.weibo.util.StreamUtils;

/**
 * 微博搜索结果方式的微博，如：http://s.weibo.com/weibo/%25E7%25AF%25AE%25E7%2590%2583?topnav
 * =1&wvr=4&k=1，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class SearchWeiboParser extends JsonStyleParser {
	private String replace = "#REPLACE#";
	private String common = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\""
			+ replace + "\",";
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

	private void parseHtmlToWeibo(WebDriver driver) {
		StringBuffer html = new StringBuffer();
		driver = ExploreRequest.getPageHtml(driver, html);
		Document doc = Jsoup.parse(html.toString());
		// 判断是否没有查询结果！
		Elements noresult = doc.getElementsByAttributeValue("class",
				"search_noresult");
		if (noresult != null && noresult.size() > 0) {
			Logger.log("查询词[" + getQuery() + "]没有查询结果！");
			saveNoResultWord();
			return;
		}
		noresult = doc.getElementsByAttributeValue("class", "W_inputStp");
		while (noresult != null && noresult.size() > 0) {
			System.out.println("要输入验证码，请输入！");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			html = new StringBuffer();
			driver = ExploreRequest.getPageHtml(driver, html);
			doc = Jsoup.parse(html.toString());
			noresult = doc.getElementsByAttributeValue("class", "W_inputStp");
		}
		Element weibo = doc.getElementById("pl_weibo_feedlist");
		Element user = doc.getElementById("pl_user_feedlist");

		if ((weibo == null || StringUtils.isEmpty(weibo.text()))
				&& (user == null || StringUtils.isEmpty(user.text()))) {
			// 如果浏览器方式没有获取到内容，则使用json解析源码的方式进行解析。
			weibo = null;
			user = null;
			html = new StringBuffer();
			driver = ExploreRequest.getPageHtml(driver, html);
			// 将其中的html转义字符转换成正常的字符！
			html = new StringBuffer(JSONUtils.unEscapeHtml(html.toString()));
			String hit = weiboStart;
			int idx = html.indexOf(weiboStart);
			if (idx == -1) {
				idx = html.indexOf(userStart);
				hit = userStart;
			}
			if (idx == -1) {
				Logger.log("查询词[" + getQuery() + "]没有查询结果！");
				saveNoResultWord();
				return;
			}
			String targetContentList = html.substring(idx + hit.length());
			targetContentList = targetContentList.substring(0,
					targetContentList.indexOf(contentEnd));
			targetContentList = "{" + targetContentList;// 补齐为JSON格式
			// 将map中的html内容拿出来！
			targetContentList = JSONUtils.getSinaHtml(targetContentList);
			if (StringUtils.isBlank(targetContentList)) {
				Logger.log("查询词[" + getQuery() + "]没有查询结果！");
				saveNoResultWord();
				return;
			}
			doc = Jsoup.parse(targetContentList);
			if (weiboStart.equals(hit)) {
				parseWeibo(doc);
			} else if (userStart.equals(hit)) {
				parseUser(doc);
			}
		}

		if (weibo != null) {
			parseWeibo(weibo);
		} else if (user != null) {
			parseUser(user);
		}
		// 记录正在处理的页数
		Query q = getQuery();
		q.setCurrentPage(q.getCurrentPage() + 1);
		// 解析下一页
		WebElement ele = null;
		try {
			List<WebElement> eles = driver.findElements(By
					.className("search_page_M"));
			if (eles != null && !eles.isEmpty()) {
				ele = eles.get(0).findElement(By.linkText("下一页"));
			}
		} catch (Throwable e) {
			Logger.log("抓取下一页信息失败！原因：" + e);
		}
		if (ele != null && ele.isEnabled()) {
			ele.click();
			try {
				Thread.sleep(2000);// 等待一段时间，让其加载完毕！
			} catch (Exception e) {
			}
			parseHtmlToWeibo(driver);
		}
	}

	/**
	 * 将无结果的查询词保存到指定的文件中。
	 */
	private void saveNoResultWord() {
		try {
			FileUtils.write(new File(FileUtils.getUserDirectory(),
					"noresultWords.txt"), getQuery().getQueryStr()
					+ IOUtils.LINE_SEPARATOR, "UTF-8", true);
		} catch (IOException e) {
			Logger.log("保存无结果的查询词失败：" + getQuery());
		}
	}

	/**
	 * 保存因故没有查询的那些词
	 */
	private void saveNotQueryWord() {
		try {
			if (getQuery().getCurrentPage() == 0) {
				FileUtils.write(new File(FileUtils.getUserDirectory(),
						"notQueryWords.txt"), getQuery().getQueryStr()
						+ IOUtils.LINE_SEPARATOR, "UTF-8", true);
			}
		} catch (Exception e) {
			Logger.log("保存无结果的查询词失败：" + getQuery());
		}
	}

	/**
	 * 解析微博
	 * 
	 * @param targetContentList
	 * @throws ParserException
	 */
	private void parseWeibo(Element doc) {
		Elements eles = doc.getElementsByClass("search_feed");
		if (eles != null && eles.size() > 0) {
			eles = eles.get(0).getElementsByClass("feed_list");
			if (eles.size() > 0) {
				for (int i = 0; i < eles.size(); i++) {
					try {
						// 一条条的微博进行处理，解析每条微博的信息
						parseWeibo(
								StringUtils.trim(parseMsgUrlFromJSONStyle(eles
										.get(i))),
								StringUtils.trim(parseMsgPublishTime(eles
										.get(i))), getClient(), dataSource);
					} catch (Exception e) {
						e.printStackTrace();
					}
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
					FetchSinaWeibo
							.fetch(getClient(), dataSource, userUrl, null);
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
	public void parse(String[] wordsFiles) {
		// 循环读取每行中的词，如果词不为空，则读取并进行查询！
		if (wordsFiles != null && wordsFiles.length > 0) {
			WebDriver driver = ExploreRequest.getDriver(null);
			if (driver == null) {
				Logger.log("打开浏览器失败！停止工作！");
				return;
			}
			for (String file : wordsFiles) {
				file = StringUtils.trim(file);
				if (StringUtils.isEmpty(file)) {
					continue;
				}
				File realFile = new File(file);
				Collection<File> col = new ArrayList<File>();
				if (realFile.isFile()) {
					col.add(realFile);
				} else if (realFile.isDirectory()) {
					col = FileUtils.listFiles(realFile, new String[] { "txt" },
							true);
				} else {
					Logger.log("文件[" + file + "]不是一个有效的文件！");
					continue;
				}
				for (File f : col) {
					Map<String, Integer> process = StreamUtils
							.read(StreamUtils.WEIBO_SEARCH_SREAM);
					if (process == null) {
						process = new HashMap<String, Integer>();
					}
					Integer num = process.get(f.toString());
					if (num != null && num == -1) {
						// 表示当前的文件已经全部读取完毕！
						continue;
					}
					int hasReadLine = num == null ? 0 : num;
					int lineNum = 0;
					try {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(new FileInputStream(f),
										"GBK"));
						for (String word = br.readLine(); word != null; word = br
								.readLine()) {
							lineNum++;
							if (lineNum <= hasReadLine) {
								// 如果还没有到达指定的读取的地方，表示这一行已经解析过，不需要再解析，继续下一行读取
								continue;
							}
							try {
								if (StringUtils.isBlank(word)) {
									continue;
								} else {
									saveQuery(word, FilenameUtils.getBaseName(f
											.getAbsolutePath()));
									// 输入到输入框中，然后点击查询，并开始解析！
									search(driver, word);
									hasReadLine = lineNum;
								}
							} catch (UnreachableBrowserException e) {
								try {
									driver.quit();
									driver = ExploreRequest.getDriver(null);
									search(driver, word);
									hasReadLine = lineNum;
								} catch (Throwable e1) {
									if (driver != null) {
										driver.quit();
									}
									driver = ExploreRequest.getDriver(null);
									if (driver == null) {
										Logger.log("“打开浏览器并登录”操作失败！");
										System.exit(0);
									}
								}
							} catch (Throwable t) {
								Logger.log("当前文件[" + f + "]中的词[" + word
										+ "]出现问题，继续下一个词语搜索！" + t);
								saveNotQueryWord();
							}
							process.put(f.toString(), hasReadLine);
							StreamUtils.write(StreamUtils.WEIBO_SEARCH_SREAM,
									process);
						}
						br.close();
					} catch (Throwable t) {
						Logger.log("当前文件[" + f + "]处理过程中出现问题，继续下一个文件操作！" + t);
					}

					try {
						Logger.log("当前文件[" + f + "]已经处理完毕！");
						FileUtils.forceDelete(f);
						process.put(f.toString(), -1);// -1表示整个文件已经读取完毕！
						StreamUtils.write(StreamUtils.WEIBO_SEARCH_SREAM,
								process);
					} catch (Exception e) {
					}
				}
				clearQuery();
			}
			driver.quit();
		}
	}

	/**
	 * 查询指定的词，并进行解析！
	 * 
	 * @param driver
	 * @param word
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException
	 */
	private void search(WebDriver driver, String word)
			throws InterruptedException, UnsupportedEncodingException {
		// 使用链接跳转方式抓取！
		driver.navigate().to(
				"http://s.weibo.com/weibo/" + URLEncoder.encode(word, "UTF-8"));
		Thread.sleep(3000);

		// // 使用界面输入方式抓取！
		// if (!driver.getCurrentUrl().startsWith("http://s.weibo.com/weibo/"))
		// {
		// driver.navigate().to("http://s.weibo.com/weibo/" +
		// URLEncoder.encode(word, "UTF-8"));
		// Thread.sleep(2000);
		// }
		// WebElement input =
		// driver.findElements(By.className("searchInp_form")).get(0);
		// input.clear();
		// input.sendKeys(word);
		// driver.findElements(By.className("searchBtn")).get(0).click();
		// Thread.sleep(2000);

		parseHtmlToWeibo(driver);
	}
}
