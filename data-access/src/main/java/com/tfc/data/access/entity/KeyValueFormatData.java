package com.tfc.data.access.entity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.Repository.SaveType;
import com.tfc.data.access.RepositoryFactory;

/**
 * 键值对结构的格式。如Map类型。
 * 
 * @author taofucheng
 * 
 */
public class KeyValueFormatData<K, V> extends AbstractFormatData<V> {
	private static final long serialVersionUID = 298567781895600062L;
	private static final String prefix = "map";
	/** id和key的映射关系 */
	private static final String idKeyMap = "id";
	private Set<Entry<K, V>> entries = new LinkedHashSet<Entry<K, V>>();
	private Class<?> keyClass;

	public KeyValueFormatData() {
		this("def");
	}

	public KeyValueFormatData(String instanceName) {
		setInstanceName(instanceName + System.nanoTime() + random());
	}

	/**
	 * 这个效率可能会很慢，且可能存在内存溢出问题！慎用！
	 * 
	 * @return
	 * @deprecated 请使用key(int index)方法与size()方法联合方式。
	 */
	@Deprecated
	public Set<K> keySet() {
		Set<K> keys = new LinkedHashSet<K>();
		for (Entry<K, V> e : entries) {
			keys.add(e.fetchKey());
		}
		return keys;
	}

	/**
	 * 获取指定位置的key名称
	 * 
	 * @param index
	 *            从0开始
	 * @return
	 */
	public K key(int index) {
		if (index < 0 || index > entries.size()) {
			return null;
		}
		int i = 0;
		for (Entry<K, V> e : entries) {
			if (i == index) {
				return e.fetchKey();
			}
			i++;
		}
		return null;
	}

	/**
	 * 这个效率可能会很慢，且可能存在内存溢出问题！慎用！
	 * 
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public List<K> keyList() {
		List<K> keys = new ArrayList<K>();
		for (Entry<K, V> e : entries) {
			keys.add(e.fetchKey());
		}
		return keys;
	}

	public void put(K key, V value) {
		if (keyClass == null && key != null) {
			keyClass = key.getClass();
		}
		if (getValueClass() == null && value != null) {
			setValueClass(value.getClass());
		}
		String store = processStore(value);
		SaveType ret = RepositoryFactory.save(genarateKey(key), store);
		if (ret != null && ret.compareTo(SaveType.save) == 0) {
			store = processStore(key);
			ret = RepositoryFactory.save(genarateId(size()), store);
			if (ret != null && ret.compareTo(SaveType.save) == 0) {
				synchronized (entries) {
					entries.add(new Entry<K, V>(this, size()));
				}
			}
		}
	}

	public int size() {
		return entries.size();
	}

	public V get(Object key) {
		if (key == null || getValueClass() == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(key));
		return parseValue(value);
	}

	@SuppressWarnings("unchecked")
	private K fetchKey(int id) {
		if (keyClass == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateId(id));
		if ("NaN".equals(value)) {
			if (Double.class.isAssignableFrom(keyClass)) {
				return (K) new Double("NaN");
			} else if (Float.class.isAssignableFrom(keyClass)) {
				return (K) new Float("NaN");
			}
		}
		return (K) parseToObject(keyClass, value);
	}

	private String genarateKey(Object key) {
		return prefix + "_" + getInstanceName() + "_" + JSON.toJSONString(key);
	}

	private String genarateId(long size) {
		return idKeyMap + "_" + getInstanceName() + "_" + size;
	}

	public boolean containsKey(Object key) {
		return RepositoryFactory.findValueByKey(genarateKey(key)) != null;
	}

	public static class Entry<K, V> {
		private KeyValueFormatData<K, V> instance;
		private int id;

		public Entry() {
		}

		public Entry(KeyValueFormatData<K, V> instance, int id) {
			this.id = id;
			this.instance = instance;
		}

		public K fetchKey() {
			return instance.fetchKey(id);
		}

		public V fetchValue() {
			return (V) instance.get(fetchKey());
		}

		public KeyValueFormatData<K, V> getInstance() {
			return instance;
		}

		public void setInstance(KeyValueFormatData<K, V> instance) {
			this.instance = instance;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@SuppressWarnings("rawtypes")
		public boolean equals(Object o) {
			if (o instanceof Entry) {
				String oKey = ((Entry) o).instance.getInstanceName() + "_" + ((Entry) o).fetchKey();
				return oKey.equals(instance.getInstanceName() + "_" + id);
			}
			return false;
		}
	}

	public Set<Entry<K, V>> entrySet() {
		return this.entries;
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public Set<Entry<K, V>> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry<K, V>> entries) {
		this.entries = entries;
	}

	public Class<?> getKeyClass() {
		return keyClass;
	}

	public void setKeyClass(Class<?> keyClass) {
		this.keyClass = keyClass;
	}
}
