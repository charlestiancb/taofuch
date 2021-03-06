package com.scoop.crawler.weibo.repository;

import java.util.List;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.repository.mysql.Weibo;

public interface DataSource {
	/**
	 * 保存微博信息。
	 * 
	 * @param weibo
	 */
	public void saveWeibo(OneWeiboInfo weibo);

	/** 获取一条没有抓取解析信息的微博 */
	public Weibo getOneUnfetchedWeibo();

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

	/***
	 * 获取一条没有抓取关系信息的用户
	 * 
	 * @return
	 */
	public User getOneUnfetchedUser();

	public void saveFans(String id, List<WeiboPersonInfo> fans);

	public void saveFollows(String id, List<WeiboPersonInfo> follows);
}
