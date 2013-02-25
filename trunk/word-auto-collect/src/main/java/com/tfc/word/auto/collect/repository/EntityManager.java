package com.tfc.word.auto.collect.repository;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import com.tfc.word.auto.collect.repository.EntitySql.SqlType;
import com.tfc.word.auto.collect.repository.convertor.ImprovedNamingStrategy;

public class EntityManager {
	public static final ImprovedNamingStrategy convertor = ImprovedNamingStrategy.INSTANCE;

	public static EntitySql createUpdateSQL(Object value, Object where) {
		StringBuffer sql = new StringBuffer("update ");
		Table t = value.getClass().getAnnotation(Table.class);
		if (t != null && StringUtils.isNotBlank(t.name())) {
			sql.append(t.name().trim());
		} else {
			sql.append(convertor.classToTableName(value.getClass().getName()));
		}
		sql.append(" set ");
		EntitySql result = new EntitySql();
		result.setType(SqlType.UPDATE);
		processField(value, sql, result, "= ? , ");
		// 判断是否有值。如果没有，则sql不正确。
		if (result.getArgs().isEmpty()) {
			return null;
		} else {
			sql = sql.replace(sql.length() - 2, sql.length(), "");
		}
		int paramNum = result.getArgs().size();
		sql.append(" where ");
		processField(where, sql, result, "=? and ");
		if (result.getArgs().size() == paramNum) {
			// 如果没有值，则表示没有条件，将where删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" where ")));
		} else {
			// 表示有值，将最后那个and删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" and ")));
		}
		return result;
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
		return createWhere(entity, sql);
	}

	private static void processField(Object entity, StringBuffer sql, EntitySql result, String fieldCondition) {
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
						sql.append(fieldCondition);
						result.addArg(fieldValue);
					}
				}
			}
			curClazz = curClazz.getSuperclass();
		}
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
		processField(entity, sql, result, ",");
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

	/**
	 * 根据对象创建一个删除SQL。其所有有值的字段将被作为where条件！
	 * 
	 * @param first
	 * @return
	 */
	public static EntitySql createDeleteSQL(Object entity) {
		StringBuffer sql = new StringBuffer("delete from ");
		Table t = entity.getClass().getAnnotation(Table.class);
		if (t != null && StringUtils.isNotBlank(t.name())) {
			sql.append(t.name().trim());
		} else {
			sql.append(convertor.classToTableName(entity.getClass().getName()));
		}
		EntitySql result = createWhere(entity, sql);
		if (result != null) {
			result.setType(SqlType.DELETE);
		}
		return result;
	}

	private static EntitySql createWhere(Object entity, StringBuffer sql) {
		sql.append(" where ");
		EntitySql result = new EntitySql();
		result.setType(SqlType.SELECT);
		processField(entity, sql, result, "= ? and ");
		if (result.getArgs().isEmpty()) {
			// 如果没有值，则表示没有条件，将where删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" where ")));
		} else {
			// 表示有值，将最后那个and删除
			result.setSql(sql.substring(0, sql.lastIndexOf(" and ")));
		}
		return result;
	}
}