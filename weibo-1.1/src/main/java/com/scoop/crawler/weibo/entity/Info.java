package com.scoop.crawler.weibo.entity;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.util.JSONUtils;

public abstract class Info {
	protected boolean hasInit = false;
	/** 具体微博的具体url */
	protected String url = "";
	/** 当前微博的信息是否可用 */
	protected boolean valid = true;
	/** 当前微博的具体HTML完整内容 */
	protected String contentHtml = "";
	protected String detailEnd = ")</script>";
	protected DefaultHttpClient client;
	private FailedHandler handler;

	public FailedHandler getHandler() {
		return handler;
	}

	public void setHandler(FailedHandler handler) {
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

	/** 当前微博的信息是否可用 */
	public boolean isValid() {
		return valid;
	}

	public String getUrl() {
		return url;
	}
}
