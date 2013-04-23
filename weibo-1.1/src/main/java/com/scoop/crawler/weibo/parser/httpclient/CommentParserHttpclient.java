package com.scoop.crawler.weibo.parser.httpclient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.parser.CommentParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 微博评论解析器。不使用浏览器，直接使用HttpClient请求与解析。
 * 
 * @author taofucheng
 * 
 */
public class CommentParserHttpclient extends CommentParser {
	public CommentParserHttpclient(DataSource dataSource, FailedHandler handler) {
		super(dataSource, handler);
	}

	public void fetchWeiboComments(WebDriver driver, Weibo w, DefaultHttpClient client) {
		OneWeiboInfo wb = new OneWeiboInfo(w.getUrl(), client);
		wb.setHandler(getHandler());
		try {
			Logger.log("解析微博[" + w + "]的评论信息……");
			// 获取所有评论信息，并进行循环处理。
			Elements eles = wb.getDetailDoc().getElementsByAttributeValue("class", "comment_lists");
			if (eles == null || eles.isEmpty()) {
				Logger.log("当前微博没有评论内容！记录的微博评论的总数：" + w.getCommentNum() + "个");
				return;
			}
			eles = eles.select("dd");
			if (eles == null || eles.isEmpty()) {
				Logger.log("当前微博没有评论内容！记录的微博评论的总数：" + w.getCommentNum() + "个");
				return;
			}
			Comments comments = new Comments(wb.getDetail());
			int cnt = 0;
			while (eles != null && eles.size() > 0) {
				Element tmp = null;
				for (int i = 0; i < eles.size(); i++) {
					Logger.log("解析其中一条评论信息……");
					tmp = eles.get(i);
					if (tmp != null) {
						// 获取对应的评论者主页URL。
						try {
							String userInfoUrl = parseToUserUrl(tmp);
							WeiboPersonInfo person = new WeiboPersonInfo(userInfoUrl, client);
							person.setHandler(wb.getHandler());
							WeiboComment comment = new WeiboComment(tmp);
							comment.setHandler(wb.getHandler());
							comment.setWeiboId(w.getWeiboId());
							comment.setPerson(person);
							dataSource.saveComment(comment);
							cnt++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// 加载下一页评论，并进行分析
				eles = loadNextPage(comments, client);
			}
			afterSave(w, cnt);
		} catch (Exception e) {
			Logger.log("解析微博[" + wb + "]的评论失败！" + e);
		}
	}

	protected Elements loadNextPage(Comments comments, DefaultHttpClient client) {
		Logger.log("获取下一 页评论信息……");
		try {
			Thread.sleep(1000);// 等待一秒钟。
		} catch (Exception e) {
		}
		// 这里取下一页的URL是否正确！
		Elements eles = Jsoup.parse(comments.getCurrentPageComments()).getElementsByAttributeValue(	"class",
																									"W_pages_minibtn");
		if (eles != null && eles.size() > 0) {
			eles = eles.first().getElementsMatchingOwnText("下一页");
		}
		if (eles != null && eles.size() > 0) {
			// 分页的URL：http://weibo.com/aj/comment/big?id=3488459005692332&max_id=3488471672960730&page=3&_t=0&__rnd=1347199980913
			String url = "http://weibo.com/aj/comment/big?";
			String param = eles.first().attr("action-data");
			if (param != null && param.trim().length() > 0) {
				url = url + JSONUtils.unEscapeHtml(param);
				String html = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.COMMENT);
				comments.setCurrentPageComments(JSONUtils.getSinaHtml(html));
				eles = Jsoup.parse(comments.getCurrentPageComments()).getElementsByClass("comment_list");
				if (eles != null) {
					return eles.select("dd");
				}
			}
		}
		return null;
	}

	/**
	 * 一个完整页面的评论信息，其中包含当前页的评论和下一页的评论
	 * 
	 * @author taofucheng
	 * 
	 */
	public class Comments {
		/** 当前一整页的所有评论信息的页面！ */
		private String currentPageComments;

		/**
		 * 一个完整页面的评论信息，其中包含当前页的评论和下一页的评论
		 */
		public Comments(String currentPageComments) {
			this.currentPageComments = currentPageComments;
		}

		public String getCurrentPageComments() {
			return currentPageComments;
		}

		public void setCurrentPageComments(String currentPageComments) {
			this.currentPageComments = currentPageComments;
		}
	}
}
