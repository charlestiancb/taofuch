package com.cloudtech.ebusi.crawler.parser;

/**
 * 对Html页面进行解析读取操作的一些解析功能。
 * 
 * @author taofucheng
 * 
 */
public abstract class Parser {
	/**
	 * 解析页面内容
	 * 
	 * @param pageContent
	 */
	public abstract void doParse(String pageContent);
}
