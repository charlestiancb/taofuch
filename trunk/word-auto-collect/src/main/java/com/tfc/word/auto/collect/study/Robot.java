package com.tfc.word.auto.collect.study;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tfc.word.auto.collect.study.service.WordAnalyzerService;

public class Robot implements Runnable {
	private String startUrl;
	private WordAnalyzerService wordService;

	public Robot(String startUrl) {
		this.startUrl = startUrl;
	}

	public void run() {
		parse(startUrl);
		// 抓取链接中的内容，然后将抓取的内容交给WordAnalyzerService进行处理。再抓取其中的一个，其过程就像蜘蛛。
	}

	private void parse(String startUrl2) {
		try {
			// TODO 如何保证读取过的链接不再读取？
			Document doc = Jsoup.connect(startUrl2).get();
			String text = doc.text();
			wordService.analyzer(text);
			Elements eles = doc.getElementsByTag("a");
			if (eles != null && eles.size() > 0) {
				for (int i = 0; i < eles.size(); i++) {
					Element ele = eles.get(i);
					String url = StringUtils.trim(ele.attr("href"));
					if (StringUtils.isNotEmpty(url)) {
						parse(url);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
