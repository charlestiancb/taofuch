package com.tfc.data.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheRepository extends Repository {
	private CacheManager manager;
	private Cache cache;
	/** 使用cache默认的配置 */
	private boolean defaultConfig = false;

	public static void main(String[] args) {
		EhcacheRepository er = new EhcacheRepository();
		er.save("hehehe", "hahaha");
		System.out.println(er.findValueByKey("hehehe"));
		er.save("hehehe", "xixixixi");
		System.out.println(er.findValueByKey("hehehe"));
	}

	public EhcacheRepository() {
		init(null);
	}

	public EhcacheRepository(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
		init(null);
	}

	public EhcacheRepository(File config) {
		try {
			init(new FileInputStream(config));
		} catch (FileNotFoundException e) {
		}
	}

	public EhcacheRepository(InputStream config) {
		init(config);
	}

	private void init(InputStream is) {
		if (cache == null) {
			if (defaultConfig) {
				manager = CacheManager.create();
			} else if (is != null) {
				manager = CacheManager.create(is);
			} else {
				// 使用默认配置文件创建CacheManager
				manager = CacheManager.create(EhcacheRepository.class.getResourceAsStream("/ehcache-repo.xml"));
			}
			// 通过manager可以生成指定名称的Cache对象
			cache = manager.getCache("defaultCache");
			if (cache == null) {
				System.err.println("不正常！没有读取到相应的配置信息！！");
			}
		}
	}

	@Override
	public SaveType save(String key, String value) {
		cache.put(new Element(key, value));
		return SaveType.save;
	}

	@Override
	public String findValueByKey(String key) {
		Element e = cache.get(key);
		return (String) (e == null ? null : e.getObjectValue());
	}

	@Override
	public String findKeyByValue(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		if (manager != null) {
			manager.shutdown();
		}
	}
}