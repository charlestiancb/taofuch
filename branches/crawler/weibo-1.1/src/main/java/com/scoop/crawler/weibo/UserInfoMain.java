package com.scoop.crawler.weibo;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.JdbcDataSource;
import com.scoop.crawler.weibo.repository.entity.EntitySql;
import com.scoop.crawler.weibo.repository.entity.EntitySql.SqlType;
import com.scoop.crawler.weibo.repository.mysql.Comment;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.RequestFailedHandler;
import com.scoop.crawler.weibo.runnable.WeiboUserRelationRunnable;

public class UserInfoMain {

	protected static DefaultHttpClient client = null;
	protected static JdbcDataSource dataSource = null;
	protected static RequestFailedHandler handler;

	public static void main(String[] args) {

		client = SinaWeiboRequest.getHttpClient("sszcgfss@gmail.com",
				"jmi2009095");
		if (dataSource == null) {
			dataSource = new JdbcDataSource();
		}
		if (handler == null) {
			handler = new RequestFailedHandler(client, dataSource);
		}
		handler.reTry();
		// 微博中的用户信息
		String sql = "select * from weibo_info where user_id not in (select user_id from user) limit 1,1";
		EntitySql esql = new EntitySql();
		esql.setSql(sql);
		esql.setType(SqlType.SELECT);
		for (List<Weibo> ws = dataSource.query(esql, Weibo.class); ws != null
				&& ws.size() > 0; ws = dataSource.query(esql, Weibo.class)) {
			Weibo w = ws.get(0);
			dataSource.savePerson(new WeiboPersonInfo("http://weibo.com/"
					+ w.getUserId() + "/info", client));
		}
		// 微博评论中的用户信息
		sql = "select * from weibo_comment where user_id not in (select user_id from user) limit 1,1";
		esql = new EntitySql();
		esql.setSql(sql);
		esql.setType(SqlType.SELECT);
		for (List<Comment> ws = dataSource.query(esql, Comment.class); ws != null
				&& ws.size() > 0; ws = dataSource.query(esql, Comment.class)) {
			Comment w = ws.get(0);
			dataSource.savePerson(new WeiboPersonInfo("http://weibo.com/"
					+ w.getUserId() + "/info", client));
		}
		// 抓取粉丝与关注信息
		WeiboUserRelationRunnable userRun = new WeiboUserRelationRunnable(
				dataSource, handler);
		userRun.run();
		handler.reTry();
	}
}
