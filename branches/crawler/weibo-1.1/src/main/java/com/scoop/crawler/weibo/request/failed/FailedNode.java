package com.scoop.crawler.weibo.request.failed;

/**
 * 失败的节点
 * 
 * @author taofucheng
 * 
 */
public enum FailedNode {
	/** 总入口，即FetchSinaWeibo中调用的地方 */
	MAIN,
	/** 解析用户主页上发布的所有躲雨信息 */
	USER_WEIBO,
	/** 单条微博信息的抓取 */
	SINGLE_WEIBO,
	/** 用户信息抓取 */
	PERSON,
	/** 每条微博对应的所有评论 */
	COMMENT,
	/** 抓取用户的粉丝 */
	FANS,
	/** 抓取用户的粉丝与关注者 */
	FOLLOWS
}
