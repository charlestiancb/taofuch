package com.tfc.participle.partition.entry;

import org.apache.commons.lang.StringUtils;

/**
 * 读取文本内容。
 * 
 * @author taofucheng
 * 
 */
public class TokenEnumeration {
	/** 传入的文本内容 */
	private String content;

	public TokenEnumeration(String text) {
		content = text;
	}

	/** 下面是否还有内容 */
	public boolean hasMore() {
		return StringUtils.isNotBlank(content);
	}

	/**
	 * 取出下一个整句。
	 * 
	 * @return
	 */
	public String next() {
		// TODO 完善逻辑
		return null;
	}
}
