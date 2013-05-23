package com.scoop.crawler.weibo.entity;

import java.io.Serializable;

public class Query implements Serializable {
	private static final long serialVersionUID = -3680276057161285380L;
	private String queryStr;
	private String collectionName;

	public Query() {
	}

	public Query(String queryStr, String collectionName) {
		setQueryStr(queryStr);
		setCollectionName(collectionName);
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}
