package com.scoop.crawler.weibo.repository.mysql;

import com.scoop.crawler.weibo.fetch.info.OneWeiboInfo;
import com.scoop.crawler.weibo.fetch.info.WeiboComment;
import com.scoop.crawler.weibo.fetch.info.WeiboPersonInfo;

/**
 * 实体类之间的转换！
 * 
 * @author taofucheng
 * 
 */
public class EntityTransfer {
	public static Weibo parseWeibo(OneWeiboInfo weibo) {
		Weibo w = new Weibo();
		w.setWeiboId(weibo.getId());
		w.setContent(weibo.getMsg());
		w.setOrign(weibo.getOrigin());
		w.setPublishTime(weibo.getPublishTime());
		w.setUrl(weibo.getUrl());
		w.setCommentNum(Long.valueOf(weibo.getCommentNum()));
		w.setForwordNum(Long.valueOf(weibo.getRwNum()));
		w.setUserId(weibo.getPublisher().getId());
		return w;
	}

	public static User parseUser(WeiboPersonInfo person) {
		User user = new User();
		user.setUserId(person.getId());
		user.setName(person.getName());
		user.setUrl(person.getUrl());
		user.setIntroduce(person.getIntro());
		user.setFavor(person.getFavorite());
		user.setInfo(person.getInfo());
		user.setTagInfo(person.getTagInfo());
		user.setWeiboNum(Long.valueOf(person.getPublishNum()));
		user.setFansNum(Long.valueOf(person.getFansNum()));
		user.setFollowNum(Long.valueOf(person.getFollowNum()));
		return user;
	}

	public static Comment parseComment(WeiboComment comment) {
		Comment c = new Comment();
		c.setCommentId(comment.getId());
		c.setContent(comment.getContent());
		c.setPublishTime(comment.getPublishTime());
		c.setUserId(comment.getPerson().getId());
		c.setWeiboId(comment.getWeiboId());
		return c;
	}
}
