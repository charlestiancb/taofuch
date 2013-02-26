package com.tfc.word.auto.collect.config;

import com.tfc.word.auto.collect.repository.JdbcRepository;
import com.tfc.word.auto.collect.repository.Repository;

public class Configuration {
	/** 最多可以有几个线程同时在学习！ */
	public static int THREAD_COUNT = 5;
	/** 自动审核通过的数字！ */
	public static int AUTO_CHECKED_NUM = 20;
	/** 存储仓库 */
	public static Repository repo = new JdbcRepository();
}
