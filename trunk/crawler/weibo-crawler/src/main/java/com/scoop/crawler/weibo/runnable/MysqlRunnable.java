package com.scoop.crawler.weibo.runnable;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.fetch.info.OneWeiboInfo;
import com.scoop.crawler.weibo.fetch.info.WeiboComment;
import com.scoop.crawler.weibo.fetch.info.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class MysqlRunnable extends WeiboCommentRunnable {

	public MysqlRunnable(DataSource dataSource, OneWeiboInfo weibo) {
		super(dataSource, weibo);
	}

	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		try {
			System.out.println("解析评论信息……");
			// 获取所有评论信息，并进行循环处理。
			Elements eles = weibo.getDetailDoc().getElementsByAttributeValue(	"class",
																				"comment_list W_linecolor clearfix");
			Comments comments = new Comments(weibo.getDetail());
			while (eles != null && eles.size() > 0) {
				Elements tmp = null;
				for (int i = 0; i < eles.size(); i++) {
					System.out.println("解析其中一条评论信息……");
					tmp = eles.get(i).getElementsByTag("dd");
					if (tmp != null && tmp.size() > 0) {
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
