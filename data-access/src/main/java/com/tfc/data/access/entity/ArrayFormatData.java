package com.tfc.data.access.entity;

import com.tfc.data.access.Repository.SaveType;
import com.tfc.data.access.RepositoryFactory;

/**
 * 数组形式的格式。
 * 
 * @author taofucheng
 * 
 */
public class ArrayFormatData<T> extends AbstractFormatData<T> {
	private static final long serialVersionUID = 8836924284059589348L;
	private static final String prefix = "array";
	private int len;
	private int curLen = 0;

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
		String store = processStore(object);
		SaveType ret = RepositoryFactory.save(genarateKey(index), store);
		if (ret != null && ret.compareTo(SaveType.save) == 0) {
			++curLen;
		}
	}

	public T get(int index) {
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

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getCurLen() {
		return curLen;
	}

	public void setCurLen(int curLen) {
		this.curLen = curLen;
	}

	public boolean contains(T obj) {
		String store = processStore(obj);
		return RepositoryFactory.findKeyByValue(store) != null;
	}

	public void add(T obj) {
		save(curLen, obj);
	}
}
