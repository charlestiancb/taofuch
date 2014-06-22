package com.ss.language.model.gibblda;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;

/**
 * Mysql数据库方式存储
 * 
 * @author taofucheng
 * 
 */
public class MysqlDataAccess {
	private String tableName;

	public static void main(String[] args) {
		MysqlDataAccess mda = MysqlDataAccess.getInstance("test");
		mda.save("hehe", "123");
		mda.save("haha", "456");
		System.out.println(mda.findValueByKey("xixi"));
		System.out.println(mda.findValueByKey("hehe"));
		mda.save("xixi", "789");
		System.out.println(mda.findValueByKey("xixi"));
	}

	private MysqlDataAccess(String instanceName) {
		tableName = "mda_" + StringUtils.defaultIfBlank(instanceName, String.valueOf(System.nanoTime()));
	}

	public void save(String key, String value) {
		String sql = "insert into " + tableName + "(`key`,`content`) values (?,?)";
		DatabaseConfig.executeSql(new EntitySql().setSql(sql).addArg(key).addArg(value).setType(SqlType.INSERT));
	}

	public String findValueByKey(String key) {
		String sql = "select `content` from " + tableName + " where `key`=?";
		List<Map<String, Object>> result = DatabaseConfig.query(sql, key);
		if (result != null && result.size() > 0) {
			Map<String, Object> m = result.get(0);
			String r = (String) m.get("CONTENT");
			return r == null ? (String) m.get("content") : r;
		}
		return null;
	}

	private void init() {
		// 先将存在的表删除
		String delSql = "DROP TABLE IF EXISTS " + tableName;
		DatabaseConfig.executeSql(new EntitySql().setSql(delSql).setType(SqlType.DELETE));
		// 先将所有标题汇总，作为文章来看。
		String createSql = "create table "
				+ tableName
				+ "(`rec_id` bigint(20) NOT NULL AUTO_INCREMENT,`key` text COLLATE utf8_bin NOT NULL,`content` text COLLATE utf8_bin NOT NULL,PRIMARY KEY (`rec_id`),INDEX `list_idx_key` (`key`(255) ASC),INDEX `list_idx_content` (`content`(255) ASC))";
		DatabaseConfig.executeSql(new EntitySql().setSql(createSql).setType(SqlType.UPDATE));
	}

	public static MysqlDataAccess getInstance(String instanceName) {
		MysqlDataAccess mda = new MysqlDataAccess(instanceName);
		mda.init();
		return mda;
	}

}
