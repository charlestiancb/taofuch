package org.ictclas4j.bean.demo;

import java.util.List;

import org.ictclas4j.segment.SegTag;

public class Main {
	private static final SegTag st = new SegTag(1, false, false);

	public static void main(String[] args) {
		List<String> ws = st.split("����һ���ִ��������Ǵ���������Ϣ�����").getWordList();
		System.err.println(ws);
	}
}
