package com.scoop.crawler.weibo.repository;

import java.util.Properties;

import org.hibernate.cfg.Environment;

public abstract class DatabaseDataSource implements DataSource {
	protected Properties pro = new Properties();

	public DatabaseDataSource() {
		pro.put(Environment.URL, "jdbc:mysql://localhost:3306/weibo?useUnicode=true&characterEncoding=UTF-8");
		pro.put(Environment.USER, "root");
		pro.put(Environment.PASS, "root");
		pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
	}
}
