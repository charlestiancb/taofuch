package com.cloudtech.ebusi.crawler.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 抓取的公司信息！
 * 
 * @author taofucheng
 * 
 */
public class CompanyInfo {
	/** 信用指数 */
	public static final String CRED_NUM = "credNum";
	/** 信用编码 */
	public static final String CRED_CODE = "credCode";
	// ////////////////////////////////////////////////////////
	/** 索引字段：公司名称 */
	public static final String INDEX_NAME = "com_name";
	/** 索引字段：公司介绍 */
	public static final String INDEX_INTRO = "com_introduce";
	/** 索引字段：主营产品 */
	public static final String INDEX_PRODUCTS = "com_products";
	/** 索引字段：主营行业 */
	public static final String INDEX_INDUSTRY = "com_industry";
	/** 索引字段：经营模式 */
	public static final String INDEX_TRADE = "com_trade_style";
	/** 索引字段：是否提供加工/定制 */
	public static final String INDEX_DEFINE = "com_define";
	/** 索引字段：注册资本 */
	public static final String INDEX_REGIST = "com_regist";
	/** 索引字段：成立时间 */
	public static final String INDEX_ESTABLISH_TIME = "com_establish_time";
	/** 索引字段：注册地 */
	public static final String INDEX_REGIST_ADDR = "com_regist_addr";
	/** 索引字段：企业类型 */
	public static final String INDEX_TYPE = "com_type";
	/** 索引字段：法人代表 */
	public static final String INDEX_REPRESENTATIVE = "com_representative";

	/** 索引字段：加工方式 */
	public static final String INDEX_PROCESS_STYLE = "com_process_style";
	/** 索引字段：工艺 */
	public static final String INDEX_CRAFT = "com_craft";
	/** 索引字段：服务领域 */
	public static final String INDEX_SERVICE_AREA = "com_service_area";

	/** 索引字段：员工人数 */
	public static final String INDEX_EMPLOYEE = "com_employee";
	/** 索引字段：研发部门人数 */
	public static final String INDEX_RDC_EMPLOYEE = "com_rdc_employee";
	/** 索引字段：厂房面积 */
	public static final String INDEX_FACTORY_AREA = "com_factory_area";
	/** 索引字段：主要销售区域 */
	public static final String INDEX_SALE_AREA = "com_sale_area";
	/** 索引字段：主要客户群体 */
	public static final String INDEX_SALE_CUSTOMER = "com_sale_customer";
	/** 索引字段：月产量 */
	public static final String INDEX_MONTH_PRODUCE = "com_month_produce";
	/** 索引字段：年营业额 */
	public static final String INDEX_YEAR_PRODUCE = "com_year_produce";
	/** 索引字段：公司主页 */
	public static final String INDEX_HOMEPAGE = "com_homepage";
	// ////////////////////////////////////////////////////////
	/**
	 * 各个字段名称对应的索引字段名称：主营产品或服务、主营行业、经营模式、
	 * 是否提供加工/定制、注册资本、公司成立时间、公司注册地、企业类型、法定代表人、
	 * 加工方式、工艺、服务领域、员工人数、研发部门人数、厂房面积、主要销售区域 、主要客户群体、月产量、年营业额、公司主页"
	 */
	public static final String[] COM_INDEX_KEYS = new String[] { INDEX_PRODUCTS, INDEX_INDUSTRY, INDEX_TRADE,
			INDEX_DEFINE, INDEX_REGIST, INDEX_ESTABLISH_TIME, INDEX_REGIST_ADDR, INDEX_TYPE, INDEX_REPRESENTATIVE,
			INDEX_PROCESS_STYLE, INDEX_CRAFT, INDEX_SERVICE_AREA, INDEX_EMPLOYEE, INDEX_RDC_EMPLOYEE,
			INDEX_FACTORY_AREA, INDEX_SALE_AREA, INDEX_SALE_CUSTOMER, INDEX_MONTH_PRODUCE, INDEX_YEAR_PRODUCE,
			INDEX_HOMEPAGE };
	/**
	 * 各个字段名称
	 */
	public static final String[] COM_INDEX_FILEDS = new String[] { "主营产品或服务", "主营行业", "经营模式", " 是否提供加工/定制", "注册资本",
			"公司成立时间", "公司注册地", "企业类型", "法定代表人", " 加工方式", "工艺", "服务领域", "员工人数", "研发部门人数", "厂房面积", "主要销售区域 ", "主要客户群体",
			"月产量", "年营业额", "公司主页" };
	// ////////////////////////////////////////////////////////
	/** 公司名称 */
	public String companyName;
	/** 公司介绍 */
	private String introduce;
	/** 公司主页 */
	private String homepage;
	/** 公司商标、标志性图片 */
	private String iconLink;
	/** 公司介绍图片（如大门图片） */
	private String comPicLink;
	/** 其余详细信息 */
	private Map<String, String> details = new HashMap<String, String>();

	public String getCompanyName() {
		return StringUtils.isBlank(companyName) ? " " : companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIntroduce() {
		return StringUtils.isBlank(introduce) ? " " : introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIconLink() {
		return iconLink;
	}

	public void setIconLink(String iconLink) {
		this.iconLink = iconLink;
	}

	public String getComPicLink() {
		return comPicLink;
	}

	public void setComPicLink(String comPicLink) {
		this.comPicLink = comPicLink;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public void putDetails(String fieldName, String fieldValue) {
		details.put(fieldName, fieldValue);
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepage() {
		return homepage;
	}

	/** 获取超链接形式的主页网址。 */
	public String getHomepageHref() {
		homepage = StringUtils.trimToEmpty(homepage);
		if (homepage.length() == 0) {
			return homepage;
		}
		StringBuilder sb = new StringBuilder();
		for (String href : homepage.split(",")) {
			sb.append("<a href='");
			sb.append(href);
			sb.append("'>");
			sb.append(href);
			sb.append("</a>, ");
		}
		return sb.substring(0, sb.length() - 1);
	}
}
