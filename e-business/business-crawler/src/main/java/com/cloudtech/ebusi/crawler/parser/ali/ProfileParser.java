package com.cloudtech.ebusi.crawler.parser.ali;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;

import com.cloudtech.ebusi.crawler.parser.AliParser;
import com.cloudtech.ebusi.crawler.parser.CompanyInfo;

/**
 * 阿里会员信息解析器
 * 
 * @author taofucheng
 * 
 */
public class ProfileParser {

	/**
	 * 解析用户信息，并将这些信息进行索引！
	 * 
	 * @param nl
	 */
	public static CompanyInfo indexComInfo(NodeList nl) {
		CompanyInfo com = new CompanyInfo();
		parseCred(nl, com);
		// 下面的内容解析都是在同一个页面中！如：http://gykaida.cn.alibaba.com/athena/companyprofile/gykaida.html
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 解析出诚信相关的指数。
	 * 
	 * @param nl
	 * @param com
	 */
	private static void parseCred(NodeList nl, CompanyInfo com) {
		NodeList _nl = nl.extractAllNodesThatMatch(new HasAttributeFilter("data-page-type"), true);
		String link = AliParser.getBulletLink(_nl.elementAt(2));
		try {
			Parser par = new Parser(link);
			_nl = par.parse(new HasAttributeFilter("class", "fd-clr"));
			if (nl.size() > 0) {
				String content = nl.asString().trim();
				content = content.substring(content.indexOf("诚信通指数") + "诚信通指数".length());
				String chengXingTong = content.substring(0, content.indexOf("\n")).trim();
				content = content.substring(content.indexOf("信用编码") + "信用编码".length());
				String xinYongbianMa = content.substring(0, content.indexOf("\n")).trim();
				com.putDetails(CompanyInfo.CRED_NUM, chengXingTong);
				com.putDetails(CompanyInfo.CRED_CODE, xinYongbianMa);
			}
			nl.elementAt(0);
		} catch (Exception e) {
			System.err.println("判断的解析失败：" + link + "  " + ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 解析公司主体信息，如：公司简介、注册信息等
	 * 
	 * @param nl
	 * @param com
	 */
	private static void parseComMain(NodeList nl, CompanyInfo com) {
		// TODO
	}

	/**
	 * 解析公司行业信息。
	 * 
	 * @param nl
	 * @param com
	 */
	private static void parseComIndustry(NodeList nl, CompanyInfo com) {
		// TODO
	}

	/**
	 * 解析公司的其它信息！
	 * 
	 * @param nl
	 * @param com
	 */
	private static void parseComDetails(NodeList nl, CompanyInfo com) {
		// TODO
	}
}
