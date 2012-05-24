package com.tfc.participle.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import com.tfc.participle.data.db.mapper.ResultMapper;

public abstract class ReadFromDB extends Reader {
	/** 数据库连接 */
	private Connection conn = null;
	private String driver;
	private String url;
	private String username;
	private String password;

	public Connection getConn() {
		if (conn == null) {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, username, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	/**
	 * 执行修改或添加操作的sql。
	 * 
	 * @param sql
	 *            sql内容
	 * @param params
	 *            sql中需要的参数。
	 * @return 返回执行成功的记录数
	 */
	public int exec(String sql, Object... params) {
		// TODO 完成exec逻辑
		return 0;
	}

	/**
	 * 将sql查询出来的结果转换成指定的类返回！
	 * 
	 * @param t
	 *            指定的类
	 * @param sql
	 *            sql内容
	 * @param params
	 *            参数信息
	 * @return
	 */
	public <T> List<T> query(ResultMapper<T> mapper, String sql, Object... params) {
		// TODO 完成query逻辑
		return null;
	}

	protected String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	protected String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected String getDriver() {
		return driver;
	}

	protected void setDriver(String driver) {
		this.driver = driver;
	}
}
