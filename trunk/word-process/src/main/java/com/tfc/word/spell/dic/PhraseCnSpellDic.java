/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.dic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tfc.word.spell.convert.CnToSpellHelper;

/**
 * 多音字词语汇总，即多音字词语字典！普通词语不汇总，汇总的就是多音字的组词！这样减少加载到内存中的容量！该类在使用时才会被加载！
 * 
 * @author taofucheng
 */
@SuppressWarnings("rawtypes")
public class PhraseCnSpellDic {
	/** 词对应的拼音，如果一个词对应有多个拼音，则用“,”隔开。词的每个字转换为123-110这样的方式 */
	private static Map<String, Map> phraseTree = null;

	private PhraseCnSpellDic() {
	}

	public static Map<String, Map> phraseTree() {
		if (phraseTree == null) {
			phraseTree = new HashMap<String, Map>();
			initialize();
		}
		return phraseTree;
	}

	private static void putPhrase(String phrase, String spells) {
		CnToSpellHelper.pushWordToTree(phraseTree, phrase, spells);
	}

	private static void initialize() {
		LinkedHashMap<String, String> map = FileDicLoader.loadToMap("classpath:/com/tfc/word/spell/dic/phrase_multi.dic",
																	"UTF-8");
		if (map != null && !map.isEmpty()) {
			for (String key : map.keySet()) {
				putPhrase(key, map.get(key));
			}
		}
	}
}
