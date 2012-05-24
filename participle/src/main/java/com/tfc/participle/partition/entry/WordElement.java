package com.tfc.participle.partition.entry;

/**
 * 分出来的每个词
 * 
 * @author taofucheng
 * 
 */
public class WordElement {
	/** 未知词性 */
	public static final String CIXING_UNKNOW = "unknow";
	/** 词性key */
	public static final String CIXING_KEY = "cixing";
	/** 词内容 */
	private String text;
	/** 词性 */
	private String cixing;

	public WordElement() {
	}

	public WordElement(String text, String cixing) {
		setText(text);
		setCixing(cixing);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCixing() {
		return cixing;
	}

	public void setCixing(String cixing) {
		this.cixing = cixing;
	}
}
