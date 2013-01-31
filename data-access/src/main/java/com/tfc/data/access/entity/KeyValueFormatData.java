package com.tfc.data.access.entity;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.LuceneDataAccess;

/**
 * 键值对结构的格式。如Map类型。
 * 
 * @author taofucheng
 * 
 */
public class KeyValueFormatData extends AbstractFormatData {
	private static final String prefix = "map";
	private String instanceName = "";

	public KeyValueFormatData(String instanceName) {
		this.instanceName = instanceName + System.nanoTime();
	}

	public void put(Object key, Object value) {
		LuceneDataAccess.save(genarateKey(key), JSON.toJSONString(value));
	}

	public String getString(Object key) {
		return (String) get(key, String.class);
	}

	public int getInt(Object key) {
		try {
			return (Integer) get(key, Integer.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public float getFloat(Object key) {
		try {
			return (Float) get(key, Float.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public double getDouble(Object key) {
		try {
			return (Double) get(key, Double.class);
		} catch (Exception e) {
			return 0;
		}
	}

	public Object get(Object key, Class<?> targetElementClass) {
		String value = LuceneDataAccess.findValueByKey(genarateKey(key));
		return parseToObject(targetElementClass, value);
	}

	private String genarateKey(Object key) {
		return prefix + "_" + instanceName + "_" + JSON.toJSONString(key);
	}

	public boolean containsKey(Object key) {
		return LuceneDataAccess.findValueByKey(genarateKey(key)) != null;
	}
}
