package com.scoop.crawler.weibo.repository.entity;

import java.util.ArrayList;
import java.util.List;

public class EntitySql {
	private String sql;
	private List<Object> args;
	private SqlType type;

	public String getSql() {
		return sql == null ? "" : sql.trim();
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getArgs() {
		if (args == null) {
			args = new ArrayList<Object>();
		}
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public void addArg(Object arg) {
		getArgs().add(arg);
	}

	public String toString() {
		return "[" + getSql() + "] args: " + getArgs();
	}

	public SqlType getType() {
		return type;
	}

	public void setType(SqlType type) {
		this.type = type;
	}

	public enum SqlType {
		INSERT, UPDATE, SELECT
	}
}