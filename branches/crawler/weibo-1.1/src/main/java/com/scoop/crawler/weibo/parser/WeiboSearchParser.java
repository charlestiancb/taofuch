package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.fetch.FetchSina;
import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.RegUtils;

/**
 * 微博搜索结果方式的微博，如：http://s.weibo.com/weibo/%25E7%25AF%25AE%25E7%2590%2583?topnav
 * =1&wvr=4&k=1，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class WeiboSearchParser extends JsonStyleParser {
	private String replace = "#REPLACE#";
	private String common = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + replace + "\",";
	private String weiboStart = common.replaceAll(replace, "pl_weibo_feedlist");
	private String userStart = common.replaceAll(replace, "pl_user_feedlist");

	public WeiboSearchParser(DataSource dataSource, RequestFailedHandler handler) {
		super(dataSource, handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(weiboStart) > -1 || html.indexOf(userStart) > -1;
	}

	public String parse(String html) throws IOException {
		// 判断是否没有查询结果！
		if (html.indexOf("<div class=\\\"search_noresult\\\">") > -1) {
			return null;
		}
		String hit = weiboStart;
		int idx = html.indexOf(weiboStart);
		if (idx == -1) {
			idx = html.indexOf(userStart);
			hit = userStart;
		}
		if (idx == -1) {
			return null;
		}
		String targetContentList = html.substring(idx + hit.length());
		targetContentList = targetContentList.substring(0, targetContentList.indexOf(contentEnd));
		targetContentList = "{" + targetContentList;// 补齐为JSON格式
		// 将map中的html内容拿出来！
		targetContentList = JSONUtils.getSinaHtml(targetContentList);
		Document doc = Jsoup.parse(targetContentList);
		if (weiboStart.equals(hit)) {
			parseWeibo(doc);
		} else if (userStart.equals(hit)) {
			parseUser(doc);
		}
		// 解析下一页
		Elements eles = doc.getElementsByAttributeValue("class", "search_page clearfix");
		if (eles != null && !eles.isEmpty()) {
			eles = eles.select("li").select("a");
			if (eles != null && !eles.isEmpty()) {
				Element ele = eles.last();
				if (ele.text().trim().equals("下一页")) {
					return "http://s.weibo.com" + ele.attr("href");
				}
			}
		}
		return null;
	}

	/**
	 * 解析微博
	 * 
	 * @param targetContentList
	 * @throws ParserException
	 */
	private void parseWeibo(Document doc) {
		Elements eles = doc.getElementsByClass("feed_list");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				saveQuery(RegUtils.parseToQuery(FetchSina.getBaseUrl()));
				try {
					// 一条条的微博进行处理，解析每条微博的信息
					parseWeibo(StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
							StringUtils.trim(parseMsgPublishTime(eles.get(i))), getClient(), dataSource);
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
	private void parseUser(Document doc) {
		Elements eles = doc.getElementsByClass("person_pic");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				try {
					String userUrl = eles.get(0).child(0).attr("href");
					// 解析用户信息。
					FetchSinaWeibo.fetch(getClient(), dataSource, userUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
