package com.scoop.crawler.weibo.entity;

/**
 * 登录信息
 * 
 * @author taofucheng
 * 
 */
public class LogonInfo {
	/** WebDriver每次重新启动一个新的间隔时间 */
	public static long DRIVER_INTERVAL = 12 * 3600 * 1000L;
	private static LogonInfo logon;
	private static long preTime;
	private String username;
	private String password;

	private LogonInfo(String username, String password) {
		setUsername(username);
		setPassword(password);
		setPreTime(System.currentTimeMillis());
	}

	public static void store(String username, String password) {
		LogonInfo.logon = new LogonInfo(username, password);
	}

	public static boolean shouldLogAgain() {
		// return System.currentTimeMillis() - preTime > 4 * 24 * 3600 * 1000;//
		// 四天就要重新登录一次
		return false;
	}

	public static LogonInfo getLogonInfo() {
		return logon;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void setPreTime(long preTime) {
		LogonInfo.preTime = preTime;
	}

	public static long getPreTime() {
		return preTime;
	}

}
