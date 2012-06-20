package com.cloudtech.ebusi.crawler;

import com.cloudtech.ebusi.crawler.parser.AbstractParser;
import com.cloudtech.ebusi.crawler.parser.AliParser;
import com.cloudtech.ebusi.index.CompanyIndexer;

public class Crawler {
	public static void main(String[] args) {
		doComCrawler(new AliParser(new CompanyIndexer()));
	}

	public static void doComCrawler(AbstractParser... parsers) {
		if (parsers != null && parsers.length > 0) {
			for (final AbstractParser parser : parsers) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						parser.doCrawler("http://search.china.alibaba.com/selloffer/--1046622.html?showStyle=img&sortType=booked&descendOrder=true&filt=y&lessThanQuantityBegin=true&cleanCookie=false");
					}
				});
				t.setName(parser.getClass().getName());
				t.start();
			}
		}
	}
}
