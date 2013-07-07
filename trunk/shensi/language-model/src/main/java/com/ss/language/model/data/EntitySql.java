package com.ss.language.model.data;

import java.util.ArrayList;
import java.util.List;

public class EntitySql {
	private String sql;
	private List<Object> args;
	private SqlType type;

	public String getSql() {
		return sql == null ? "" : sql.trim();
	}

	public EntitySql setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public List<Object> getArgs() {
		if (args == null) {
			args = new ArrayList<Object>();
		}
		return args;
	}

	public EntitySql setArgs(List<Object> args) {
		this.args = args;
		return this;
	}

	public EntitySql addArg(Object arg) {
		getArgs().add(arg);
		return this;
	}

	public String toString() {
		return "[" + getSql() + "] args: " + getArgs();
	}

	public SqlType getType() {
		return type;
	}

	public EntitySql setType(SqlType type) {
		this.type = type;
		return this;
	}

	public enum SqlType {
		INSERT, UPDATE, SELECT, DELETE
	}
}