package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;

/**
 * 抽象的格式。主要为具体的格式提供一些公共的方法。
 * 
 * @author taofucheng
 * 
 */
public class AbstractFormatData {

	protected Object parseToObject(Class<?> targetElementClass, String value) {
		if (value != null) {
			if (value.startsWith("{") && value.endsWith("}")) {
				return JSON.parseObject(value, targetElementClass);
			} else if (value.startsWith("[") && value.endsWith("]")) {
				return JSON.parseArray(value, targetElementClass);
			} else {
				return value;
			}
		}
		return null;
	}

}
