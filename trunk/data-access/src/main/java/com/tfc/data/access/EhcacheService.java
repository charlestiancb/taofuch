package com.tfc.data.access;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

public class EhcacheService implements DataService {
	private CacheManager manager;
	private Cache cache;

	private void init() {
		if (manager == null || cache == null) {
			// 使用默认配置文件创建CacheManager
			manager = CacheManager.create();
			// 通过manager可以生成指定名称的Cache对象
			cache = manager.getCache(CacheManager.DEFAULT_NAME);
		}
	}

	public void save(Object key, Object value) {
		// TODO Auto-generated method stub
		init();
	}

	public Object getByKey(Object key) {
		// TODO Auto-generated method stub
		init();
		return null;
	}

	public void close() {
		manager.shutdown();
	}

}
