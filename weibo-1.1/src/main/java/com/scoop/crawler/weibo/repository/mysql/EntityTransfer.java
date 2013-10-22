package com.scoop.crawler.weibo.repository.mysql;

import org.apache.commons.lang.StringUtils;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 实体类之间的转换！
 * 
 * @author taofucheng
 * 
 */
public class EntityTransfer {
	public static String parseWeiboId(OneWeiboInfo weibo) {
		return weibo.getPublisher().getId() + "_" + weibo.getId();
	}

	public static Weibo parseWeibo(OneWeiboInfo weibo) {
		Weibo w = new Weibo();
		w.setWeiboId(parseWeiboId(weibo));
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
		String name = person.getName();
		if (StringUtils.isEmpty(name)) {
			Logger.log("该用户[" + person.getId() + "]信息不全，拒绝保存！");
			throw new RuntimeException();
		}
		user.setName(name);
		user.setUrl(person.getUrl());
		user.setAddr(person.getAddr());
		user.setBirthday(person.getBirthday());
		user.setBlog(person.getBlog());
		user.setCompany(person.getCompany());
		user.setEmail(person.getEmail());
		user.setGender(person.getGender());
		user.setIntroduce(person.getIntroduce());
		user.setUniversity(person.getUniversity());
		user.setTagInfo(person.getTagInfo());
		user.setWeiboNum(Long.valueOf(person.getPublishNum()));
		user.setFansNum(Long.valueOf(person.getFansNum()));
		user.setFollowNum(Long.valueOf(person.getFollowNum()));
		if (person.isNeedFetchRelation()) {
			user.setHasRelation("0");// 表示需要抓取关系信息
		} else {
			user.setHasRelation("1");// 表示不需要抓取关系信息
		}
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
