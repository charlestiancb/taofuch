package com.tfc.data.access;


/**
 * 使用Lucence存取数据。
 * 
 * @author taofucheng
 * 
 */
public class RepositoryFactory {
	private static Repository repo = new LuceneRepository();

	private RepositoryFactory() {
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static boolean save(String id, String key, String value) {
		return repo.save(id, key, value);
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static boolean save(String key, String value) {
		return save("0", key, value);
	}

	public static String findValueByKey(String key) {
		return repo.findValueByKey(key);
	}

	public static String findKeyByValue(String value) {
		return repo.findKeyByValue(value);
	}

	public static String findKeyById(String id) {
		return findKeyById(id);
	}

	public static String findValueById(String id) {
		return findValueById(id);
	}

	public static void main(String[] args) {
		save("", "hehehehe", "hahahahaha");
		System.out.println(findValueByKey("hehehehe"));
	}
}
