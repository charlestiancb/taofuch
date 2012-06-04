package com.cloudtech.ebusi.crawler.parser;

public abstract class Parser {
	/**
	 * 解析页面内容
	 * 
	 * @param pageContent
	 */
	public abstract void doParse(String pageContent);
}
