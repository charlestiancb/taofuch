package com.scoop.crawler.weibo.parser;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

/**
 * 用户粉丝信息解析器。
 * 
 * @author taofucheng
 * 
 */
public class FansParser extends UserRelationParser {

	public FansParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

}
