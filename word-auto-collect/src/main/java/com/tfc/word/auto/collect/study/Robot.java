package com.tfc.word.auto.collect.study;

public class Robot implements Runnable {
	private String startUrl;

	public Robot(String startUrl) {
		this.startUrl = startUrl;
	}

	public void run() {
		// TODO Auto-generated method stub
		// 抓取链接中的内容，然后将抓取的内容交给WordAnalyzerService进行处理。再抓取其中的一个，其过程就像蜘蛛。
	}
}
