package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.LuceneDataAccess;

/**
 * 数组形式的格式。
 * 
 * @author taofucheng
 * 
 */
public class ArrayFormatData extends AbstractFormatData {
	private static final String prefix = "array";
	private String instanceName;
	private int len;

	/**
	 * 类似这样的定义：new int[2]
	 * 
	 * @param instanceName
	 * @param len
	 */
	public ArrayFormatData(String instanceName, int len) {
		this.instanceName = instanceName + System.nanoTime();
		this.len = len;
	}

	/**
	 * 类似这样的定义：new int[]{1,2}
	 * 
	 * @param instanceName
	 * @param elements
	 */
	public ArrayFormatData(String instanceName, Object... elements) {
		this.instanceName = instanceName;
		this.len = elements == null ? 0 : elements.length;
		if (this.len > 0) {
			for (int i = 0; i < this.len; i++) {
				save(i, elements[i]);
			}
		}
	}

	public void save(int index, Object object) {
		LuceneDataAccess.save(genarateKey(index), JSON.toJSONString(object));
	}

	public Object get(int index, Class<?> targetElementClass) {
		String value = LuceneDataAccess.findValueByKey(genarateKey(index));
		return parseToObject(targetElementClass, value);
	}

	public int getInt(int index) {
		return (Integer) get(index, Integer.class);
	}

	public String getString(int index) {
		return (String) get(index, String.class);
	}

	public float getFloat(int index) {
		return (Float) get(index, Float.class);
	}

	public double getDouble(int index) {
		return (Double) get(index, Double.class);
	}

	private String genarateKey(int index) {
		return prefix + "_" + instanceName + "_" + index;
	}

	public int length() {
		return len;
	}
}
