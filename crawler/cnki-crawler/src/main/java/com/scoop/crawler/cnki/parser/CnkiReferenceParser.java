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
	private static final String start = pre + "frame/list.aspx?filename=";
	private static final String end = "reftype=";
	private static final String split = "(\\. )|(\\. )";

	public static void main(String[] args) {

		String citationurl = "http://www.cnki.net/kcms/detail/detail.aspx?dbCode=cjfd&QueryID=34&CurRec=2&filename=ZGTS200506003&dbname=CJFD0305";
		CnkiReferenceParser.parseReference(citationurl);
	}

	public static List<RefStatistic> parseReference(Document page) {
		List<RefStatistic> result = new ArrayList<RefStatistic>();
		try {
			String tmp = page.data();
			int idx = tmp.indexOf(start);
			if (idx == -1) {
				return result;
			}
			List<Reference> references = new ArrayList<Reference>();
			LinkedHashMap<Integer, RefStatistic> stats = new LinkedHashMap<Integer, RefStatistic>();
			String listUrl = tmp.substring(idx + pre.length());
			listUrl = listUrl.substring(0, listUrl.indexOf(end) + end.length());
			listUrl = "http://www.cnki.net/kcms/detail/" + listUrl + "3&page=1&CurDBCode=" + getCurDbCode(listUrl);
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
			return parseReference(Jsoup.connect(artifactUrl).timeout(30000).get());
		} catch (IOException e) {
			return result;
		}
	}

	/**
	 * 解析每一页分页的信息
	 * 
	 * @param listUrl
	 * @throws IOException
	 */
	private static void parsePaging(String listUrl, List<Reference> references) throws IOException {
		if (StringUtil.isBlank(listUrl)) {
			return;
		}
		Document doc = Jsoup.parse(Http.getPageContent(listUrl, "UTF-8"));
		Elements es = doc.getElementsByClass("content").select("li");
		if (es.isEmpty()) {
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
		String page = listUrl.substring(listUrl.indexOf("&page="));
		page = page.substring(page.indexOf("=") + 1);
		page = page.substring(0, page.indexOf("&"));
		String[] paths = listUrl.split("&page=" + page);
		page = String.valueOf(Integer.parseInt(page) + 1);
		listUrl = paths[0] + "&page=" + page + paths[1];
		parsePaging(listUrl, references);
	}

	private static Reference getRefInfo(Element e) {
		String content = e.text();
		String[] es = content.split(split);
		if (es.length < 4) {
			return null;
		}
		Reference r = new Reference();
		r.setAuthor(getAuthor(StringUtils.trimToEmpty(es[0])));
		r.setInstitution(StringUtils.trimToEmpty(es[2]));
		r.setTitle(StringUtils.trimToEmpty(es[1]));
		r.setYear(getYear(es[3]));
		r.setYearAdd(getYearAdd(es[3]));
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

	private static int getYear(String str) {
		int idx = str.indexOf("(");
		if (idx == -1) {
			return Integer.parseInt(StringUtils.trimToEmpty(str));
		}
		String tmp = StringUtils.trimToEmpty(str.substring(0, idx));
		return Integer.parseInt(tmp);
	}

	private static String getCurDbCode(String listUrl) {
		int flag = listUrl.indexOf("dbcode=");
		String tmp = listUrl.substring(flag);
		tmp = tmp.substring(0, tmp.indexOf("&"));
		return tmp.substring(tmp.indexOf("=") + 1);
	}
}
