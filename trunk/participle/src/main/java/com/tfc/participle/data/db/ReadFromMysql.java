package com.tfc.participle.data.db;

/**
 * 从MySQL中读取数据。
 * 
 * @author taofucheng
 * 
 */
public class ReadFromMysql extends ReadFromDB {
	private static final String driver = "com.MySQL.jdbc.Driver";

	private ReadFromMysql() {
	}

	/**
	 * 获取一个连接的实例。
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static final ReadFromMysql getInstance(String url, String username, String password) {
		ReadFromMysql mysql = new ReadFromMysql();
		mysql.setDriver(driver);
		mysql.setUrl(url);
		mysql.setUsername(username);
		mysql.setPassword(password);
		return mysql;
	}
}
