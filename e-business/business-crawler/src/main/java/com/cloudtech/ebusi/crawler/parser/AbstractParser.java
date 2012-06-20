package com.cloudtech.ebusi.crawler.parser;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageCrawler;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

import com.cloudtech.ebusi.crawler.WebListVisitor;
import com.cloudtech.ebusi.crawler.index.Indexer;

/**
 * 对Html页面进行解析读取操作的一些解析功能。
 * 
 * @author taofucheng
 * 
 */
public abstract class AbstractParser {
	protected Indexer indexer;

	public AbstractParser(Indexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * 返回索引器
	 * 
	 * @return
	 */
	public Indexer getIndexer() {
		return indexer;
	}

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
	public abstract void doParse(Page page, Indexer indexer);

	/**
	 * 开始抓取
	 * 
	 * @param startUrl
	 *            开始的页面
	 */
	public void doCrawler(String startUrl) {
		PageCrawler crawler = new PageCrawler(startUrl);
		crawler.crawl(new WebListVisitor(startUrl, this));
	}
}
