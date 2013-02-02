/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.tfc.word.spell.convert.CnToSpellHelper;
import com.tfc.word.spell.convert.SingleCnToSpell;
import com.tfc.word.spell.convert.WordMapper;
import com.tfc.word.spell.dic.PhraseCnSpellDic;
import com.tfc.word.spell.dic.SpellToCharsDic;

/**
 * 汉字与拼音之间的转换接口！
 * 
 * @author taofucheng
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PingyinAPI {
	/** 空的字符串数组 */
	private static final String[] EMPTY = new String[] {};
	/** 字母正则 */
	private static final String ASCII_LETTER = "\\w+";
	/** 字母匹配 */
	private static final Pattern ASCII_LETTER_PATTERN = Pattern.compile(ASCII_LETTER, Pattern.CASE_INSENSITIVE);
	/** 转换之后的最大内容 */
	private static final int MAX_RESULT = 15;

	/**
	 * 将给定的文本中的汉字转换为拼音首字母。其它字符不变！
	 * 
	 * @param cnText
	 * @return
	 */
	public static String[] toFirstLetter(String cnText) {
		if (StringUtils.isBlank(cnText)) {
			return new String[] {};
		}
		String[] py = toPingyin(cnText);
		String[] result = new String[] {};
		if (py != null && py.length > 1) {
			// 先取正常拼音，如果结果有多个，则使用单字实现。
			while (cnText.length() > 0) {
				String ch = cnText.substring(0, 1);
				cnText = cnText.substring(1);
				String[] tmp = SingleCnToSpell.getFullSpell(ch).split(",");
				for (int i = 0; i < tmp.length; i++) {
					tmp[i] = tmp[i].substring(0, 1);// 取首字母
				}
				result = CnToSpellHelper.interbreed(result, tmp);
			}
		} else {
			// 如果只有一个，则将这个结果进行首字母处理
			while (cnText.length() > 0) {
				String ch = cnText.substring(0, 1);
				cnText = cnText.substring(1);
				String[] tmp = SingleCnToSpell.getFullSpell(ch).split(",");
				for (int i = 0; i < tmp.length; i++) {
					tmp[i] = " " + tmp[i];// 每个字的拼音之间使用空格区分
				}
				result = CnToSpellHelper.interbreed(result, tmp);
			}
			// 判断哪个拼音才是与结果的拼音接近
			for (String p : result) {
				if (p.replaceAll(" ", "").equals(py[0])) {
					StringBuffer sb = new StringBuffer("");
					for (String s : p.split(" ")) {
						if (StringUtils.isNotBlank(s)) {
							sb.append(s.substring(0, 1));
						}
					}
					return new String[] { sb.toString() };
				}
			}
		}
		// 去除重复
		List<String> tmp = new ArrayList<String>();
		for (String t : result) {
			if (!tmp.contains(t)) {
				tmp.add(t);
			}
		}
		return tmp.toArray(result);
	}

	/**
	 * 将给定的文本中的汉字转换为拼音。
	 * 
	 * @param cnText
	 * @return
	 */
	public static String[] toPingyin(String cnText) {
		if (StringUtils.isBlank(cnText)) {
			return new String[] { cnText };
		}
		// 需要将cnText中的干扰字符过滤成空格！
		Map<String, Map> wordsTree = PhraseCnSpellDic.phraseTree();
		String[] result = new String[] {};
		while (cnText.length() > 0) {
			if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isBlank(cnText)) {
				return new String[] { cnText };
			}
			String chr = cnText.substring(0, 1);
			String chrAscii = SingleCnToSpell.getCnAscii(chr.charAt(0));
			cnText = cnText.substring(1);
			Map<String, Map> nextWord = wordsTree.get(chrAscii.toLowerCase());// 以统一化的小写方式去查找
			if (nextWord == null) {
				result = CnToSpellHelper.interbreed(result, SingleCnToSpell.getFullSpell(chr).split(","));
				continue;
			} else {
				WordMapper ws = CnToSpellHelper.getWordSpell(chr, nextWord, cnText, true);
				if (ws == null) {
					// 开头没有敏感词，下一个循环
					result = CnToSpellHelper.interbreed(result, SingleCnToSpell.getFullSpell(chr).split(","));
					continue;
				} else {
					result = CnToSpellHelper.interbreed(result, ws.getValues());
					cnText = cnText.substring(ws.getKey().length() - 1);
					// 跳过当前的词，进行下一个循环
					continue;
				}
			}
		}
		return result;
	}

	/**
	 * 将给定的拼音转换为汉字！
	 * 
	 * @param pinyin
	 * @return
	 */
	public static String[] toCn(String pinyin) {
		if (StringUtils.isBlank(pinyin)) {
			return new String[] { pinyin };
		}
		// 需要将cnText中的干扰字符过滤成空格！
		Map<String, Map> wordsTree = SpellToCharsDic.spellTree();
		String[] result = new String[] {};
		while (pinyin.length() > 0) {
			if (wordsTree == null || wordsTree.isEmpty() || StringUtils.isBlank(pinyin)) {
				return new String[] { pinyin };
			}
			String chr = pinyin.substring(0, 1);
			pinyin = pinyin.substring(1);
			if (StopCharacter.isStopChar(chr)) {
				result = CnToSpellHelper.appendToArray(chr, result);
				continue;
			}
			Map<String, Map> nextWord = wordsTree.get(chr.toLowerCase());// 以统一化的小写方式去查找
			if (nextWord == null) {
				result = CnToSpellHelper.appendToArray(chr, result);
				continue;
			} else {
				WordMapper ws = CnToSpellHelper.getWordSpell(chr, nextWord, pinyin, false);
				if (ws == null) {
					// 开头没有词，下一个循环
					result = CnToSpellHelper.appendToArray(chr, result);
					continue;
				} else {
					result = CnToSpellHelper.interbreed(result, ws.getValues());
					pinyin = pinyin.substring(ws.getKey().length() - 1);
					// 跳过当前的词，进行下一个循环
					continue;
				}
			}
		}
		return result;
	}

	/**
	 * 找出给定的词相似拼音的所有词（返回的词中不包含传入的词）。<br>
	 * 这里面有两层循环，但是一般情况下，查找之后数据量很小，因此这两层循环没有什么性能上的影响。<br>
	 * <font color='red'>注意：这个方法尽量不要经常使用，因为这个方法的性能不是很好！</font>
	 * 
	 * @param word
	 * @return
	 */
	public static String[] getSamePinyinWords(String word) {
		// 将词转换为拼音，然后到拼音词库中找词，如果没有，则返回null。
		word = StringUtils.trim(word);
		if (StringUtils.isEmpty(word)) {
			return EMPTY;
		}
		String pureWord = word.replaceAll(ASCII_LETTER, "");// 这是去除了字母之后的内容
		String[] pinyins = toPingyin(word);
		if (pinyins != null && pinyins.length > 0) {
			Set<String> result = new LinkedHashSet<String>();
			for (String pinyin : pinyins) {
				String[] words = toCn(pinyin);
				if (words != null && words.length > 0) {
					for (String w : words) {
						if (result.size() >= MAX_RESULT) {
							break;
						} else if (StringUtils.isEmpty(w) || word.equals(w.trim())) {// 将传入的词本身去除
							continue;
						} else if (pureWord.length() != word.length() || w.trim().length() != word.length()) {// 如果输入的是拼音与汉字混合或转换后的词与输入的词长度不一样，则continue
							continue;
						} else {
							result.add(w);
						}
					}
				}
			}
			if (pureWord.length() != word.length()) {// 如果输入的词是汉字与拼音混合的，则将拼音部分保留！
				String unLetterWord = word.replaceAll(ASCII_LETTER, "|");// 这是去除了字母之后的内容
				List<String> lettersWords = new ArrayList<String>();
				for (Matcher m = ASCII_LETTER_PATTERN.matcher(word); m.find(); lettersWords.add(m.group(0)))
					;
				pinyins = toPingyin(unLetterWord);
				if (pinyins != null && pinyins.length > 0) {
					Set<String> result_ = new HashSet<String>();
					for (String pinyin : pinyins) {
						String[] words = toCn(pinyin);
						if (words != null && words.length > 0) {
							for (String w : words) {
								if (StringUtils.isEmpty(w) || unLetterWord.equals(w.trim())
										|| w.trim().length() != unLetterWord.length()) {// 将传入的词本身去除
									continue;
								} else {
									result_.add(w);
								}
							}
						}
					}
					if (!result_.isEmpty()) {
						for (String w : result_) {
							if (result.size() >= MAX_RESULT) {
								break;
							}
							StringBuffer sb = new StringBuffer();
							String[] tmp = w.split("\\|");
							for (int i = 0; i < tmp.length; i++) {
								String _w = tmp[i];
								sb.append(_w);
								if (i < tmp.length && i < lettersWords.size()) {
									sb.append(lettersWords.get(i));
								}
							}
							result.add(sb.toString());
						}
					}
				}
			}
			return result.toArray(EMPTY);
		}
		return null;
	}
}
