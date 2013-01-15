/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.convert;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.tfc.word.spell.StopCharacter;

/**
 * 帮助类
 * 
 * @author taofucheng
 */
public class CnToSpellHelper {
	private static final String[] EMPTY = new String[] {};

	/**
	 * 将arr1与arr2以前者为优先进行杂交算法，得出杂交后的数组。如果两者都为空，则直接返回空数组；如果其中一个为空，则直接返回另一个；
	 * 如果两都都不为空，则进行杂交算法。
	 * 
	 * @param arr1
	 *            父因子
	 * @param arr2
	 *            母因子
	 * @return 杂交后的孩子数组
	 */
	public static String[] interbreed(String[] arr1, String[] arr2) {
		if ((arr1 == null || arr1.length == 0) && (arr2 == null || arr2.length == 0)) {
			return EMPTY;
		}
		if (arr1 == null || arr1.length == 0) {
			return arr2;
		}
		if (arr2 == null || arr2.length == 0) {
			return arr1;
		}
		if (arr1.length == 1 && arr2.length == 1) {
			return new String[] { arr1[0] + arr2[0] };
		}
		Set<String> result = new HashSet<String>();
		for (String t1 : arr1) {
			for (String t2 : arr2) {
				result.add(t1 + t2);
			}
		}
		return result.toArray(EMPTY);
	}

	/**
	 * 将指定的字符追加到数组中的每个元素中！
	 * 
	 * @param append
	 * @param arr
	 * @return
	 */
	public static String[] appendToArray(String append, String[] arr) {
		if (StringUtils.isEmpty(append)) {
			return arr;
		}
		if (arr == null || arr.length == 0) {
			return new String[] { append };
		}
		for (int i = 0; i < arr.length; i++) {
			arr[i] = arr[i] + append;
		}
		return arr;
	}

	/**
	 * 将指定的字符追加到数组中的每个元素中！
	 * 
	 * @param append
	 * @param arr
	 * @return
	 */
	public static String[] appendToArray(char c, String[] result) {
		return appendToArray(String.valueOf(c), result);
	}

	/**
	 * 用英文逗号将数组内容拼接。如果提供的数组为空或不存在，则直接返回""。
	 * 
	 * @param arr
	 * @return
	 */
	public static String toString(String... arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer("");
		for (String a : arr) {
			sb.append(a);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 将指定的词加入到给定的树中！规则：<br>
	 * 1、将符号等无意义的词变成空格；<br>
	 * 2、将每个字转换为ascii码存放，如果是字母类的，则不转换直接存放
	 * 
	 * @param phraseTree
	 *            词树
	 * @param phrase
	 *            词或字
	 * @param spells
	 *            对应的拼音
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Map> pushWordToTree(Map<String, Map> phraseTree, String phrase, String spells) {
		phrase = StringUtils.trimToEmpty(phrase).toLowerCase();// 将所有字母转换为小写，统一格式，便于处理
		spells = StringUtils.trimToEmpty(spells).toLowerCase();// 将所有字母转换为小写，统一格式，便于处理
		if (phraseTree == null || StringUtils.isBlank(spells)) {
			return phraseTree;
		}
		if (StringUtils.isEmpty(phrase)) {
			phraseTree.put(StopCharacter.TREE_END_TAG, getSpellMap(phraseTree.get(StopCharacter.TREE_END_TAG), spells));
			return phraseTree;
		}
		String next = SingleCnToSpell.getCnAscii(phrase.charAt(0));
		Map w = phraseTree.get(next);
		if (w == null) {
			w = new HashMap<String, Map>();
		}
		phraseTree.put(next, pushWordToTree(w, phrase.substring(1), spells));
		return phraseTree;
	}

	/**
	 * 将拼音加入到树中（特殊词处理成空格！）
	 * 
	 * @param spellTree
	 * @param word
	 * @param spells
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Map> pushSpellToTree(Map<String, Map> spellTree, String word, String spells) {
		word = StringUtils.trimToEmpty(word).toLowerCase();// 将所有字母转换为小写，统一格式，便于处理
		spells = StringUtils.trimToEmpty(spells).toLowerCase();// 将所有字母转换为小写，统一格式，便于处理
		if (spellTree == null || StringUtils.isBlank(word) || StringUtils.isBlank(spells)) {
			return spellTree;
		}
		for (String phrase : spells.split(",")) {
			if (StringUtils.isEmpty(phrase)) {
				spellTree.put(StopCharacter.TREE_END_TAG, getSpellMap(spellTree.get(StopCharacter.TREE_END_TAG), word));
				return spellTree;
			}
			String next = phrase.substring(0, 1);
			if (StopCharacter.isStopChar(next)) {
				next = " ";
			}
			Map w = spellTree.get(next);
			if (w == null) {
				w = new HashMap<String, Map>();
			}
			spellTree.put(next, pushWordToTree(w, phrase.substring(1), word));
		}
		return spellTree;
	}

	/**
	 * 将拼音加入到词树的底端，作为结束标志的描述信息。
	 * 
	 * @param content
	 * @return
	 */
	private static Map<String, String> getSpellMap(Map<String, String> origin, String content) {
		if (origin == null) {
			origin = new HashMap<String, String>();
		}
		content = StringUtils.trim(content);
		if (StringUtils.isNotEmpty(content)) {
			for (String s : content.split(",")) {
				origin.put(s, "");
			}
		}
		return origin;
	}

	/**
	 * 从文本中获取指定的词
	 * 
	 * @param chr
	 * @param nextWord
	 * @param cnText
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static WordMapper getWordSpell(String append, Map<String, Map> nextWordsTree, String text, boolean needAscii) {
		if (nextWordsTree == null || nextWordsTree.isEmpty()) {
			return null;
		}
		Map<String, Object> endTag = nextWordsTree.get(StopCharacter.TREE_END_TAG);
		if (StringUtils.isEmpty(text)) {
			if (endTag != null) {
				// 如果有结束符，则表示匹配成功
				return getKeyword(append, endTag);
			} else {
				// 没有，则返回null
				return null;
			}
		}
		String next = text.substring(0, 1);
		String realNext = needAscii ? SingleCnToSpell.getCnAscii(text.charAt(0)) : next;
		realNext = StopCharacter.isStopChar(realNext) ? " " : realNext;
		Map<String, Map> nextTree = nextWordsTree.get(realNext.toLowerCase());// 以统一的小写字母进行查找
		if (endTag == null) {
			if (nextTree != null && nextTree.size() > 0) {
				// 没有结束标志，则表示敏感词没有结束，继续往下走。
				return getWordSpell(append + next, nextTree, text.substring(1), needAscii);
			} else {
				// 如果没有下一个匹配的字，表示匹配结束！
				return null;
			}
		} else {
			WordMapper tmp = null;
			if (nextTree != null && nextTree.size() > 0) {
				// 如果大于0，则表示还有更长的词，继续往下找
				tmp = getWordSpell(append + next, nextTree, text.substring(1), true);
				if (tmp == null) {
					// 没有更长的词，则就返回这个词。在返回之前，先判断它是模糊的，还是精确的
					tmp = getKeyword(append, endTag);
				}
				return tmp;
			} else {
				// 没有往下的词了，那就是词结束了。
				return getKeyword(append, endTag);
			}
		}
	}

	/**
	 * 组装获取到的词语！
	 * 
	 * @param word
	 * @param endTag
	 * @return
	 */
	private static WordMapper getKeyword(String word, Map<String, Object> endTag) {
		WordMapper wm = new WordMapper();
		wm.setKey(word);
		wm.setValues(endTag.keySet().toArray(EMPTY));
		return wm;
	}

	/**
	 * 从ascii码形式转换为汉字。
	 * 
	 * @param ascii
	 * @return
	 */
	public static String parseToCn(String ascii) {
		ascii = StringUtils.trim(ascii);
		if (StringUtils.isEmpty(ascii) || ascii.indexOf("-") == -1) {
			return ascii;
		}
		String[] bytes = ascii.split("\\-");
		byte[] bs = new byte[2];
		bs[0] = (byte) Integer.parseInt(bytes[0]);
		bs[1] = (byte) Integer.parseInt(bytes[1]);
		try {
			return new String(bs, "GBK");
		} catch (UnsupportedEncodingException e) {
			return ascii;
		}
	}
}
