package org.ictclas4j.bean.demo;

import java.util.List;

import org.ictclas4j.segment.SegTag;

public class Main {
	private static final SegTag st = new SegTag(1, false, false);

	public static void main(String[] args) {
		List<String> ws = st.split("这是一个分词器，这是处理中文信息的情况").getWordList();
		System.err.println(ws);
	}
}
