package com.tfc.data.access;

public interface DataService {
	public void save(Object key, Object value);

	public Object getByKey(Object key);
}
