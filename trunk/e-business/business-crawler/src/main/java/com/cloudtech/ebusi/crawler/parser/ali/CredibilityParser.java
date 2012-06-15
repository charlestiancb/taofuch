package com.cloudtech.ebusi.crawler.parser.ali;

import org.htmlparser.util.NodeList;

import com.cloudtech.ebusi.crawler.parser.CompanyInfo;

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
	public static boolean accept(NodeList nl) {
		if (nl == null) {
			return false;
		}
		CompanyInfo com = new CompanyInfo();
		ProfileParser.parseCred(nl, com);
		String chengXingTong = com.getDetails().get(CompanyInfo.CRED_NUM);
		String xinYongbianMa = com.getDetails().get(CompanyInfo.CRED_CODE);
		if (Integer.parseInt(chengXingTong) > 0 || xinYongbianMa.length() > 0) {
			return true;// 如果存在诚信通指数和信用编码，则认为是可以抓取的！
		}
		return false;
	}

}
