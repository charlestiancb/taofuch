package com.scoop.crawler.cnki.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.cnki.entity.RefStatistic;
import com.scoop.crawler.cnki.request.Http;

/**
 * CNKI抓取的总入口程序
 * 
 * @author taofucheng
 * 
 */
public class CnkiArticleParser {

	/**
	 * @param args
	 * @throws ParserException
	 */
	private List<String> metalist = new ArrayList<String>();
	public String journal;
	public String titleCn = null;// 中文篇名
	public String titleEn = null;// 英文篇名
	public String author = null;// 中文作者
	public String abstr = null;// 中文摘要
	public String keywords = null;// 中文关键字
	public String citedsum;// 各年份被引总频次
	public String downsum;// 各年份下载总频次
	public String year;
	public String volume;
	public List<RefStatistic> citationlist = new ArrayList<RefStatistic>();// 被引文献信息列表

	private static final String AUTH = "【作者】";
	private static final String ABSTRACT = "【摘要】";
	private static final String KEYWORD = "【关键词】";
	private static final String QUOTED = "【被引频次】";
	private static final String DOWNLOAD = "【下载频次】";

	public CnkiArticleParser() {
		journal = "";
		titleCn = "";
		author = "";
		abstr = "";
		keywords = "";
		citedsum = "";
		downsum = "";
		year = "";
		volume = "";
	}

	public void getHtml(String downUrl) {
		try {
			String html = Http.getPageContent(downUrl, "UTF-8");
			if (StringUtils.isBlank(html)) {
				// 没有返回内容，说明有错误，记录下来，然后下次重试！
				RecordErrorParser.recordFailedUrl(downUrl);
				return;
			}
			Document page = Jsoup.parse(html);
			getMeta(page);// 解析信息
			citationlist = CnkiReferenceParser.parseReference(page);
		} catch (Exception e) {
			System.err.println("发生错误[" + downUrl + "]：" + ExceptionUtils.getFullStackTrace(e));
		}
	}

	// 文章的相关数据
	protected void getMeta(Document page) {
		// 先将所有的结果获取，然后再一条条的解析：论文名、作者、摘要、关键词、被引频次、下载频次
		// 论文名
		Element e = page.getElementById("chTitle");
		titleCn = StringUtils.trimToEmpty(e == null ? "" : e.text());
		metalist.add(titleCn);
		e = page.getElementById("enTitle");
		titleEn = StringUtils.trimToEmpty(e == null ? "" : e.text());
		metalist.add(titleEn);
		// 作者与摘要
		Elements tmp = page.getElementsByAttributeValue("class", "author summaryRight").select("p");
		if (tmp != null && !tmp.isEmpty()) {
			author = StringUtils.trimToEmpty(tmp.first().text());
			author = author.startsWith(AUTH) ? author.substring(AUTH.length()) : author;
			metalist.add(author);
			//
			abstr = StringUtils.trimToEmpty(tmp.last().text());
			abstr = abstr.startsWith(ABSTRACT) ? abstr.substring(ABSTRACT.length()) : abstr;
			metalist.add(abstr);
		}

		// 关键词
		tmp = page.getElementsByAttributeValue("class", "keywords int5");
		keywords = StringUtils.trimToEmpty(tmp == null ? "" : tmp.text());
		keywords = keywords.startsWith(KEYWORD) ? keywords.substring(KEYWORD.length()) : keywords;
		keywords = keywords.endsWith("【文内图片】") ? keywords.substring(0, keywords.lastIndexOf("【文内图片】")) : keywords;
		metalist.add(keywords);
		// 被引频次
		tmp = page.getElementsByAttributeValue("class", "summary  pad10").select("li");
		if (tmp != null && !tmp.isEmpty()) {
			Iterator<Element> eles = tmp.iterator();
			while (eles.hasNext()) {
				String text = StringUtils.trimToEmpty(eles.next().text());
				if (text.startsWith(QUOTED)) {
					// 被引频次
					citedsum = StringUtils.trim(text.replace(QUOTED, ""));
					metalist.add(citedsum);
				} else if (text.startsWith(DOWNLOAD)) {
					// 下载频次
					downsum = StringUtils.trim(text.replace(DOWNLOAD, ""));
					metalist.add(downsum);
				}
			}
		}
	}
}
