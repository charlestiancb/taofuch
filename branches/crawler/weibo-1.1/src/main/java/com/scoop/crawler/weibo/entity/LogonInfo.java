package com.scoop.crawler.weibo.entity;

/**
 * 登录信息
 * 
 * @author taofucheng
 * 
 */
public class LogonInfo {
	private static LogonInfo logon;
	private String username;
	private String password;

	private LogonInfo(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	public static void store(String username, String password) {
		LogonInfo.logon = new LogonInfo(username, password);
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
}
