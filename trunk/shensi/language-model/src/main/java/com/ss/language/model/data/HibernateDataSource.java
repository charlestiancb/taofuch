package com.ss.language.model.data;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import com.ss.language.model.data.entity.WordIdf;
import com.ss.language.model.utils.ClassUtils;

public class HibernateDataSource extends DatabaseConfig {
	private static SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	public HibernateDataSource() {
		if (sessionFactory == null) {
			// Hibernate的基本配置
			pro.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
			pro.put(Environment.CONNECTION_PROVIDER, DriverManagerConnectionProviderImpl.class.getName());
			pro.put(Environment.POOL_SIZE, 10);
			pro.put(Environment.AUTO_CLOSE_SESSION, true);
			// 初始化Hibernate
			Configuration c = new Configuration();
			c.setProperties(pro);
			// 将实体类加载到Hibernate容器中！
			Set<Class<?>> clazzs = ClassUtils.getClasses(WordIdf.class.getPackage());
			if (clazzs != null && !clazzs.isEmpty()) {
				for (Class<?> clazz : clazzs) {
					if (Serializable.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
						c = c.addAnnotatedClass(clazz);
					}
				}
			}
			// 字段自动转换与数据库保持一致
			c.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
			// 创建SessionFactory！
			sessionFactory = c.buildSessionFactory();
		}
	}

	public <T> void save(T obj) {
		Session s = getCurrentSession();
		Transaction t = s.beginTransaction();
		try {
			s.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.commit();
	}

	public <T> void merge(T obj) {
		Session s = getCurrentSession();
		Transaction t = s.beginTransaction();
		try {
			s.merge(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.commit();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(Class<T> entityClass, int firstRow, int maxRows) {
		Session s = getCurrentSession();
		List<T> result = s.createQuery("from " + entityClass.getSimpleName())
							.setFirstResult(firstRow)
							.setMaxResults(maxRows)
							.list();
		s.close();
		return result;
	}

	protected Session getCurrentSession() {
		return sessionFactory.openSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
