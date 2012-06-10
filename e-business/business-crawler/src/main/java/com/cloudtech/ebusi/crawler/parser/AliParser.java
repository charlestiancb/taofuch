package com.cloudtech.ebusi.crawler.parser;

import java.util.List;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Url;

/**
 * 解析阿里的公司信息的数据，并将其分字段进行建立相应的索引。
 * 
 * @author taofucheng
 * 
 */
public class AliParser extends Parser {

	@Override
	public boolean followUrl(Url url) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void doParse(Page page) {
		// 得到所有的链接，然后得到所有的链接内容，然后解析每个详细页面中的内容！
		List<String> links = page.getLinks();
		if (links != null && !links.isEmpty()) {
			for (String link : links) {
				if (link.startsWith("http://detail.china.alibaba.com/buyer/offerdetail")) {
					// 这个页面是每个公司的主页信息。其中包含许多其它信息！
					// TODO
					System.err.println(link);
				}
			}
		}
	}

}
