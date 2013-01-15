package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.MysqlDataSource;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class MysqlRunnable extends WeiboCommentRunnable {

	public MysqlRunnable(DataSource dataSource, OneWeiboInfo weibo) {
		super(dataSource, weibo);
	}

	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		try {
			System.out.println("解析评论信息……");
			try {
				MysqlDataSource m = (MysqlDataSource) dataSource;
				System.out.println("目前数据库连接数：" + m.getSessionFactory().getStatistics().getConnectCount());
			} catch (Exception e1) {
			}
			// 获取所有评论信息，并进行循环处理。
			Elements eles = weibo.getDetailDoc().getElementsByAttributeValue("class", "comment_lists").select("dd");
			Comments comments = new Comments(weibo.getDetail());
			while (eles != null && eles.size() > 0) {
				Element tmp = null;
				for (int i = 0; i < eles.size(); i++) {
					System.out.println("解析其中一条评论信息……");
					tmp = eles.get(i);
					if (tmp != null) {
						// 获取对应的评论者主页URL。
						try {
							String userInfoUrl = parseToUrl(tmp, weibo.getUrl());
							WeiboPersonInfo person = new WeiboPersonInfo(userInfoUrl, client);
							person.setHandler(weibo.getHandler());
							WeiboComment comment = new WeiboComment(tmp);
							comment.setHandler(weibo.getHandler());
							comment.setWeiboId(weibo.getId());
							comment.setPerson(person);
							dataSource.saveComment(comment);
							dataSource.savePerson(person);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// 加载下一页评论，并进行分析
				eles = loadNextPage(comments, client);
			}
		} catch (Exception e) {
			System.err.println("解析微博[" + weibo + "]的评论失败！");
			e.printStackTrace();
		} finally {
			// 将线程释放
			ThreadUtils.freeThread();
		}
	}
}
