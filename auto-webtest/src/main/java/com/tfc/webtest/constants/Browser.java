package com.tfc.webtest.constants;

/**
 * 浏览器类型
 * 
 * @author taofucheng
 * 
 */
public enum Browser {
	ie6(1), ie7(2), ie8(3), ie9(4), firefox(5), chrome(6);
	private int value;

	Browser(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
