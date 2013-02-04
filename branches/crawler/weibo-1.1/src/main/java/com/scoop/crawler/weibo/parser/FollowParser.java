package com.scoop.crawler.weibo.parser;

import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.failed.FailedHandler;

/**
 * 用户关注信息解析器
 * 
 * @author taofucheng
 * 
 */
public class FollowParser extends UserRelationParser {

	public FollowParser(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

}
