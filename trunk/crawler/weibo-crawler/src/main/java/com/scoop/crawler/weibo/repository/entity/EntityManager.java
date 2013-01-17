package com.scoop.crawler.weibo.repository.entity;

import com.scoop.crawler.weibo.repository.mysql.Weibo;

public class EntityManager {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Weibo weibo = new Weibo();
		weibo.setCommentNum(2L);
		weibo.setContent("这是测试内容哦~");
		weibo.setForwordNum(3L);
		weibo.setOrign("手工");
		weibo.setPublishTime("2012-11-27");
		weibo.setUrl("http://adfasdfasd");
		weibo.setUserId("adfXaADSZxa-adax");
		weibo.setWeiboId("asdfasdfDSdjgh");
		System.out.println(createInsertSQL(weibo));
	}

	public static String createInsertSQL(Object weibo) {
		// TODO Auto-generated method stub
		return null;
	}

}
