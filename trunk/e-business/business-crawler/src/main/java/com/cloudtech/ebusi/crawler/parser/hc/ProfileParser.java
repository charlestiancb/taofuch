package com.cloudtech.ebusi.crawler.parser.hc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.cloudtech.ebusi.crawler.parser.CompanyInfo;

/**
 * 慧聪会员信息解析器
 * 
 * @author taofucheng
 * 
 */
public class ProfileParser {

	/**
	 * 解析用户信息，并获取这些信息，便于进行索引！
	 * 
	 * @param nl
	 *            所有Tab链接
	 */
	public static CompanyInfo getIndexComInfo(NodeList nl) {
		CompanyInfo com = new CompanyInfo();
		parseCred(nl, com);
		// 下面的内容解析都是在同一个页面中！如：http://gykaida.cn.alibaba.com/athena/companyprofile/gykaida.html
		String mainLink = getBulletLink(nl.elementAt(3));
		com.setB2bHomepage(mainLink);
		try {
			NodeList _nl = new Parser(mainLink).parse(new HasAttributeFilter(	"class",
																				"m-body m-body-ext info-detail info-brief"));
			boolean ok = parseComBase(_nl, com);
			if (!ok) {
				return null;
			}
			NodeList _nlTmp = _nl.extractAllNodesThatMatch(new TagNameFilter("table"), true);
			ok = parseComDetails((TableTag) _nlTmp.elementAt(0), com);
			if (!ok) {
				return null;
			}
			ok = parseComDetails((TableTag) _nlTmp.elementAt(1), com);
			if (!ok) {
				return null;
			}
			ok = parseComDetails((TableTag) _nlTmp.elementAt(2), com);
			if (!ok) {
				return null;
			}
			// 将解析的一对对内容解析成索引信息所需的属性与值的对应关系。
			parseToIndexFormat(com);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return com;
	}

	/**
	 * 将网页内容转换成索引字段与值的对应关系。
	 * 
	 * @param com
	 */
	private static void parseToIndexFormat(CompanyInfo com) {
		Map<String, String> details = new HashMap<String, String>();
		for (int i = 0; i < CompanyInfo.COM_INDEX_FILEDS.length; i++) {
			String key = CompanyInfo.COM_INDEX_FILEDS[i];
			String value = StringUtils.trimToNull(com.getDetails().get(key));
			if (value == null) {
				value = StringUtils.trimToNull(com.getDetails().get(key + "："));
			}
			if (value == null) {
				// 如果没有内容，则不处理
				continue;
			} else {
				details.put(CompanyInfo.COM_INDEX_KEYS[i], value);
			}
		}
		// 将处理结果放回去
		com.getDetails().clear();
		com.getDetails().putAll(details);
		// 将homepage信息设置到对应属性中
		com.setHomepage(com.getDetails().get(CompanyInfo.INDEX_HOMEPAGE));
		com.getDetails().remove(CompanyInfo.INDEX_HOMEPAGE);
	}

	/**
	 * 解析公司名称 与 公司简介 信息。
	 * 
	 * @param nl
	 *            整个页面的主要内容
	 * @param com
	 */
	private static boolean parseComBase(NodeList nl, CompanyInfo com) {
		// 公司名称
		NodeList _nlTmp = nl.extractAllNodesThatMatch(	new HasAttributeFilter("class", "info-company-title fd-clr"),
														true);
		Span span = (Span) _nlTmp.extractAllNodesThatMatch(new TagNameFilter("span"), true).elementAt(0);
		if (span == null) {
			return false;
		}
		TextNode node = (TextNode) span.childAt(0);
		com.setCompanyName(node.toHtml());
		// 公司简介
		_nlTmp = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "info-body"), true);
		com.setIntroduce(_nlTmp.elementAt(0).getChildren().elementAt(1).toHtml());
		// 公司图片Link
		_nlTmp = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "info-image-list"), true);
		_nlTmp = _nlTmp.extractAllNodesThatMatch(new TagNameFilter("img"), true);
		String picLink = "";
		for (int i = 0; i < _nlTmp.size(); i++) {
			ImageTag n = (ImageTag) _nlTmp.elementAt(i);
			if (StringUtils.isNotBlank(n.getImageURL())) {
				picLink += StringUtils.trim(n.getImageURL()) + ",";
			}
		}
		if (picLink.length() > 0) {
			picLink = picLink.substring(0, picLink.length() - 1);
		}
		com.setComPicLink(picLink);
		return true;
	}

	/**
	 * 解析出诚信相关的指数。
	 * 
	 * @param nl
	 *            所有Tab链接
	 * @param com
	 */
	public static void parseCred(NodeList nl, CompanyInfo com) {
		String link = getBulletLink(nl.elementAt(2));
		if (StringUtils.isBlank(link)) {
			return;
		}
		try {
			Parser par = new Parser(link);
			NodeList _nl = par.parse(new HasAttributeFilter("class", "fd-clr"));
			if (_nl.size() > 0) {
				String content = _nl.asString().trim();
				content = content.substring(content.indexOf("诚信通指数") + "诚信通指数".length());
				int idx = content.indexOf("\n");
				String chengXingTong = content.substring(0, idx == -1 ? 0 : idx).trim();
				content = content.substring(content.indexOf("信用编码") + "信用编码".length());
				idx = content.indexOf("\n");
				String xinYongbianMa = content.substring(0, idx == -1 ? 0 : idx).trim();
				if (StringUtils.isNumeric(chengXingTong)) {
					com.putDetails(CompanyInfo.CRED_NUM, chengXingTong);
					com.putDetails(CompanyInfo.CRED_CODE, xinYongbianMa);
				}
			}
		} catch (Exception e) {
			System.err.println("判断的解析失败：" + link + "  " + ExceptionUtils.getFullStackTrace(e));
		}
	}

	private static String getBulletLink(Node node) {
		if (!(node instanceof Bullet)) {
			return null;
		}
		node = node.getChildren().extractAllNodesThatMatch(new HasAttributeFilter("href")).elementAt(0);
		LinkTag t = (LinkTag) node;
		return t.extractLink();
	}

	/**
	 * 解析公司的详细信息！并将这些信息组装成一对对的Map值，如：{"员工数":"70"}
	 * 
	 * @param nl
	 * @param com
	 */
	private static boolean parseComDetails(TableTag details, CompanyInfo com) {
		if (details == null || details.getChildren() == null) {
			return false;
		}
		NodeList tds = details.getChildren().extractAllNodesThatMatch(new TagNameFilter("td"), true);
		for (int i = 0; i < tds.size(); i += 2) {
			String label = StringUtils.trimToEmpty(tds.elementAt(i).toPlainTextString());
			String content = StringUtils.trimToEmpty(tds.elementAt(i + 1).toPlainTextString());
			if (tds.elementAt(i).getChildren().extractAllNodesThatMatch(new TagNameFilter("a")).size() > 0) {
				label = getLinkTagContent(label);
			}
			if (tds.elementAt(i + 1).getChildren().extractAllNodesThatMatch(new TagNameFilter("a")).size() > 0) {
				content = getLinkTagContent(content);
			}
			com.putDetails(StringUtils.trimToEmpty(label), StringUtils.trimToEmpty(content.trim()));
		}
		return true;
	}

	/**
	 * 获取连接中的内容
	 * 
	 * @param content
	 * @return
	 */
	private static String getLinkTagContent(String content) {
		String[] contents = content.split("\n");
		content = "";
		for (String c : contents) {
			c = StringUtils.trimToEmpty(c);
			if (c.length() == 0) {
				continue;
			}
			content += c + ",";
		}
		content = content.substring(0, content.length() - 1);
		return content;
	}
}
