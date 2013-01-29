package com.tfc.data.access.entity;

/**
 * 数组形式的格式。
 * 
 * @author taofucheng
 * 
 */
public class ArrayFormatData {
	private static final String prefix = "array";
	private String instanceName;
	private long len;

	public ArrayFormatData(String instanceName, long len) {
		this.instanceName = instanceName;
		this.len = len;
	}

}
