package com.cloudtech.ebusi.crawler;

import net.vidageek.crawler.PageCrawler;

import com.cloudtech.ebusi.crawler.parser.AliParser;

public class AliCrawler {
	public static void main(String[] args) {
		String baseUrl = "http://search.china.alibaba.com/selloffer/--1046622.html?showStyle=img&sortType=booked&descendOrder=true&filt=y&lessThanQuantityBegin=true&cleanCookie=false";
		PageCrawler crawler = new PageCrawler(baseUrl);
		crawler.crawl(new WebListVisitor(baseUrl, new AliParser(null)));
	}
}
