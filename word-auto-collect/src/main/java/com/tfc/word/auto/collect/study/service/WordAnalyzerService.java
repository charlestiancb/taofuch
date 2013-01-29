package com.tfc.word.auto.collect.study.service;

/**
 * 词语分析的服务入口
 * 
 * @author taofucheng
 * 
 */
public class WordAnalyzerService {
	/**
	 * 对给定的文件进行分析。
	 * 
	 * @param text
	 */
	public void analyzer(String text) {
		// 数字、英文、符号，都不作为词
		// 操作步骤：先对整个内容进行分词。将非符号分隔的所有单字拼接成词。检查这些拼接成的词在数据库中有没有，如果没有，添加到数据库中，如果有，增加一次统计次数（达到数量之后，自动审核通过）。
	}
}
