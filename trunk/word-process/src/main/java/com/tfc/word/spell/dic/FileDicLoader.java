/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.word.spell.dic;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.tfc.word.spell.convert.CnToSpellHelper;
import com.tfc.word.utils.ClasspathUtils;

/**
 * 文本词典的加载器。
 * 
 * @author taofucheng
 */
public class FileDicLoader {
	/**
	 * 加载词典中的对应关系到Map中，词与关系之间用“|”分隔！方式为：classpath:/表示类路径下，/表示webroot路径下
	 * 
	 * @param filePath
	 *            文件名称。目前只支持classpath下的路径！对应关系用|分隔
	 * @return
	 */
	public static LinkedHashMap<String, String> loadToMap(String filePath, String encoding) {
		return convertToMap(filePath, encoding, false);
	}

	/**
	 * 将文件中的 <b>ascii对应拼音 </b> 转换成 <b>汉字对应拼音 </b>。对应关系用|分隔。例如：“215-214|zi” ->
	 * “字|zi”
	 * 
	 * @param filePath
	 * @return
	 */
	public static LinkedHashMap<String, String> convertAsciiToCn(String filePath, String encoding) {
		return convertToMap(filePath, encoding, true);
	}

	public static LinkedHashMap<String, String> loadToMap(InputStream is, String encoding) {
		try {
			return loadToMap(is, encoding, false);
		} catch (IOException e) {
			return null;
		}
	}

	private static LinkedHashMap<String, String> convertToMap(String filePath, String encoding, boolean convert2Cn) {
		filePath = StringUtils.trim(filePath);
		if (StringUtils.isEmpty(filePath)) {
			return null;
		}
		if (filePath.toLowerCase().startsWith("classpath:")) {
			filePath = filePath.substring("classpath:".length());
			filePath = filePath.startsWith("/") ? filePath : "/" + filePath;
			try {
				InputStream is = ClasspathUtils.loadFromClassPath(filePath);
				return loadToMap(is, encoding, convert2Cn);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static LinkedHashMap<String, String> loadToMap(InputStream is, String encoding, boolean convert2Cn) throws IOException {
		List<String> lines = IOUtils.readLines(is, encoding);
		if (lines != null && !lines.isEmpty()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (String line : lines) {
				if (line.startsWith("//") || StringUtils.isBlank(line)) {
					continue;
				} else {
					String[] ls = line.split("\\|", 2);
					String key = convert2Cn ? CnToSpellHelper.parseToCn(ls[0]) : ls[0];
					if (ls.length == 1) {
						map.put(key, "");
					} else {
						map.put(key, StringUtils.trim(ls[1]));
					}
				}
			}
			return map;
		}
		return null;
	}
}
