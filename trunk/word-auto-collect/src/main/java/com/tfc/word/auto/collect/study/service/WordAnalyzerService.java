package com.tfc.word.auto.collect.study.service;

import java.util.ArrayList;
import java.util.List;

import com.tfc.analysis.KWSeeker;
import com.tfc.analysis.entity.Keyword;
import com.tfc.analysis.fragment.AbstractFragment;
import com.tfc.word.auto.collect.config.Configuration;
import com.tfc.word.auto.collect.repository.entity.WordBase;

/**
 * 词语分析的服务入口
 * 
 * @author taofucheng
 * 
 */
public class WordAnalyzerService {
	private KWSeeker seeker = null;

	/**
	 * 对给定的文件进行分析。
	 * 
	 * @param text
	 */
	public void analyzer(String text) {
		// 纯的数字、英文、符号，都不作为词
		// 操作步骤：先对整个内容进行分词。将非符号分隔的所有单字拼接成词。检查这些拼接成的词在数据库中有没有，如果没有，添加到数据库中，如果有，增加一次统计次数（达到数量之后，自动审核通过）。
		initSeeker();
		seeker.highlight(text, fragment);
	}

	private void initSeeker() {
		if (seeker == null) {
			List<Keyword> words = new ArrayList<Keyword>();
			List<WordBase> ws = Configuration.repo.getAllCheckedWords();
			if (ws != null && ws.size() > 0) {
				for (WordBase wb : ws) {
					words.add(new Keyword(wb.getWord()));
				}
			}
			seeker = KWSeeker.getInstance(words);
		}
	}

	private static class ReplaceFragment extends AbstractFragment {

	}
}
