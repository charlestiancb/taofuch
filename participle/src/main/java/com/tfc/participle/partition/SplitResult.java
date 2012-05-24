package com.tfc.participle.partition;

import java.util.List;

import com.tfc.participle.partition.entry.WordElement;

/**
 * 一句话的分词结果。其中包含一句话中所有的词以及不能分词的字
 * 
 * @author taofucheng
 * 
 */
public class SplitResult {
	private List<WordElement> words;
	private StringBuilder structs;

	public SplitResult(List<WordElement> words) {
		setWords(words);
		structs = null;
	}

	/**
	 * 是否需要继续细分分词
	 * 
	 * @return
	 */
	public boolean needAgain() {
		// TODO 完成逻辑
		return false;
	}

	/**
	 * 获取当前结果的句型
	 * 
	 * @return
	 */
	public String getStructure() {
		if (structs == null) {
			structs = new StringBuilder();
			if (words != null && !words.isEmpty()) {
				for (int i = 0; i < words.size(); i++) {
					WordElement we = words.get(i);
					structs.append(we.getCixing());
					if (i < words.size() - 1) {
						// 最后一个不用加空格
						structs.append(" ");
					}
				}
			}
		}
		return structs.toString();
	}

	private void setWords(List<WordElement> words) {
		this.words = words;
	}

	public List<WordElement> getWords() {
		return words;
	}
}
