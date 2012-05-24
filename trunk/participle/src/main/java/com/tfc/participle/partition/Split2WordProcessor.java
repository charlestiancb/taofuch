package com.tfc.participle.partition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tfc.analysis.entity.Keyword;
import com.tfc.analysis.fragment.AbstractFragment;
import com.tfc.analysis.process.Processor;
import com.tfc.analysis.utils.AnalysisUtils;
import com.tfc.participle.partition.entry.WordElement;

public class Split2WordProcessor implements Processor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<WordElement> process(Map<String, Map> wordsTree, String text, AbstractFragment fragment, int minLen) {
		if (StringUtils.isBlank(text)) {
			return null;
		}
		List<WordElement> words = new ArrayList<WordElement>();
		String pre = null;// 词的前面一个字
		while (true) {
			if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isEmpty(text)) {
				return words;
			}
			if (text.length() < minLen) {
				return words;
			}
			String chr = text.substring(0, 1);
			text = text.substring(1);
			Map<String, Map> nextWord = wordsTree.get(chr);
			if (nextWord == null) {
				// 没有对应的下一个字，表示这不是关键词的开头，进行下一个循环
				pre = chr;
				words.add(new WordElement(chr, WordElement.CIXING_UNKNOW));
				continue;
			} else {
				Keyword w = AnalysisUtils.getSensitiveWord(chr, pre, nextWord, text);
				if (w == null) {
					// 开头没有关键词，下一个循环
					pre = chr;
					words.add(new WordElement(chr, WordElement.CIXING_UNKNOW));
					continue;
				} else {
					words.add(new WordElement(w.getWord(), w.getStringProp(WordElement.CIXING_KEY)));
					text = text.substring(w.getWordLength() - 1);
					pre = w.getWord().substring(w.getWordLength() - 1, w.getWordLength());
					// 跳过当前的词，进行下一个循环
					continue;
				}
			}
		}
	}
}
