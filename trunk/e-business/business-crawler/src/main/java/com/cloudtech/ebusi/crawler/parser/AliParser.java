package com.cloudtech.ebusi.crawler.parser;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Url;

public class AliParser extends Parser {

	@Override
	public boolean followUrl(Url url) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void doParse(Page page) {
		// 得到所有的链接，然后得到所有的链接内容，然后解析每个详细页面中的内容！
		System.err.println(page);
	}

}
