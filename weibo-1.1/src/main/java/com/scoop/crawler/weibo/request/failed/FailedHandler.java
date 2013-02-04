package com.scoop.crawler.weibo.request.failed;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;

public abstract class FailedHandler {
	private DefaultHttpClient client;
	private DataSource dataSource;

	public FailedHandler(DefaultHttpClient client, DataSource dataSource) {
		this.client = client;
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 记录失败的url请求
	 * 
	 * @param request
	 */
	public abstract void record(FailedRequest request);

	/**
	 * 将失败的记录重新请求一次！
	 */
	public abstract void reTry();

	public DefaultHttpClient getClient() {
		return client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}
}
