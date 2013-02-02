package com.tfc.data.access.entity;

import com.tfc.data.access.RepositoryFactory;

/**
 * 数组形式的格式。
 * 
 * @author taofucheng
 * 
 */
public class ArrayFormatData<T> extends AbstractFormatData {
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
		this.instanceName = instanceName + System.nanoTime() + random();
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
	public ArrayFormatData(String instanceName, T... elements) {
		this.instanceName = instanceName;
		this.len = elements == null ? 0 : elements.length;
		if (this.len > 0) {
			for (int i = 0; i < this.len; i++) {
				save(i, elements[i]);
			}
		}
	}

	public void save(int index, T object) {
		if (valueClass == null && object != null) {
			valueClass = object.getClass();
		}
		String store = getStoreValue(object);
		RepositoryFactory.save(genarateKey(index), store);
	}

	@SuppressWarnings("unchecked")
	public T getValue(int index) {
		if (valueClass == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(index));
		if ("NaN".equals(value)) {
			if (Double.class.isAssignableFrom(valueClass)) {
				return (T) new Double("NaN");
			} else if (Float.class.isAssignableFrom(valueClass)) {
				return (T) new Float("NaN");
			}
		}
		return (T) parseToObject(valueClass, value);
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
