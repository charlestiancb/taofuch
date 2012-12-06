package com.tfc.webtest.content;

import org.apache.commons.lang.StringUtils;

/**
 * 函数
 * 
 * @author taofucheng
 * 
 */
public abstract class FunctionContent implements Content {
	/** 函数定义的前缀 */
	public static final String FUNC_PRE = "#func:";

	public boolean isBelongTo(String content) {
		if (StringUtils.isBlank(content)) {
			return false;
		} else {
			content = StringUtils.trim(content);
			return content.startsWith(FUNC_PRE + getFuncName() + "(") && content.endsWith(")");
		}
	}

	/**
	 * 返回当前函数的名称
	 * 
	 * @return
	 */
	public abstract String getFuncName();

}
