package com.cloudtech.ebusi.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.vidageek.crawler.Page;
import net.vidageek.crawler.PageVisitor;
import net.vidageek.crawler.Status;
import net.vidageek.crawler.Url;

import com.cloudtech.ebusi.crawler.parser.Parser;

/**
 * 网站列表搜索器
 * 
 * @author taofucheng
 * 
 */
public class WebListVisitor implements PageVisitor {
	private final String domain;
	private final Parser parse;

	public WebListVisitor(String baseUrl, Parser parse) {
		if ((baseUrl == null) || (baseUrl.trim().length() == 0)) {
			throw new IllegalArgumentException("baseUrl cannot be null or empty");
		}
		Matcher matcher = Pattern.compile("(http://[^/]+)").matcher(baseUrl);
		if (!matcher.find()) {
			throw new IllegalArgumentException("baseUrl must match http://[^/]+");
		}
		domain = matcher.group(1) + "/";
		this.parse = parse;
		if (this.parse == null) {
			throw new IllegalArgumentException("parse must not be null!!");
		}
	}

	public boolean followUrl(final Url url) {
		if (url == null) {
			return false;
		}
		return url.link().startsWith(domain) && parse.followUrl(url);
	}

	public void onError(final Url url, final Status statusError) {
		parse.parseError(url, statusError);
	}

	public void visit(final Page page) {
		parse.doParse(page);
	}
}
