package com.tfc.data.access;

public abstract class Repository {
	public abstract boolean save(String key, String value);

	public abstract String findValueByKey(String key);

	public abstract String findKeyByValue(String value);

	public abstract void close();
}
