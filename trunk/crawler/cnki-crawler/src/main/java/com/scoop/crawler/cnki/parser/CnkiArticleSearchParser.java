package com.scoop.crawler.cnki.parser;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.cnki.entity.RefStatistic;
import com.scoop.crawler.cnki.request.Http;

/**
 * CNKI搜索入口程序
 * 
 * @author taofucheng
 * 
 */
public class CnkiArticleSearchParser {
	public static final String ENC = "UTF-8";

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
		String searchDataUrl = "http://epub.cnki.net/KNS/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.21&PageName=ASP.brief_result_aspx&DbPrefix=CRLD&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%bc%95%e6%96%87%e6%95%b0%e6%8d%ae%e5%ba%93&ConfigFile=CRLD.xml&db_opt=%E4%B8%AD%E5%9B%BD%E5%BC%95%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93&db_value=%E4%B8%AD%E5%9B%BD%E5%BC%95%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93&base_special1=%25&magazine_special1=%25&year_from="
				+ fromYear
				+ "&year_to="
				+ endYear
				+ "&yearB_type=echar&txt_4_sel=CLC&txt_4_value1="
				+ refContent
				+ "&txt_4_logical=and&txt_4_relation=%23CNKI_AND&txt_4_special1=%3D&his=0&__=Sat%20Oct%2026%202013%2022%3A40%3A56%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
		String param = Http.getPageContent(searchDataUrl, ENC);
		String dataListurl = "http://epub.cnki.net/kns/brief/brief.aspx?pagename="
				+ param + "&t=" + new Date().getTime() + "&keyValue=&S=1";
		fetchSearchList(dataListurl);
	}

	public static void fetchSearchList(String url) {
		String html = Http.getPageContent(url, ENC);
		// TODO
		Http.setRefer(url);
		for (int i = 0; i < 3; i++) {
			url = "http://epub.cnki.net/kns/detail/detail.aspx?QueryID=0&CurRec=1&DbCode=cjfd&dbname=cjfd2000&filename=ZGTS200005006";
			html = Http.getPageContent(url, "UTF-8");
		}
		// TODO
		Document doc = Jsoup.parse(html);
		Elements es = doc.getElementsByAttributeValue("class",
				"GridTableContent");
		Element table = es == null ? null : es.get(0);
		if (table == null) {
			return;
		}
		Elements trs = table.select("tr");
		if (trs != null && trs.size() > 1) {
			for (int i = 1; i < trs.size(); i++) {// 从第二开始，因为第一行是标题
				Elements tds = trs.get(i).getElementsByTag("td");
				if (tds != null && tds.size() > 5) {
					LineInfo line = new LineInfo();
					line.setIndex(tds.get(0).text());
					String title = tds.get(1).html();
					title = title.substring(title
							.indexOf("ReplaceJiankuohao('")
							+ "ReplaceJiankuohao('".length());
					title = title.substring(0, title.indexOf("')")).trim();
					line.setTitle(title);// 这是标题行
					line.setAuthor(tds.get(2).text());// 这是被引作者
					line.setOrigin(tds.get(3).text());// 这是被引文献来源
					line.setPublishTime(tds.get(4).text());// 这是发表时间
					String count = tds.get(5).html();
					count = count.substring(count.indexOf("document.write(")
							+ "document.write(".length());
					count = count.substring(0, count.indexOf(");")).trim();
					line.setCount(count);// 这是被引次数
					Elements link = tds.get(1).select("a");
					if (link != null && link.size() > 0) {
						// TODO 这个得到的链接有问题！少了一些如随机数的参数！
						String href = link.get(0).attr("href");
						href = href.startsWith("/") ? ("http://epub.cnki.net" + href)
								: url.substring(0, url.lastIndexOf("/") + 1)
										+ href;
						// 如果有超链接，则取超链接，并根据超链接抓取对应的引用文献信息
						line.setRefs(CnkiReferenceParser.parseReference(href));
					}
					// TODO 直接写到文件中
				}
			}
		}
		// 获取下一页链接
		Elements pages = doc.getElementsByAttributeValue("class",
				"TitleLeftCell");
		if (pages != null && pages.size() > 0) {
			Elements pageNum = pages.get(0).select("a");
			if (pageNum != null && pageNum.size() > 0) {
				Element nextPage = pageNum.get(pageNum.size() - 1);
				if ("下一页".equals(StringUtils.trim(nextPage.text()))) {
					fetchSearchList("http://epub.cnki.net/kns/brief/brief.aspx"
							+ nextPage.attr("href"));
				}
			}
		}
	}

}

class LineInfo {
	/** 序号 */
	private String index;
	/** 被引题名 */
	private String title;
	/** 被引作者 */
	private String author;
	/** 被引文献来源 */
	private String origin;
	/** 发表时间 */
	private String publishTime;
	/** 被引次数 */
	private String count;
	/** 被引用的文献 */
	private List<RefStatistic> refs;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<RefStatistic> getRefs() {
		return refs;
	}

	public void setRefs(List<RefStatistic> refs) {
		this.refs = refs;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
