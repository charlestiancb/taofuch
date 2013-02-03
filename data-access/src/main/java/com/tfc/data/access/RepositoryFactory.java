package com.tfc.data.access;

/**
 * 使用Lucence存取数据。
 * 
 * @author taofucheng
 * 
 */
public class RepositoryFactory {
	// new LuceneRepository();EhcacheRepository();
	private static Repository repo = new EhcacheRepository();

	private RepositoryFactory() {
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static boolean save(String key, String value) {
		return repo.save(key, value);
	}

	public static String findValueByKey(String key) {
		return repo.findValueByKey(key);
	}

	public static String findKeyByValue(String value) {
		return repo.findKeyByValue(value);
	}

	public static String findKeyById(String id) {
		return repo.findKeyById(id);
	}

	public static String findValueById(String id) {
		return repo.findValueById(id);
	}

	public static void main(String[] args) {
		save("hehehehe", "hahahahaha");
		System.out.println(findValueByKey("hehehehe"));
	}

	public static void close() {
		repo.close();
	}
}
