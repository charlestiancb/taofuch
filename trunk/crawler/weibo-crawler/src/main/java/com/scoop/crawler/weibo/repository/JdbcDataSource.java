package com.scoop.crawler.weibo.repository;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.cfg.Environment;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.parser.Parser;
import com.scoop.crawler.weibo.repository.entity.EntityManager;
import com.scoop.crawler.weibo.repository.entity.EntitySql;
import com.scoop.crawler.weibo.repository.entity.FetchType;
import com.scoop.crawler.weibo.repository.mysql.Comment;
import com.scoop.crawler.weibo.repository.mysql.EntityTransfer;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.repository.mysql.Fans;
import com.scoop.crawler.weibo.repository.mysql.FetchInfo;
import com.scoop.crawler.weibo.repository.mysql.Follow;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.repository.mysql.Weibo;

public class JdbcDataSource extends DatabaseDataSource {
	private Connection conn;

	/**
	 * 用于操作Mysql的数据源方式！
	 */
	public JdbcDataSource() {
		super();
		if (conn == null) {
			connect();
		}
	}

	/**
	 * 连接数据库
	 */
	private void connect() {
		try {
			Class.forName(pro.getProperty(Environment.DRIVER));
			conn = DriverManager.getConnection(	pro.getProperty(Environment.URL),
												pro.getProperty(Environment.USER),
												pro.getProperty(Environment.PASS));
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("创建数据库连接失败！");
			System.exit(0);
		}
	}

	public void saveWeibo(OneWeiboInfo weibo) {
		// 保存微博信息，先判断存不存在，如果不存在才插入！
		try {
			if (!isWeiboExists(weibo.getId())) {
				executeSql(EntityManager.createInsertSQL(EntityTransfer.parseWeibo(weibo)));
			}
		} catch (Exception e) {
		}
		saveFetchIfNeccessory(weibo.getId(), FetchType.weibo);
		saveUserIfNeccessory(weibo.getPublisher());
	}

	/**
	 * 保存抓取信息
	 * 
	 * @param id
	 * @param weibo
	 */
	@SuppressWarnings("unchecked")
	private void saveFetchIfNeccessory(String id, FetchType weibo) {
		try {
			FetchInfo fi = new FetchInfo(Parser.getQuery(), id, weibo.name());
			if (fi.needSave()) {
				List<Map<String, Object>> records = (List<Map<String, Object>>) executeSql(EntityManager.createSelectSQL(fi));
				if (records == null || records.isEmpty()) {
					executeSql(EntityManager.createInsertSQL(fi));
				}
			}
		} catch (Exception e) {
		}
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveComment(WeiboComment comment) {
		// 保存微博评论信息，先判断存不存在，如果不存在才插入！
		try {
			if (!isCommentExists(comment.getId())) {
				executeSql(EntityManager.createInsertSQL(EntityTransfer.parseComment(comment)));
			}
		} catch (Exception e) {
		}
		savePerson(comment.getPerson());
	}

	public void savePerson(WeiboPersonInfo person) {
		// 保存发布者信息，即用户信息，先判断存不存在，如果不存在才插入！
		saveUserIfNeccessory(person);
		saveFans(person.getId(), person.getFans());
		saveFollows(person.getId(), person.getFollows());
	}

	/**
	 * 保存粉丝信息
	 * 
	 * @param fans
	 */
	protected void saveFans(String userId, List<WeiboPersonInfo> fans) {
		userId = StringUtils.trim(userId);
		if (StringUtils.isNotEmpty(userId) && fans != null && fans.size() > 0) {
			for (WeiboPersonInfo fansUser : fans) {
				try {
					Fans f = new Fans();
					f.setUserId(userId);
					f.setFansId(fansUser.getId());
					executeSql(EntityManager.createInsertSQL(f));
				} catch (Exception e) {
				}
				saveUserIfNeccessory(fansUser);
			}
		}
	}

	/**
	 * 保存关注信息
	 * 
	 * @param follows
	 */
	protected void saveFollows(String userId, List<WeiboPersonInfo> follows) {
		userId = StringUtils.trim(userId);
		if (StringUtils.isNotEmpty(userId) && follows != null && follows.size() > 0) {
			for (WeiboPersonInfo followUser : follows) {
				try {
					Follow f = new Follow();
					f.setUserId(userId);
					f.setFollowId(followUser.getId());
					executeSql(EntityManager.createInsertSQL(f));
				} catch (Exception e) {
				}
				saveUserIfNeccessory(followUser);
			}
		}
	}

	/**
	 * 如果用户不存在，则保存！
	 * 
	 * @param person
	 */
	protected void saveUserIfNeccessory(WeiboPersonInfo person) {
		try {
			if (!isUserExists(person.getId())) {
				// 如果用户不存在，则保存！
				executeSql(EntityManager.createInsertSQL(EntityTransfer.parseUser(person)));
			}
		} catch (Exception e) {
		}
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

	public boolean isWeiboExists(String weiboId) {
		boolean r = false;
		try {
			Weibo w = new Weibo();
			w.setWeiboId(weiboId);
			r = hasRecord(w);
		} catch (Exception e) {
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	private boolean hasRecord(Object w) {
		boolean r;
		List<Map<String, Object>> rs = (List<Map<String, Object>>) executeSql(EntityManager.createSelectSQL(w));
		r = rs != null && rs.size() > 0;
		return r;
	}

	public boolean isCommentExists(String commentId) {
		boolean r = false;
		try {
			Comment c = new Comment();
			c.setCommentId(commentId);
			r = hasRecord(c);
		} catch (Exception e) {
		}
		return r;
	}

	public boolean isUserExists(String userId) {
		boolean r = false;
		try {
			User u = new User();
			u.setUserId(userId);
			r = hasRecord(u);
		} catch (Exception e) {
		}
		return r;
	}

	public static void main(String[] args) {
		Weibo weibo = new Weibo();
		weibo.setCommentNum(2L);
		weibo.setContent("这是测试内容哦~");
		weibo.setForwordNum(3L);
		weibo.setOrign("手工");
		weibo.setPublishTime("2012-11-27");
		weibo.setUrl("http://adfasdfasd");
		weibo.setUserId("adfXaADSZxa-adax");
		weibo.setWeiboId("asdfasdfDSdjgh");
		new JdbcDataSource().executeSql(EntityManager.createInsertSQL(weibo));
		User u = new User();
		u.setUserId("1729014640");
		new JdbcDataSource().query(EntityManager.createSelectSQL(u), u.getClass());
	}

	public void saveFailedRequest(FailedRequest request) {
		// 保存失败的请求！
		try {
			executeSql(EntityManager.createInsertSQL(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FailedRequest pop() {
		EntitySql sql = EntityManager.createSelectSQL(new FailedRequest());
		sql.setSql(sql.getSql() + " limit 1");
		List<FailedRequest> records = query(sql, FailedRequest.class);
		FailedRequest req = new FailedRequest();
		if (records != null && records.size() > 0) {
			try {
				return records.get(0);
			} catch (Exception e) {
			}
		}
		return req;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> query(EntitySql sql, Class<T> clazz) {
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
						BeanUtils.setProperty(entity, columMapPro.get(key), r.get(key));
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
						result.put(colName, f.getName());
					}
				}
			}
			curClazz = curClazz.getSuperclass();
		}
		return result;
	}
}
