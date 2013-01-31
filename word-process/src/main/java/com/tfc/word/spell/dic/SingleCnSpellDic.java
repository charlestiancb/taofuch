/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.dic;

import java.util.LinkedHashMap;

/**
 * 每个汉字对应的拼音（包括多音字）
 * 
 * @author taofucheng
 */
public class SingleCnSpellDic {
	private static LinkedHashMap<String, String> spellMap = null;

	private SingleCnSpellDic() {
	}

	public static LinkedHashMap<String, String> spellMap() {
		if (spellMap == null) {
			spellMap = FileDicLoader.loadToMap("classpath:/com/tfc/word/spell/dic/char_spell.dic", "UTF-8");
			if (spellMap == null) {
				spellMap = new LinkedHashMap<String, String>(20901);
			}
		}
		return spellMap;
	}
}
