package com.scoop.crawler.weibo.repository;

import com.scoop.crawler.weibo.fetch.info.OneWeiboInfo;
import com.scoop.crawler.weibo.fetch.info.WeiboComment;
import com.scoop.crawler.weibo.fetch.info.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;

public interface DataSource {
	/**
	 * 保存微博信息。
	 * 
	 * @param weibo
	 */
	public void saveWeibo(OneWeiboInfo weibo);

	/**
	 * 关闭
	 */
	public void close();

	/**
	 * 保存微博评论信息
	 * 
	 * @param comment
	 */
	public void saveComment(WeiboComment comment);

	/**
	 * 保存用户信息
	 * 
	 * @param person
	 */
	public void savePerson(WeiboPersonInfo person);

	public boolean isWeiboExists(String weiboId);

	public boolean isCommentExists(String commentId);

	public boolean isUserExists(String userId);

	/**
	 * 保存失败的请求！
	 * 
	 * @param request
	 */
	public void saveFailedRequest(FailedRequest request);

	/**
	 * 加载第一条失败的请求！加载之后，同时删除！
	 * 
	 * @return
	 */
	public FailedRequest pop();
}
