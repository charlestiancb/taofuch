package com.tfc.data.access;

import com.tfc.data.access.Repository.SaveType;

/**
 * 使用Lucence存取数据。
 * 
 * @author taofucheng
 * 
 */
public class RepositoryFactory {
	// new LuceneRepository();EhcacheRepository();MysqlRepository();
	private static Repository repo = new MysqlRepository();

	private RepositoryFactory() {
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static SaveType save(String key, String value) {
		return repo.save(key, value);
	}

	public static String findValueByKey(String key) {
		return repo.findValueByKey(key);
	}

	public static String findKeyByValue(String value) {
		return repo.findKeyByValue(value);
	}

	public static void main(String[] args) {
		save("hehehehe", "hahahahaha");
		System.out.println(findValueByKey("hehehehe"));
	}

	public static void close() {
		repo.close();
	}
}
