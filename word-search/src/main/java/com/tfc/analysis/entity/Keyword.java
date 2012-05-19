/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.analysis.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 在文本中搜索到的关键词。
 * 
 * @author taofucheng
 */
public class Keyword implements Serializable {
	private static final long serialVersionUID = -5441706739810114609L;
	/** 本类中props属性对外的名称 */
	public static final String propsName = "props";
	/** 关键词内容 */
	private String word;
	private int wordLength = 0;
	/** 所有属性 */
	private Map<String, Object> props = new HashMap<String, Object>();

	/**
	 * 无参数构造函数
	 */
	public Keyword(String word) {
		setWord(word);
	}

	public String getWord() {
		return StringUtils.trimToEmpty(word);
	}

	public void setWord(String word) {
		this.word = word;
		this.wordLength = word.length();
	}

	public int getWordLength() {
		return wordLength;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[word=");
		sb.append(getWord());
		sb.append("],");
		sb.append("[props=");
		sb.append(props);
		sb.append("]");
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Keyword) {
			Keyword k = (Keyword) obj;
			if (k.toString().equals(this.toString())) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	/**
	 * 向属性信息中增加描述
	 * 
	 * @param mapword
	 * @param string
	 */
	public void putProp(String key, String value) {
		props.put(key, value);
	}

	public String getStringProp(String key) {
		return (String) props.get(key);
	}

	public Map<String, Object> getAllProps() {
		return props;
	}
}
