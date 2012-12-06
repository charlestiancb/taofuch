package com.tfc.webtest.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 正则表达式工具
 * 
 * @author taofucheng
 * 
 */
public class RegUtils {
	/***
	 * 将指定的内容中的变量使用params中的变量内容替换。<br>
	 * <font color="red">注意：如果文本中对应的变量不存在于params中，则不替换！</font>
	 * 
	 * @param contentWithVar
	 *            含有变量的文本
	 * @param params
	 *            变量的值
	 * @return
	 */
	public static String replace(String contentWithVar, Map<String, Object> params) {
		if (StringUtils.isBlank(contentWithVar)) {
			return contentWithVar;
		}
		Matcher m = Pattern.compile("$\\{(.+)\\}").matcher(contentWithVar);
		int found = m.groupCount();
		if (found == 0) {
			return contentWithVar;
		}
		for (int i = 0; i < found; i++) {
			String var = m.group(i);
			String value = (String) params.get(var);
			if (value == null) {
				continue;
			} else {
				contentWithVar = contentWithVar.replace("${" + var + "}", value);
			}
		}
		return null;
	}
}
