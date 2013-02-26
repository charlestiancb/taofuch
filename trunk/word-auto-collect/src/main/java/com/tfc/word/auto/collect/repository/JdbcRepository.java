package com.tfc.word.auto.collect.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.tfc.word.auto.collect.config.Configuration;
import com.tfc.word.auto.collect.repository.entity.FetchOrig;
import com.tfc.word.auto.collect.repository.entity.WordBase;

public class JdbcRepository extends Repository {
	private Connection conn;
	protected Properties pro = new Properties();

	public JdbcRepository() {
		pro.put("url", "jdbc:mysql://localhost:3306/words?useUnicode=true&characterEncoding=UTF-8");
		pro.put("user", "root");
		pro.put("password", "root");
		pro.put("driverClass", "com.mysql.jdbc.Driver");
		if (conn == null) {
			connect();
		}
	}

	/**
	 * 连接数据库
	 */
	private void connect() {
		try {
			Class.forName(pro.getProperty("driverClass"));
			conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"),
					pro.getProperty("password"));
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void saveWord(String word) {
		if (StringUtils.isBlank(word)) {
			return;
		}
		WordBase wb = new WordBase();
		wb.setWord(word);
		executeSql(EntityManager.createInsertSQL(wb));
	}

	/**
	 * 更新词的次数，加一次。同时，如果加一次之后次数达到审核次数，则自动审核通过。
	 * 
	 * @param word
	 *            指定的词
	 * @return 审核通过-true；否则-false
	 */
	public boolean updateWord(WordBase word) {
		if (word == null || word.getRecId() == null || StringUtils.isBlank(word.getWord())) {
			return false;
		}
		word.setStatNum(word.getStatNum() + 1);
		if (word.getStatNum() >= Configuration.AUTO_CHECKED_NUM) {
			word.setCheckedTime(new Date());
			word.setCheckStatus(WordBase.CHECK_STATUS_YES);
		}
		WordBase where = new WordBase();
		where.setRecId(word.getRecId());
		executeSql(EntityManager.createUpdateSQL(word, where));
		return WordBase.CHECK_STATUS_YES.equals(word.getCheckStatus());
	}

	public WordBase getWord(String word) {
		if (StringUtils.isBlank(word)) {
			return null;
		}
		WordBase wb = new WordBase();
		wb.setWord(word);
		List<WordBase> words = query(wb);
		if (words == null || words.isEmpty()) {
			return null;
		} else {
			return words.get(0);
		}
	}

	public void saveOrgi(String _orig) {
		FetchOrig orig = new FetchOrig();
		orig.setOrig(_orig);
		orig.setAddTime(new Date());
		EntityManager.createInsertSQL(orig);

	}

	public FetchOrig getOrgi(String orig) {
		FetchOrig entity = new FetchOrig();
		entity.setOrig(orig);
		List<FetchOrig> origs = query(entity);
		if (origs != null && origs.size() > 0) {
			return origs.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> query(T entity) {
		EntitySql sql = EntityManager.createSelectSQL(entity);
		Class<?> clazz = entity.getClass();
		if (sql == null || clazz == null) {
			return null;
		}
		List<Map<String, Object>> records = (List<Map<String, Object>>) executeSql(sql);
		if (records != null && records.size() > 0) {
			Map<String, String> columMapPro = parseMap(clazz);
			List<T> result = new ArrayList<T>();
			for (Map<String, Object> r : records) {
				try {
					Object instance = clazz.newInstance();
					for (String key : r.keySet()) {
						try {
							BeanUtils.setProperty(instance, columMapPro.get(key.toLowerCase()), r.get(key));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					result.add((T) instance);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		return null;
	}

	private <T> Map<String, String> parseMap(Class<T> clazz) {
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

	private Object executeSql(EntitySql sqlObj) {
		if (sqlObj == null) {
			return null;
		}
		String sql = sqlObj.getSql();
		List<Object> args = sqlObj.getArgs();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (conn == null || conn.isClosed()) {
				connect();
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
}
