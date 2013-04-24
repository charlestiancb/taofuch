package com.ss.language.model.data;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import com.ss.language.model.data.entity.WordIdf;

public class HibernateDataSource extends DatabaseConfig {
	private static SessionFactory sessionFactory;
	private static Properties pro = new Properties();

	public HibernateDataSource() {
		if (sessionFactory == null) {
			pro.put(Environment.URL, "jdbc:mysql://localhost:3306/weibo?useUnicode=true&characterEncoding=UTF-8");
			pro.put(Environment.USER, "root");
			pro.put(Environment.PASS, "root");
			pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
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
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(pro).buildServiceRegistry();
			sessionFactory = c.buildSessionFactory(serviceRegistry);
		}
	}
}
