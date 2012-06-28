package com.cloudtech.ebusi.crawler.parser.hc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

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

		com.setB2bHomepage(nl.elementAt(0).getPage().getUrl());
		boolean ok = parseComBase(nl, com);
		if (!ok) {
			return null;
		}
		NodeList _nlTmp = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "contentbox"), true);
		_nlTmp = _nlTmp.extractAllNodesThatMatch(new TagNameFilter("table"), true);
		// TODO 做到这里
		ok = parseComDetails((TableTag) _nlTmp.elementAt(0), com);
		if (!ok) {
			return null;
		}
		// 将解析的一对对内容解析成索引信息所需的属性与值的对应关系。
		parseToIndexFormat(com);
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
		NodeList _nl = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "comInfogo"), true);
		_nl = _nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "comName"), true);
		if (_nl.size() > 0) {
			// 如果存在公司名称，则取之
			com.setCompanyName(_nl.elementAt(0).toPlainTextString().trim());
		}
		// 公司简介
		_nl = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "cAbout"), true);
		com.setIntroduce(_nl.elementAt(0).toPlainTextString().trim());
		// 公司图片Link
		_nl = _nl.extractAllNodesThatMatch(new TagNameFilter("img"), true);
		if (_nl.size() > 0) {
			// TODO 慧聪的居然取不到！可能是异步加载的！
			ImageTag image = (ImageTag) _nl.elementAt(1);
			com.setComPicLink(StringUtils.trimToEmpty(image.getImageURL()));
		}
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
		NodeList _nl = nl.extractAllNodesThatMatch(new HasAttributeFilter("class", "comInfogo"), true);
		_nl = _nl.extractAllNodesThatMatch(new TagNameFilter("li"), true);
		for (int i = 0; i < _nl.size(); i++) {
			Bullet li = (Bullet) _nl.elementAt(i);
			String liContent = li.toPlainTextString();
			if (liContent.indexOf("买卖通指数：") > -1) {
				// 如果这是买卖通指数信息，则取之
				com.putDetails(CompanyInfo.CRED_NUM, liContent.replace("买卖通指数：", "").trim());
			}
		}
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
