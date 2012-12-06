package com.tfc.webtest.constants;

import org.apache.commons.lang.StringUtils;

/**
 * 操作类型定义。都是小写！其中的value值与数据库中的定义保持一致（数据库中是在界面上显示给用户看的）。
 * 
 * @author taofucheng
 * 
 */
public enum OperType {
	/** 点击操作 */
	click(1),
	/** 双击 */
	double_click(2);
	private int value;

	OperType(int value) {
		this.value = value;
	}

	/**
	 * 获取操作的值，如：click会返回1
	 * 
	 * @return
	 */
	public int value() {
		return value;
	}

	/**
	 * 根据操作名称获取对应的类型
	 * 
	 * @param name
	 * @return
	 */
	public static OperType getByName(String name) {
		name = StringUtils.trim(name);
		if (name == null || name.isEmpty()) {
			return null;
		}
		return OperType.valueOf(name.toLowerCase());
	}

	/**
	 * 根据操作类型的值，获取对应的操作
	 * 
	 * @param value
	 * @return
	 */
	public static OperType getByValue(int value) {
		OperType[] types = OperType.values();
		if (types != null && types.length > 0) {
			for (OperType t : types) {
				if (t.value() == value) {
					return t;
				}
			}
		}
		return null;
	}
}
