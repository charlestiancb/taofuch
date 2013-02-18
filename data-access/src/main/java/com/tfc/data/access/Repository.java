package com.tfc.data.access;

public abstract class Repository {
	public abstract SaveType save(String key, String value);

	public abstract String findValueByKey(String key);

	public abstract String findKeyByValue(String value);

	public abstract void close();

	/**
	 * 保存的类型
	 * 
	 * @author taofucheng
	 * 
	 */
	public enum SaveType {
		save, update
	}
}
