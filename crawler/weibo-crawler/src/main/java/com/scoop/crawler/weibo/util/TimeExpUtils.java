package com.scoop.crawler.weibo.util;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import edu.fudan.nlp.chinese.ner.TimeNormalizer;
import edu.fudan.nlp.chinese.ner.TimeUnit;

public class TimeExpUtils {
	public static void main(String[] args) {
		System.err.println(abstractTime("今天 20:14"));
	}

	/**
	 * 根据文字描述抽取出对应的时间
	 * 
	 * @param timeStr
	 * @return
	 */
	public static String abstractTime(String timeStr) {
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(TimeExpUtils.class.getResourceAsStream("/data/TimeExp.gz"))));
			Pattern p = (Pattern) in.readObject();
			in.close();
			String pattern = p.pattern();
			TimeNormalizer normalizer = new TimeNormalizer(pattern);
			TimeUnit[] units = normalizer.parse(timeStr);
			timeStr = units[0].Time_Norm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeStr;
	}
}
