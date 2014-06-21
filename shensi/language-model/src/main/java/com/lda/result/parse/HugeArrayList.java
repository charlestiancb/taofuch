package com.lda.result.parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ss.language.model.data.DatabaseConfig;
import com.ss.language.model.data.EntitySql;
import com.ss.language.model.data.EntitySql.SqlType;

public class HugeArrayList<E> extends ArrayList<E> {
	private String tablePrefix = "list_tmp_";
	/**
	 * 
	 */
	private static final long serialVersionUID = -6742797239706348786L;
	private String tableName;
	private int cacheSize = 300;
	private List<E> cache = new ArrayList<E>();
	private Class<? extends Object> clazz;
	/** 位移量 */
	private int offset = 0;
	private int size = 0;

	public HugeArrayList() {
		tableName = tablePrefix + System.nanoTime();
		initTable();
	}

	public HugeArrayList(int cacheSize) {
		this();
		this.cacheSize = cacheSize;
	}

	public HugeArrayList(String tableName) {
		if (tableName == null || tableName.trim().isEmpty()) {
			this.tableName = tablePrefix + System.nanoTime();
		} else {
			this.tableName = tablePrefix + tableName.trim().toLowerCase();
		}
		initTable();
	}

	public HugeArrayList(String tableName, int cacheSize) {
		this(tableName);
		this.cacheSize = cacheSize;
	}

	private void initTable() {
		// 先将存在的表删除
		String delSql = "DROP TABLE IF EXISTS " + tableName;
		DatabaseConfig.executeSql(new EntitySql().setSql(delSql).setType(SqlType.DELETE));
		// 先将所有标题汇总，作为文章来看。
		String createSql = "create table "
				+ tableName
				+ "(`rec_id` bigint(20) NOT NULL AUTO_INCREMENT,`content` text COLLATE utf8_bin NOT NULL,PRIMARY KEY (`rec_id`),INDEX `list_idx_content` (`content`(4000) ASC))";
		DatabaseConfig.executeSql(new EntitySql().setSql(createSql).setType(SqlType.UPDATE));
	}

	public boolean add(E e) {
		if (clazz == null || clazz.isAssignableFrom(e.getClass())) {
			clazz = e.getClass();
		}
		// 插入表中
		String sql = "insert into " + tableName + "(content) values (?)";
		DatabaseConfig.executeSql(new EntitySql().setSql(sql).addArg(JSON.toJSONString(e)).setType(SqlType.INSERT));
		// 判断缓存容量，将内容放入缓存，并处理size与offset
		synchronized (cache) {
			boolean tooLarge = false;
			while (cache.size() >= cacheSize) {// 将缓存中多余的移除
				tooLarge = true;
				cache.remove(0);
			}
			cache.add(e);
			size++;
			if (tooLarge) {
				offset++;// 如果发生了超出现象，则需要增加位移信息
			}
		}
		return true;
	}

	public boolean addAll(Collection<? extends E> c) {
		if (c != null && c.size() > 0) {
			for (E e : c) {
				add(e);
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public E get(int index) {
		if (index >= offset + cache.size() || index < offset) {
			// 如果要取的数据在缓存范围之外，则查询数据库
			offset = index == cacheSize ? index : index > cacheSize ? index - cacheSize : 0;
			String titleSql = "select content from " + tableName + " order by rec_id asc limit " + index + ","
					+ cacheSize;
			List<Map<String, Object>> result = DatabaseConfig.query(titleSql);
			if (result == null || result.isEmpty()) {
				return null;
			}
			cache.clear();
			for (Map<String, Object> record : result) {
				String content = (String) record.get("content");
				cache.add((E) JSON.parseObject(content, clazz));
			}
		}
		return cache.get(index - offset);
	}

	public E remove(int index) {
		E e = get(index);
		if (e != null) {
			String sql = "select rec_id from " + tableName + " limit " + index + ",1";
			List<Map<String, Object>> result = DatabaseConfig.query(sql);
			if (result == null || result.isEmpty()) {
				return e;
			}
			sql = "delete from " + tableName + " where rec_id = ?";
			int count = (Integer) DatabaseConfig.executeSql(new EntitySql().setSql(sql)
					.addArg(result.get(0).get("rec_id")).setType(SqlType.DELETE));
			size = size - count;
		}
		return e;
	}

	public boolean remove(Object o) {
		if (o == null || o.getClass() != clazz) {
			return false;
		}
		String sql = "delete from " + tableName + " where content = ?";
		int count = (Integer) DatabaseConfig.executeSql(new EntitySql().setSql(sql).addArg(JSON.toJSONString(o))
				.setType(SqlType.DELETE));
		size = size - count;
		cache.clear();
		offset = 0;
		return true;
	}

	protected void removeRange(int fromIndex, int toIndex) {
		for (int i = fromIndex; i <= toIndex; i++) {
			remove(i);
		}
	}

	public boolean contains(Object o) {
		if (o == null || o.getClass() != clazz) {
			return false;
		}
		int M = 0;
		try {
			M = (int) DatabaseConfig.count("select count(1) from " + tableName + " where content = ?",
					JSON.toJSONString(o));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return M > 0;
	}

	public int indexOf(Object o) {
		// 无法定位位置
		return -1;
	}

	public int size() {
		return size;
	}

	public void clear() {
		initTable();
		cache.clear();
		/** 位移量 */
		offset = 0;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}
}
