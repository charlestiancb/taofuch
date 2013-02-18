package com.tfc.data.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MysqlRepository extends Repository {
	public static void main(String[] args) {
		MysqlRepository er = new MysqlRepository();
		er.save("hehehe", "hahaha");
		System.out.println(er.findValueByKey("hehehe"));
		er.save("hehehe", "xixixixi");
		System.out.println(er.findValueByKey("hehehe"));
	}

	private EhcacheRepository ehcache = new EhcacheRepository(true);

	private Connection conn = DbConfig.openConn();
	private static String tableName = "database_access_temp_table";
	private static final String insertSql = "insert into " + tableName + "(id,content) values (?,?)";
	private static final String selectByKeySql = "select * from " + tableName + " where id=?";
	private static final String selectByValueSql = "select * from " + tableName + " where content=?";
	private static final String updateSql = "update " + tableName + " set content=? where id=?";

	{
		initTable();
	}

	/** 初始化操作所需要的表 */
	private void initTable() {
		// 先删除后创建表
		String sql = "drop table if exists " + tableName;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute(sql);
			//
			sql = "CREATE TABLE `" + tableName + "` (id TEXT NULL , content TEXT NULL );";
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public SaveType save(String key, String value) {
		ehcache.save(key, value);
		List<Map<String, Object>> record = (List<Map<String, Object>>) executeSql(selectByKeySql, SqlType.select, key);
		if (record == null || record.isEmpty()) {
			// 如果不存在，则新增
			executeSql(insertSql, SqlType.insert, key, value);
			return SaveType.save;
		} else {
			// 否则更新之
			executeSql(updateSql, SqlType.update, value, key);
			return SaveType.update;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findValueByKey(String key) {
		String result = ehcache.findValueByKey(key);
		if (result != null) {
			return result;
		}
		List<Map<String, Object>> record = (List<Map<String, Object>>) executeSql(selectByKeySql, SqlType.select, key);
		if (record == null || record.isEmpty()) {
			return null;
		} else {
			result = (String) record.get(0).get("content");
			ehcache.save(key, result);
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findKeyByValue(String value) {
		List<Map<String, Object>> record = (List<Map<String, Object>>) executeSql(selectByValueSql, SqlType.select,
				value);
		if (record == null || record.isEmpty()) {
			return null;
		} else {
			return (String) record.get(0).get("id");
		}
	}

	@Override
	public void close() {
		try {
			ehcache.close();
			conn.close();
		} catch (Exception e) {
		}
	}

	protected Object executeSql(String sql, SqlType type, String... args) {
		if (args == null) {
			args = new String[] {};
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn == null || conn.isClosed()) {
				conn = DbConfig.openConn();
			}
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			switch (type) {
			case insert:
				return ps.execute();
			case update:
				return ps.executeUpdate();
			case select:
				rs = ps.executeQuery();
				List<String> colNames = new ArrayList<String>();
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 0; i < md.getColumnCount(); i++) {
					colNames.add(md.getColumnLabel(i + 1));
				}
				while (rs.next()) {
					Map<String, Object> record = new LinkedHashMap<String, Object>();
					for (String col : colNames) {
						record.put(col, rs.getObject(col));
					}
					result.add(record);
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static enum SqlType {
		insert, update, select
	}

	public static class DbConfig {

		private static String url = "jdbc:mysql://localhost:3306/infordb?useUnicode=true&characterEncoding=UTF-8";
		private static String driverClass = "com.mysql.jdbc.Driver";
		private static String user = "root";
		private static String password = "root";
		private static Connection conn;

		public static Connection openConn() {
			try {
				if (conn != null && !conn.isClosed()) {
					return conn;
				}
				// 连接数据库
				Class.forName(driverClass);
				conn = DriverManager.getConnection(url, user, password);
				conn.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("创建数据库连接失败！");
				System.exit(0);
			}
			return conn;
		}

		/** 设置url链接 */
		public static void setUrl(String _url) {
			url = StringUtils.isBlank(_url) ? url : StringUtils.trim(_url);
		}

		/** 设置驱动程序 */
		public static void setDriverClass(String _driverClass) {
			driverClass = StringUtils.isBlank(_driverClass) ? driverClass : StringUtils.trim(_driverClass);
		}

		/** 设置用户名 */
		public static void setUser(String _user) {
			user = StringUtils.isBlank(_user) ? user : StringUtils.trim(_user);
		}

		/** 设置密码 */
		public static void setPassword(String _password) {
			password = StringUtils.isBlank(_password) ? password : StringUtils.trim(_password);
		}
	}
}
