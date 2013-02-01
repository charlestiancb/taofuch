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

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
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
		try {
			return (Integer) get(index, Integer.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public String getString(int index) {
		return (String) get(index, String.class);
	}

	public float getFloat(int index) {
		try {
			return (Float) get(index, Float.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public double getDouble(int index) {
		try {
			return (Double) get(index, Double.class);
		} catch (Exception e) {
			return 0;
		}
	}

	private String genarateKey(int index) {
		return prefix + "_" + instanceName + "_" + index;
	}

	/**
	 * 相当于s.length，其中，s=new int[len];
	 * 
	 * @return
	 */
	public int length() {
		return len;
	}
}