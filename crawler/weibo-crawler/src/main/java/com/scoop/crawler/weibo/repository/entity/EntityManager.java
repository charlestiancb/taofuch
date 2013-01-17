package com.scoop.crawler.weibo.repository.entity;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

import com.scoop.crawler.weibo.repository.mysql.Weibo;

public class EntityManager {
	private static final NamingStrategy convertor = ImprovedNamingStrategy.INSTANCE;

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
						Column col = f.getAnnotation(Column.class);
						if (col != null && StringUtils.isNotBlank(col.name())) {
							sql.append(col.name().trim());
						} else {
							sql.append(convertor.classToTableName(f.getName()));
						}
						sql.append(",");
						try {
							result.addArg(FieldUtils.readField(f, entity, true));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							result.addArg(null);
						}
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