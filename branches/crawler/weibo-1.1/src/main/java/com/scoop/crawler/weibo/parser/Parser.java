package com.scoop.crawler.weibo.parser;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.ThreadUtils;

public abstract class Parser {
	protected DataSource dataSource;
	private FailedHandler handler;
	private DefaultHttpClient client;
	protected String detailEnd = ")</script>";

	public Parser(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	/**
	 * 分析出指定模块的html代码
	 * 
	 * @param start
	 * @return
	 */
	protected String cut(String html, String start) {
		String content = "";
		int idx = html.indexOf(start);
		if (idx > -1) {
			content = html.substring(idx + start.length());
			idx = content.indexOf(detailEnd);
			content = content.substring(0, idx);
			content = "{" + content;
			return JSONUtils.getSinaHtml(content);
		}
		return "";
	}

	public FailedHandler getHandler() {
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
