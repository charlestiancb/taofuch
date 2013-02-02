package com.tfc.data.access.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.LuceneDataAccess;

/**
 * 键值对结构的格式。如Map类型。
 * 
 * @author taofucheng
 * 
 */
public class KeyValueFormatData<K, V> extends AbstractFormatData {
	private static final String prefix = "map";
	private String instanceName = "";
	private Set<Entry<K, V>> entries = new LinkedHashSet<Entry<K, V>>();
	private Class<?> valueClass = null;

	public KeyValueFormatData(String instanceName) {
		this.instanceName = instanceName + System.nanoTime();
	}

	public void put(K key, V value) {
		if (valueClass == null && value != null) {
			valueClass = value.getClass();
		}
		String store = JSON.toJSONString(value);
		if (Number.class.isAssignableFrom(valueClass) && "NaN".equals(String.valueOf(value))) {
			// 如果是数字，则使用String的方式存储
			store = "NaN";
		}
		boolean ret = LuceneDataAccess.save(genarateKey(key), store);
		if (ret) {
			synchronized (entries) {
				entries.add(new Entry<K, V>(this, key));
			}
		}
	}

	public long size() {
		return entries.size();
	}

	@SuppressWarnings("unchecked")
	public V getValue(Object key) {
		if (valueClass == null) {
			return null;
		}
		String value = LuceneDataAccess.findValueByKey(genarateKey(key));
		if ("NaN".equals(value)) {
			if (Double.class.isAssignableFrom(valueClass)) {
				return (V) new Double("NaN");
			} else if (Float.class.isAssignableFrom(valueClass)) {
				return (V) new Float("NaN");
			}
		}
		return (V) parseToObject(valueClass, value);
	}

	private String genarateKey(Object key) {
		return prefix + "_" + instanceName + "_" + JSON.toJSONString(key);
	}

	public boolean containsKey(Object key) {
		return LuceneDataAccess.findValueByKey(genarateKey(key)) != null;
	}

	public static class Entry<K, V> {
		private KeyValueFormatData<K, V> instance;
		private K key;

		public Entry(KeyValueFormatData<K, V> instance, K perKey) {
			this.key = perKey;
			this.instance = instance;
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return (V) instance.getValue(key);
		}
	}

	public Set<Entry<K, V>> entrySet() {
		return this.entries;
	}
}
