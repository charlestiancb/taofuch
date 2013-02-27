package com.tfc.word.auto.collect.study.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
	private ReplaceFragment fragment = new ReplaceFragment();

	/**
	 * 对给定的文件进行分析。
	 * 
	 * @param text
	 */
	public void analyzer(String text) {
		if (StringUtils.isBlank(text)) {
			return;
		}
		// 纯的数字、英文、符号，都不作为词
		// 操作步骤：先对整个内容进行分词。将非符号分隔的所有单字拼接成词。检查这些拼接成的词在数据库中有没有，如果没有，添加到数据库中，如果有，增加一次统计次数（达到数量之后，自动审核通过）。
		initSeeker();
		text = seeker.highlight(text, fragment);
		if (StringUtils.isBlank(text)) {
			return;
		}
		// 将接下的内容处理成一个个的词！
		List<String> words = new ArrayList<String>(Arrays.asList(text.split(ReplaceFragment.split)));
		for (String word : words) {
			word = StringUtils.trim(word);
			if (StringUtils.isBlank(word) || !isValid(word)) {
				continue;
			}
			WordBase wb = Configuration.repo.getWord(word);
			if (wb == null) {
				// 不存在的时候，保存。
				Configuration.repo.saveWord(word);
			} else {
				// 存在的要注意其数量变化。
				boolean result = Configuration.repo.updateWord(wb);
				if (result && wb.getStatNum() < Configuration.AUTO_CHECKED_NUM) {
					seeker.addWord(new Keyword(word));
				}
			}
		}
		System.out.println(words);
	}

	/**
	 * 判断是否是非词汇信息，如：是否全是数字，是否全是标点符号
	 * 
	 * @param word
	 * @return
	 */
	private boolean isValid(String word) {
		if (StringUtils.isNumeric(word)) {
			// 纯数字，非法
			return false;
		}
		if (word.trim().length() == 1) {
			// 如果是一个字符，那就是没有意义的词。
			return false;
		}
		return true;
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

	public static void main(String[] args) {
		new WordAnalyzerService().analyzer("这是一个内容测试，哈哈哈，,,，你好啊！");
	}

	private static class ReplaceFragment extends AbstractFragment {
		public static final String split = "$$$";

		@Override
		public String format(Keyword word) {
			return split;
		}

	}
}
