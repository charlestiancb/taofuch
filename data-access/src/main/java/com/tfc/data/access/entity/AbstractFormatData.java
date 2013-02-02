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
				try {
					return JSON.parseObject(value, targetElementClass);
				} catch (Exception e) {
					return value;
				}
			}
		}
		if (targetElementClass != null && Number.class.isAssignableFrom(targetElementClass)) {
			return 0;
		}
		return null;
	}

	public static void main(String[] args) {
		int[][] x = new int[2][3];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				x[i][j] = j;
			}
		}
		System.err.println(x[0].length);
	}

}
