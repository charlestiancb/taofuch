package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.fetch.FetchSina;
import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.RegUtils;

/**
 * 微博搜索结果方式的微博，如：http://s.weibo.com/weibo/%25E7%25AF%25AE%25E7%2590%2583?topnav
 * =1&wvr=4&k=1，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class JsonStyle4SearchParser extends JsonStyleParser {
	private String replace = "#REPLACE#";
	private String common = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + replace + "\",";
	private String weiboStart = common.replaceAll(replace, "pl_weibo_feedlist");
	private String userStart = common.replaceAll(replace, "pl_user_feedlist");

	public JsonStyle4SearchParser(DefaultHttpClient client, DataSource dataSource) {
		super(client, dataSource);
	}

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(weiboStart) > -1 || html.indexOf(userStart) > -1;
	}

	public void parse(String html) throws IOException {
		String hit = weiboStart;
		int idx = html.indexOf(weiboStart);
		if (idx == -1) {
			idx = html.indexOf(userStart);
			hit = userStart;
		}
		if (idx == -1) {
			return;
		}
		String targetContentList = html.substring(idx + hit.length());
		targetContentList = targetContentList.substring(0, targetContentList.indexOf(contentEnd));
		targetContentList = "{" + targetContentList;// 补齐为JSON格式
		// 将map中的html内容拿出来！
		targetContentList = JSONUtils.getSinaHtml(targetContentList);
		if (weiboStart.equals(hit)) {
			parseWeibo(targetContentList);
		} else if (userStart.equals(hit)) {
			parseUser(targetContentList);
		}
	}

	/**
	 * 解析微博
	 * 
	 * @param targetContentList
	 * @throws ParserException
	 */
	private void parseWeibo(String targetContentList) {
		Elements eles = Jsoup.parse(targetContentList).getElementsByClass("feed_list");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				saveQuery(RegUtils.parseToQuery(FetchSina.getBaseUrl()));
				try {
					// 一条条的微博进行处理，解析每条微博的信息
					parseWeibo(StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
							StringUtils.trim(parseMsgPublishTime(eles.get(i))), client, dataSource);
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
	private void parseUser(String targetContentList) {
		Elements eles = Jsoup.parse(targetContentList).getElementsByClass("person_pic");
		if (eles.size() > 0) {
			for (int i = 0; i < eles.size(); i++) {
				try {
					String userUrl = eles.get(0).child(0).attr("href");
					// 解析用户信息。
					FetchSinaWeibo.fetch(client, dataSource, userUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
