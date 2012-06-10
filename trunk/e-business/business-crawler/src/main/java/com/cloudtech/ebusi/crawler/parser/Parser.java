package com.cloudtech.ebusi.crawler.parser;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

/**
 * 对Html页面进行解析读取操作的一些解析功能。
 * 
 * @author taofucheng
 * 
 */
public abstract class Parser {
	/**
	 * 判断这个URL链接是否是应该进行操作的内容。
	 * 
	 * @param url
	 *            当前预操作的url链接。
	 * @return
	 */
	public abstract boolean followUrl(final Url url);

	/**
	 * 当读取页面时发生错误而无法正常读取时，进行相应的处理。
	 * 
	 * @param url
	 * @param statusError
	 */
	public void parseError(Url url, Status statusError) {
		// TODO 错误时的处理方式，记录已经瓟取过的页面！;
	}

	/**
	 * 对读取的页面进行处理。比如：是一个列表页面。
	 * 
	 * @param page
	 */
	public abstract void doParse(Page page);
}
