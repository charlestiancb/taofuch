package com.ss.language.model.gibblda;

import java.util.List;
import java.util.Map;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;

/**
 * 使用Lucence存取数据。
 * 
 * @author taofucheng
 * 
 */
public class LuceneDataAccess {
	private static boolean hasInit = false;
	private static String tb = "tmp_key_value_tb4lda";
	static {
		init();
	}

	/**
	 * 初始化Lucene的一些设置
	 * 
	 * @param option
	 */
	private static void init() {
		if (hasInit) {
			// 如果已经初始化过，则不进行
			return;
		}
		// 删除、创建临时表
		String sql = "DROP TABLE IF EXISTS `" + tb + "`";
		EntitySql sqlObj = new EntitySql();
		sqlObj.setSql(sql);
		sqlObj.setType(SqlType.INSERT);
		DatabaseConfig.executeSql(sqlObj);
		//
		sql = "CREATE TABLE `" + tb + "` (`k` varchar(255) NOT NULL,"
				+ "  `v` text DEFAULT NULL," + "  PRIMARY KEY (`k`),"
				+ "  FULLTEXT (`v`)"
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
		sqlObj.setSql(sql);
		DatabaseConfig.executeSql(sqlObj);
		//
		hasInit = true;
	}

	/**
	 * 保存键值对的数据
	 * 
	 * @param key
	 * @param value
	 */
	public static void save(String key, String value) {
		try {
			// 先确定是否存在，然后更新或保存。
			String v = findValueByKey(key);
			String sql = null;
			EntitySql sqlObj = new EntitySql();
			if (v == null) {
				// 添加
				sql = "insert into " + tb + "(k,v) values(?,?)";
				sqlObj.setSql(sql);
				sqlObj.setType(SqlType.INSERT);
				sqlObj.addArg(key).addArg(value);
				DatabaseConfig.executeSql(sqlObj);
			} else {
				// 修改
				sql = "update " + tb + " set v=? where k=?";
				sqlObj.setSql(sql);
				sqlObj.setType(SqlType.UPDATE);
				sqlObj.addArg(value).addArg(key);
				DatabaseConfig.executeSql(sqlObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String findValueByKey(String key) {
		try {
			String sql = "select v from " + tb + " where k=?";
			List<Map<String, Object>> values = DatabaseConfig.query(sql, key);
			if (values != null && values.size() > 0) {
				Map<String, Object> v = values.get(0);
				return (String) v.values().iterator().next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String findKeyByValue(String value) {
		try {
			String sql = "select k from " + tb + " where v=?";
			List<Map<String, Object>> values = DatabaseConfig.query(sql, value);
			if (values != null && values.size() > 0) {
				Map<String, Object> v = values.get(0);
				return (String) v.values().iterator().next();
			}
		} catch (Exception e) {
		}
		return null;
	}
}
