/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.dic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tfc.word.spell.convert.CnToSpellHelper;

/**
 * 拼音对应的汉字及词语词典
 * 
 * @author taofucheng
 */
@SuppressWarnings("rawtypes")
public class SpellToCharsDic {
	/** 拼音对应的字或词，如果一个词对应有多个拼音，则用“,”隔开。词的每个字转换为123-110这样的方式 */
	private static Map<String, Map> spellTree = null;

	static {
		if (spellTree == null) {
			spellTree = new HashMap<String, Map>();
			initialize();
		}
	}

	private SpellToCharsDic() {
	}

	/**
	 * 拼音对应的字或词
	 * 
	 * @return
	 */
	public static Map<String, Map> spellTree() {
		return spellTree;
	}

	private static void putPhrase(LinkedHashMap<String, String> cnToSpell) {
		if (cnToSpell != null && !cnToSpell.isEmpty()) {
			for (String cn : cnToSpell.keySet()) {
				CnToSpellHelper.pushSpellToTree(spellTree, cn, cnToSpell.get(cn));
			}
		}
	}

	private static void initialize() {
		// 加载两个词语词典
		putPhrase(FileDicLoader.loadToMap("classpath:/com/tfc/word/spell/dic/phrase_multi.dic", "UTF-8"));
		putPhrase(FileDicLoader.loadToMap("classpath:/com/tfc/word/spell/dic/phrase_spell.dic", "UTF-8"));
		// 加载单个汉字的拼音词典
		putPhrase(FileDicLoader.convertAsciiToCn("classpath:/com/tfc/word/spell/dic/char_spell.dic", "UTF-8"));
	}
}
