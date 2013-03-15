package com.tfc.system.relationship.dao;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import com.tfc.system.relationship.entity.SystemInfo;
import com.tfc.system.relationship.utils.ClassUtils;

public class HibernateDao {
	protected static Properties pro = new Properties();
	private static SessionFactory sessionFactory;

	static {
		pro.put(Environment.URL, "jdbc:mysql://localhost:3306/relationship?useUnicode=true&characterEncoding=UTF-8");
		pro.put(Environment.USER, "root");
		pro.put(Environment.PASS, "root");
		pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		if (sessionFactory == null) {
			// Hibernate的基本配置
			pro.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
			pro.put(Environment.CONNECTION_PROVIDER, DriverManagerConnectionProviderImpl.class.getName());

			// 使用c3p0连接池
			pro.put(Environment.C3P0_MIN_SIZE, "5");
			pro.put(Environment.C3P0_MAX_SIZE, "30");
			pro.put(Environment.C3P0_TIMEOUT, "60");// 1分钟
			pro.put(Environment.C3P0_MAX_STATEMENTS, "50");
			pro.put(Environment.C3P0_IDLE_TEST_PERIOD, "60");
			pro.put(Environment.C3P0_ACQUIRE_INCREMENT, "2");

			pro.put(Environment.AUTO_CLOSE_SESSION, "true");
			// 初始化Hibernate
			Configuration c = new Configuration();
			c.setProperties(pro);
			// 将实体类加载到Hibernate容器中！
			Set<Class<?>> clazzs = ClassUtils.getClasses(SystemInfo.class.getPackage());
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
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(pro).buildServiceRegistry();
			sessionFactory = c.buildSessionFactory(serviceRegistry);
		}
	}

	private static Session getCurrentSession() {
		return sessionFactory.openSession();
	}

	@SuppressWarnings("unchecked")
	public static <T> T getById(Class<T> entityClass, Serializable id) {
		Session s = getCurrentSession();
		T t = (T) s.get(entityClass, id);
		closeSession(s);
		return t;
	}

	private static void closeSession(Session s) {
		try {
			s.close();
			// s.disconnect();
		} catch (HibernateException e) {
		}
	}

	public static void save(Object entity) {
		Session s = getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.save(entity);
		tx.commit();
	}

	public static void delete(Object entity) {
		Session s = getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.delete(entity);
		tx.commit();
	}

	public static void update(Object entity) {
		Session s = getCurrentSession();
		Transaction tx = s.beginTransaction();
		s.update(entity);
		tx.commit();
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getAll(Class<T> clazz) {
		String entityName = clazz.getSimpleName();
		Session s = getCurrentSession();
		List<T> result = s.createQuery("from " + entityName).list();
		closeSession(s);
		return result;
	}

	public static long count(String hql, Object... args) {
		hql = StringUtils.trim(hql);
		if (hql == null || hql.isEmpty()) {
			return 0;
		}
		if (!hql.toLowerCase().replaceAll("[ ]+", " ").startsWith("select count(")) {
			hql = "select count(*) " + hql;
		}
		Session s = getCurrentSession();
		Query q = s.createQuery(hql);
		setParameters(q, args);
		long result = (Long) q.uniqueResult();
		closeSession(s);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> get(String hql, Object... args) {
		Session s = getCurrentSession();
		Query q = s.createQuery(hql);
		setParameters(q, args);
		List<T> result = q.list();
		closeSession(s);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unique(String hql, Object... args) {
		Session s = getCurrentSession();
		Query q = s.createQuery(hql);
		setParameters(q, args);
		T result = (T) q.uniqueResult();
		closeSession(s);
		return result;
	}

	public static void executeHql(String hql, Object... args) {
		Session s = getCurrentSession();
		Query q = s.createQuery(hql);
		setParameters(q, args);
		q.executeUpdate();
		closeSession(s);
	}

	private static void setParameters(Query q, Object... args) {
		if (args != null && args.length > 0) {
			int i = 0;
			for (Object arg : args) {
				q.setParameter(i++, arg);
			}
		}
	}
}
