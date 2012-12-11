package com.scoop.crawler.weibo.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.repository.DataSource;

/**
 * 解析类似这样：http://gov.weibo.com/profile.php?uid=sciencenet&ref=链接形式中的微博内容。
 * 这个内容的组织方式是HTML的，如：加V或官方网站方式的微博
 * 
 * @author taofucheng
 * 
 */
public class HtmlStyleParser extends Parser {

	public HtmlStyleParser(DefaultHttpClient client, DataSource dataSource) {
		super(client, dataSource);
	}

	public void parse(Elements eles) throws IOException {
		for (int i = 0; i < eles.size(); i++) {
			// 一条条的微博进行处理，解析每条微博的信息
			parseWeibo(	StringUtils.trim(parseMsgUrlFromHtmlStyle(eles.get(i))),
						StringUtils.trim(parseMsgPublishTime(eles.get(i))),
						client,
						dataSource);
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
}
