package com.scoop.crawler.weibo.parser;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

public class UserRelationParser extends Parser {

	public UserRelationParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

}
