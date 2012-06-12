package com.cloudtech.ebusi.crawler.parser.ali;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;

/**
 * 阿里会员的诚信档案解析器
 * 
 * @author taofucheng
 * 
 */
public class CredibilityParser {
	/**
	 * 判断是否符合信用档案的要求，即：是否开通了诚信通
	 * 
	 * @param credibilityLink
	 *            公司信用档案的页面链接
	 * @return
	 */
	public static boolean accept(String credibilityLink) {
		if (StringUtils.isBlank(credibilityLink)) {
			return false;
		}
		try {
			Parser par = new Parser(credibilityLink);
			NodeList nl = par.parse(new HasAttributeFilter("class", "fd-clr"));
			if (nl.size() > 0) {
				String content = nl.asString().trim();
				content = content.substring(content.indexOf("诚信通指数") + "诚信通指数".length());
				String chengXingTong = content.substring(0, content.indexOf("\n")).trim();
				content = content.substring(content.indexOf("信用编码") + "信用编码".length());
				String xinYongbianMa = content.substring(0, content.indexOf("\n")).trim();
				if (Integer.parseInt(chengXingTong) > 0 || xinYongbianMa.length() > 0) {
					return true;// 如果存在诚信通指数和信用编码，则认为是可以抓取的！
				}
			}
			nl.elementAt(0);
		} catch (Exception e) {
			System.err.println("判断的解析失败：" + credibilityLink + "  " + ExceptionUtils.getFullStackTrace(e));
		}
		return false;
	}

}
