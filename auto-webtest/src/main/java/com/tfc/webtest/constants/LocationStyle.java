package com.tfc.webtest.constants;

import org.apache.commons.lang.StringUtils;

/**
 * 定位方式！都是小写！其中的value值与数据库中的定义保持一致（数据库中是在界面上显示给用户看的）
 * 
 * @author taofucheng
 * 
 */
public enum LocationStyle {
	id(1), name(2), css(3), tag_name(4), class_name(5), xpath(6), link_text(7);
	private int value;

	LocationStyle(int value) {
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
	public static LocationStyle getByName(String name) {
		name = StringUtils.trim(name);
		if (name == null || name.isEmpty()) {
			return null;
		}
		return LocationStyle.valueOf(name.toLowerCase());
	}

	/**
	 * 根据操作类型的值，获取对应的操作
	 * 
	 * @param value
	 * @return
	 */
	public static LocationStyle getByValue(int value) {
		LocationStyle[] styles = LocationStyle.values();
		if (styles != null && styles.length > 0) {
			for (LocationStyle t : styles) {
				if (t.value() == value) {
					return t;
				}
			}
		}
		return null;
	}
}
