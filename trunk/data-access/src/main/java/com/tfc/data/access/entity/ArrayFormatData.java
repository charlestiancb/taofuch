package com.tfc.data.access.entity;

import com.tfc.data.access.RepositoryFactory;

/**
 * 数组形式的格式。
 * 
 * @author taofucheng
 * 
 */
public class ArrayFormatData<T> extends AbstractFormatData<T> {
	private static final String prefix = "array";
	private int len;
	private int curLen;

	public ArrayFormatData() {
		this("def", 0);
	}

	/**
	 * 类似这样的定义：new int[2]
	 * 
	 * @param instanceName
	 * @param len
	 */
	public ArrayFormatData(String instanceName, int len) {
		this.setInstanceName(instanceName + System.nanoTime() + random());
		this.len = len;
	}

	/**
	 * 类似这样的定义：new int[]{1,2}
	 * 
	 * @param instanceName
	 * @param elements
	 */
	public ArrayFormatData(String instanceName, T... elements) {
		this.setInstanceName(instanceName);
		this.len = elements == null ? 0 : elements.length;
		if (this.len > 0) {
			for (int i = 0; i < this.len; i++) {
				save(i, elements[i]);
			}
		}
	}

	public void save(int index, T object) {
		if (getValueClass() == null && object != null) {
			setValueClass(object.getClass());
		}
		String store = getStoreValue(object);
		boolean ret = RepositoryFactory.save(genarateKey(index), store);
		if (ret) {
			++curLen;
		}
	}

	public T getValue(int index) {
		if (getValueClass() == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(index));
		return parseValue(value);
	}

	private String genarateKey(int index) {
		return prefix + "_" + getInstanceName() + "_" + index;
	}

	/**
	 * 相当于s.length，其中，s=new int[len];
	 * 
	 * @return
	 */
	public int length() {
		return len > curLen ? len : curLen;
	}
}
