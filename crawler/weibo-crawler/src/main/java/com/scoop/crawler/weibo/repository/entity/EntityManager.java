package com.scoop.crawler.weibo.repository.entity;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

import com.scoop.crawler.weibo.repository.entity.EntitySql.SqlType;
import com.scoop.crawler.weibo.repository.mysql.Weibo;

public class EntityManager {
	public static final NamingStrategy convertor = ImprovedNamingStrategy.INSTANCE;

	/**
	 * @param args
	 */
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
		System.out.println(createInsertSQL(weibo));
		System.out.println(createSelectSQL(weibo));
	}

	/**
	 * 根据拥有的值自动创建查询SQL及where条件。
	 * 
	 * @param entity
	 * @return
	 */
	public static EntitySql createSelectSQL(Object entity) {
		StringBuffer sql = new StringBuffer("select * from ");
		Table t = entity.getClass().getAnnotation(Table.class);
		if (t != null && StringUtils.isNotBlank(t.name())) {
			sql.append(t.name().trim());
		} else {
			sql.append(convertor.classToTableName(entity.getClass().getName()));
		}
		sql.append(" where ");
		EntitySql result = new EntitySql();
		result.setType(SqlType.SELECT);
		Class<?> curClazz = entity.getClass();
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
						Object fieldValue = null;
						try {
							fieldValue = FieldUtils.readField(f, entity, true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (fieldValue == null) {
							continue;// 如果没有值，则这个字段不需要加入到sql中
						}
						Column col = f.getAnnotation(Column.class);
						if (col != null && StringUtils.isNotBlank(col.name())) {
							sql.append(col.name().trim());
						} else {
							sql.append(convertor.classToTableName(f.getName()));
						}
						sql.append("= ? and ");
						result.addArg(fieldValue);
					}
				}
			}
			curClazz = curClazz.getSuperclass();
		}
		if (result.getArgs().isEmpty()) {
			// 如果没有值，则表示没有条件，将where删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" where ")));
		} else {
			// 表示有值，将最后那个and删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" and ")));
		}
		return result;
	}

	public static EntitySql createInsertSQL(Object entity) {
		StringBuffer sql = new StringBuffer("insert into ");
		Table t = entity.getClass().getAnnotation(Table.class);
		if (t != null && StringUtils.isNotBlank(t.name())) {
			sql.append(t.name().trim());
		} else {
			sql.append(convertor.classToTableName(entity.getClass().getName()));
		}
		sql.append("(");// 属性开始
		// 获取所有的属性名与属性值！
		EntitySql result = new EntitySql();
		result.setType(SqlType.INSERT);
		Class<?> curClazz = entity.getClass();
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
						Object fieldValue = null;
						try {
							fieldValue = FieldUtils.readField(f, entity, true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						if (fieldValue == null) {
							continue;// 如果没有值，则这个字段不需要加入到sql中
						}
						Column col = f.getAnnotation(Column.class);
						if (col != null && StringUtils.isNotBlank(col.name())) {
							sql.append(col.name().trim());
						} else {
							sql.append(convertor.classToTableName(f.getName()));
						}
						sql.append(",");
						result.addArg(fieldValue);
					}
				}
			}
			curClazz = curClazz.getSuperclass();
		}
		if (result.getArgs().isEmpty()) {
			return new EntitySql();// 没有值，则说明没有属性！
		}
		sql = sql.replace(sql.length() - 1, sql.length(), "");
		sql.append(") values (");// 属性结束
		for (int i = 0; i < result.getArgs().size(); i++) {
			sql.append("?,");
		}
		sql = sql.replace(sql.length() - 1, sql.length(), "");
		sql.append(")");// 属性结束
		result.setSql(sql.toString());
		return result;
	}
}