package com.tfc.data.access;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class EhcacheService implements DataService {
	private void init() {
		// 使用默认配置文件创建CacheManager
		CacheManager manager = CacheManager.create();
		// 通过manager可以生成指定名称的Cache对象
		Cache cache = manager.getCache("demoCache");
	}

	public void save(Object key, Object value) {
		// TODO Auto-generated method stub

	}

	public Object getByKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

}
