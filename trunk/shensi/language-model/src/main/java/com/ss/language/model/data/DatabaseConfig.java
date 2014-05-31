package com.ss.language.model.data;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Environment;

import com.tfc.data.access.MysqlRepository.DbConfig;

/**
 * 连接数据库的配置
 * 
 * @author taofucheng
 * 
 */
public class DatabaseConfig {
	private static Connection conn;
	protected static Properties pro = new Properties();
	static {
		pro.put(Environment.URL, "jdbc:mysql://localhost:3306/weibo?useUnicode=true&characterEncoding=UTF-8");
		pro.put(Environment.USER, "root");
		pro.put(Environment.PASS, "root");
		pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		DbConfig.setUrl((String) pro.get(Environment.URL));
		DbConfig.setDriverClass((String) pro.get(Environment.DRIVER));
		DbConfig.setUser((String) pro.get(Environment.USER));
		DbConfig.setPassword((String) pro.get(Environment.PASS));
		conn = DbConfig.openConn();
	}

	/**
	 * 查询给定SQL的记录数。
	 * 
	 * @param countSql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public static long count(String countSql, Object... args) throws SQLException {
		Connection conn = DatabaseConfig.openConn();
		PreparedStatement ps = conn.prepareStatement(countSql);
		if (args != null && args.length > 0) {
			int i = 1;
			for (Object arg : args) {
				ps.setObject(i++, arg);
			}
		}
		ResultSet rs = ps.executeQuery();
		long count = 0;
		while (rs.next()) {
			count = rs.getLong(1);
		}
		rs.close();
		ps.close();
		return count;
	}

	/**
	 * 查询指定的SQL
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static List<Map<String, Object>> query(String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, args[i]);
				}
			}
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
		} catch (Throwable e) {
			System.err.println(sql + " " + args[0]);
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 执行指定的SQL内容
	 * 
	 * @param sqlObj
	 * @return
	 */
	public static Object executeSql(EntitySql sqlObj) {
		if (sqlObj == null) {
			return null;
		}
		String sql = sqlObj.getSql();
		List<Object> args = sqlObj.getArgs();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn == null || conn.isClosed()) {
				conn = DbConfig.openConn();
			}
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.size(); i++) {
				ps.setObject(i + 1, args.get(i));
			}
			switch (sqlObj.getType()) {
			case INSERT:
			case DELETE:
				return ps.execute();
			case UPDATE:
				return ps.executeUpdate();
			case SELECT:
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
			System.err.println(sqlObj);
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

	@SuppressWarnings({ "unchecked" })
	public static <T> List<T> query(EntitySql sql, Class<T> clazz) {
		if (sql == null || clazz == null) {
			return null;
		}
		List<Map<String, Object>> records = (List<Map<String, Object>>) executeSql(sql);
		if (records != null && records.size() > 0) {
			Map<String, String> columMapPro = parseMap(clazz);
			List<T> result = new ArrayList<T>();
			for (Map<String, Object> r : records) {
				try {
					Object entity = clazz.newInstance();
					for (String key : r.keySet()) {
						try {
							BeanUtils.setProperty(entity, columMapPro.get(key.toLowerCase()), r.get(key));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					result.add((T) entity);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		return null;
	}

	private static <T> Map<String, String> parseMap(Class<T> clazz) {
		Class<?> curClazz = clazz;
		Map<String, String> result = new HashMap<String, String>();
		while (curClazz != Object.class) {
			Field[] fs = curClazz.getDeclaredFields();
			if (fs != null && fs.length > 0) {
				for (Field f : fs) {
					String modifier = f.toGenericString();
					if (!modifier.startsWith("private") || modifier.split(" ").length != 3) {
						// 不是类似private String xxx这样的定义就认为不合法！
						continue;
					} else if (f.getAnnotation(Transient.class) != null) {
						continue;
					} else {
						String colName = "";
						Column col = f.getAnnotation(Column.class);
						if (col != null && StringUtils.isNotBlank(col.name())) {
							colName = col.name().trim();
						} else {
							colName = EntityManager.convertor.classToTableName(f.getName());
						}
						result.put(colName.toLowerCase(), f.getName());
					}
				}
			}
			curClazz = curClazz.getSuperclass();
		}
		return result;
	}

	public static void closeConn() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Connection openConn() {
		return DbConfig.openConn();
	}
}
