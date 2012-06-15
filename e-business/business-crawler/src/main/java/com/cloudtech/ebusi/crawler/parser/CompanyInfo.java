package com.cloudtech.ebusi.crawler.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * 抓取的公司信息！
 * 
 * @author taofucheng
 * 
 */
public class CompanyInfo {
	/** 公司名称 */
	public String companyName;
	/** 公司介绍 */
	private String introduce;
	/** 公司主营产品 */
	private String products;
	/** 主营行业 */
	private String industry;
	/** 公司商标、标志性图片 */
	private String iconLink;
	/** 公司介绍图片（如大门图片） */
	private String comPicLink;
	/** 其余详细信息 */
	private Map<String, String> details = new HashMap<String, String>();

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
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
}
