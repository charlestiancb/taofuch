/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.analysis.process;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tfc.analysis.entity.Keyword;
import com.tfc.analysis.fragment.AbstractFragment;
import com.tfc.analysis.utils.AnalysisUtils;

/**
 * 对文本进行高亮处理。
 * 
 * @author taofucheng
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Highlight implements Processor {
	/**
	 * 将文本中的关键词提取出来。
	 * 
	 * @param wordsTree
	 *            关键词库树
	 * @param text
	 *            待处理的文本
	 * @return 返回提取的关键词或null
	 */
	public String process(Map<String, Map> wordsTree, String text, AbstractFragment fragment, int minLen) {
		StringBuffer result = new StringBuffer("");
		String pre = null;// 词的前面一个字
		while (true) {
			if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isEmpty(text)) {
				return result.append(text).toString();
			}
			if (text.length() < minLen) {
				return result.append(text).toString();
			}
			String chr = text.substring(0, 1);
			text = text.substring(1);
			Map<String, Map> nextWord = wordsTree.get(chr);
			if (nextWord == null) {
				// 没有对应的下一个字，表示这不是关键词的开头，进行下一个循环
				result.append(chr);
				pre = chr;
				continue;
			} else {
				Keyword w = AnalysisUtils.getSensitiveWord(chr, pre, nextWord, text);
				if (w == null) {
					// 开头没有关键词，下一个循环
					result.append(chr);
					pre = chr;
					continue;
				} else {
					result.append(fragment.format(w));
					text = text.substring(w.getWordLength() - 1);
					pre = w.getWord().substring(w.getWordLength() - 1, w.getWordLength());
					// 跳过当前的词，进行下一个循环
					continue;
				}
			}
		}
	}

}
