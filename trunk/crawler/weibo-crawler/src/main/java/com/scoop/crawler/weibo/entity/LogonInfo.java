package com.scoop.crawler.weibo.entity;

import java.util.Date;

/**
 * 登录信息
 * 
 * @author taofucheng
 * 
 */
public class LogonInfo {
	private static LogonInfo logon;
	private static long preTime;
	private String username;
	private String password;

	private LogonInfo(String username, String password) {
		setUsername(username);
		setPassword(password);
		setPreTime(new Date().getTime());
	}

	public static void store(String username, String password) {
		LogonInfo.logon = new LogonInfo(username, password);
	}

	public static boolean shouldLogAgain() {
		// return new Date().getTime() - preTime > 6 * 3600 * 1000;
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
