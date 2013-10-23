package com.scoop.crawler.cnki.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.cnki.entity.RefStatistic;
import com.scoop.crawler.cnki.entity.Reference;
import com.scoop.crawler.cnki.request.Http;

/**
 * 解析CNKI库中每篇文章的引用文章信息。
 * 
 * @author taofucheng
 * 
 */
public class CnkiReferenceParser {
	private static final String pre = "','";
	private static final String start = pre + "frame/list.aspx?";
	private static final String end = "&reftype=";
	private static final String split = "\\.";

	public static void main(String[] args) {

		String citationurl = "http://www.cnki.net/kcms/detail/detail.aspx?dbCode=cjfd&QueryID=4&CurRec=10&filename=TSQB201020011&dbname=CJFD0910&v=MjA0ODVSWVM2VDU5U256anJCWXplckk9T2xtekc3TzVIS2ZFcDR0RVp1OFBmUTVXeg==";
		System.out.println(CnkiReferenceParser.parseReference(citationurl));
	}

	public static List<RefStatistic> parseReference(Document page) {
		List<RefStatistic> result = new ArrayList<RefStatistic>();
		try {
			String tmp = page.data();
			int idx = tmp.indexOf(start);
			if (idx == -1) {
				System.err.println("请求文章信息失败！"
						+ page.getElementsByAttributeValue("class", "sorry")
								.html());
				return result;
			}
			List<Reference> references = new ArrayList<Reference>();
			LinkedHashMap<Integer, RefStatistic> stats = new LinkedHashMap<Integer, RefStatistic>();
			String listUrl = tmp.substring(idx + pre.length());
			listUrl = listUrl.substring(0, listUrl.indexOf(end) + end.length());
			listUrl = "http://www.cnki.net/kcms/detail/" + listUrl + "1&page=1";// 指定抓取的reftype=1，便于解析
			parsePaging(listUrl, references);
			Collections.sort(references);
			for (Reference r : references) {
				RefStatistic stat = stats.get(r.getYear());
				if (stat == null) {
					stat = new RefStatistic();
					stat.setYear(r.getYear());
					stat.setCount(1);
				} else {
					stat.setCount(stat.getCount() + 1);
				}
				stat.addRef(r);
				stats.put(r.getYear(), stat);
			}
			if (stats != null) {
				result.addAll(stats.values());
			}
		} catch (IOException e) {
			System.err.println("发生错误，但不影响后面的解析！错误信息：");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 解析指定文章的引文信息
	 * 
	 * @param artifactUrl
	 *            具体的文章的URL。
	 */
	public static List<RefStatistic> parseReference(String artifactUrl) {
		List<RefStatistic> result = new ArrayList<RefStatistic>();
		if (StringUtil.isBlank(artifactUrl)) {
			return result;
		}
		try {
			return parseReference(Jsoup.parse(Http.getPageContent(artifactUrl,
					"UTF-8")));
		} catch (Exception e) {
			return result;
		}
	}

	/**
	 * 解析每一页分页的信息
	 * 
	 * @param listUrl
	 * @throws IOException
	 */
	private static void parsePaging(String listUrl, List<Reference> references)
			throws IOException {
		if (StringUtil.isBlank(listUrl)) {
			return;
		}
		try {
			Thread.sleep(2000L);// 等待两秒，模拟人点击
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Document doc = Jsoup.parse(Http.getPageContent(listUrl, "UTF-8"));
		Elements es = doc.getElementsByClass("content").select("li");
		if (es.isEmpty()) {
			System.err.println(references.isEmpty() ? "请求引用文献失败！" : ""
					+ doc.getElementsByAttributeValue("class", "sorry").html());
			return;
		}
		Iterator<Element> el = es.iterator();
		Element e = null;
		while (el.hasNext()) {
			e = el.next();
			if (e == null) {
				continue;
			}
			Reference ref = getRefInfo(e);
			if (ref != null) {
				references.add(ref);
			}
		}
		// 解析下一页的内容
		int idx = listUrl.indexOf("&page=");
		if (idx > -1) {
			String page = listUrl.substring(idx);
			page = page.substring(page.indexOf("=") + 1);
			// page = page.substring(0, page.indexOf("&"));
			String[] paths = listUrl.split("&page=" + page);
			page = String.valueOf(Integer.parseInt(page) + 1);
			listUrl = paths[0] + "&page=" + page
					+ (paths.length < 2 ? "" : paths[1]);
			parsePaging(listUrl, references);
		}
	}

	/**
	 * 将每个li里面的内容解析成对应的字段信息
	 * 
	 * @param e
	 * @return
	 */
	private static Reference getRefInfo(Element e) {
		String content = e.text();
		String[] es = content.split(split);// 以“. ”切割内容。
		if (es.length < 4) {
			return null;
		}
		Reference r = new Reference();
		r.setAuthor(getAuthor(trim(es[0])));// 第一部分是作者
		r.setYear(getYear(trim(es[es.length - 1])));// 第后部分是出版年份
		r.setYearAdd(getYearAdd(trim(es[es.length - 1])));
		String tmp = e.html().replace(StringUtils.trim(es[0]), "")
				.replace(StringUtils.trim(es[es.length - 1]), "");
		Element artifact = Jsoup.parse(tmp).select("a").first();
		String next = artifact.nextSibling().toString().toString();// 文章后面的一个内容
		String title = artifact.text() + (next.startsWith("[") ? next : "");
		tmp = tmp.replace(artifact.html(), "");
		if (next.startsWith("[")) {
			tmp = tmp.replace(next, "");
		}
		String institution = Jsoup.parse(tmp).text();
		institution = institution.startsWith(".") ? institution.substring(1)
				: institution;
		institution = institution.endsWith(".") ? institution.substring(0,
				institution.length() - 1) : institution;
		r.setInstitution(trim(institution));
		r.setTitle(trim(title));
		return r;
	}

	private static String getAuthor(String author) {
		int idx = author.indexOf("]");
		if (author.startsWith("[") && idx > 0) {
			// 如果有[2]形式的内容，则去除！
			author = author.substring(idx + 1);
		}
		return author;
	}

	/**
	 * 将类似“2008(04)”这样的解析成“04”。
	 * 
	 * @param str
	 * @return
	 */
	private static String getYearAdd(String str) {
		int idx = str.indexOf("(");
		if (idx == -1) {
			return "";
		}
		String tmp = str.substring(idx + 1);
		idx = tmp.indexOf(")");
		if (idx == -1) {
			return StringUtils.trimToEmpty(tmp);
		}
		return tmp.substring(0, idx).trim();
	}

	/**
	 * 将类似“2008(04)”这样的解析成“2008”。
	 * 
	 * @param str
	 * @return
	 */
	private static int getYear(String str) {
		int idx = str.indexOf("(");
		if (idx == -1) {
			return (StringUtils.isBlank(str) || !StringUtils.isNumeric(str)) ? 0
					: Integer.parseInt(StringUtils.trimToEmpty(str));
		}
		String tmp = StringUtils.trimToEmpty(str.substring(0, idx));
		return Integer.parseInt(tmp);
	}

	private static String trim(String str) {
		str = StringUtils.trim(str);
		if (StringUtils.isBlank(str)) {
			return str;
		} else {
			while (str.startsWith("　")) {
				str = str.substring(1);
			}
			while (str.endsWith("　")) {
				str = str.substring(0, str.length() - 1);
			}
			return str;
		}
	}
}
