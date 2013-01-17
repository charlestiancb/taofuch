package com.scoop.crawler.weibo.repository.entity;

import java.util.List;

public class EntitySql {
	private String sql;
	private List<Object> args;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}
}
