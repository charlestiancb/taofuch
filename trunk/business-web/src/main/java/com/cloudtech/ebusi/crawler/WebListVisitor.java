package com.cloudtech.ebusi.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

/**
 * 网站列表搜索器
 * 
 * @author taofucheng
 * 
 */
public class WebListVisitor implements PageVisitor {
	private final String domain;

	public WebListVisitor(String baseUrl) {
		if ((baseUrl == null) || (baseUrl.trim().length() == 0)) {
			throw new IllegalArgumentException("baseUrl cannot be null or empty");
		}
		Matcher matcher = Pattern.compile("(http://[^/]+)").matcher(baseUrl);
		if (!matcher.find()) {
			throw new IllegalArgumentException("baseUrl must match http://[^/]+");
		}
		domain = matcher.group(1) + "/";
	}

	public boolean followUrl(final Url url) {
		if (url == null) {
			return false;
		}
		return url.link().startsWith(domain);
	}

	public void onError(final Url url, final Status statusError) {
		// TODO 错误时的处理方式，记录已经瓟取过的页面！
	}

	public void visit(final Page page) {
		// 成功读取页面时的操作。
		// TODO 将内容解析、建索引、记录已经瓟取过的页面！
	}
}
