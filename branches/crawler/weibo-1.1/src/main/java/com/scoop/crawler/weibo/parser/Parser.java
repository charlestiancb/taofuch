package com.scoop.crawler.weibo.parser;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.util.ThreadUtils;

public abstract class Parser {
	protected DataSource dataSource;
	private RequestFailedHandler handler;
	private DefaultHttpClient client;

	public Parser(DataSource dataSource, RequestFailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public RequestFailedHandler getHandler() {
		return handler;
	}

	public void setHandler(RequestFailedHandler handler) {
		this.handler = handler;
	}

	public DefaultHttpClient getClient() {
		return client == null ? ThreadUtils.allocateHttpClient() : client;
	}

	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}
}
