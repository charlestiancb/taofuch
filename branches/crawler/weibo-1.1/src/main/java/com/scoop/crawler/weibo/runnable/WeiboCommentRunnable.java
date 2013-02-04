package com.scoop.crawler.weibo.runnable;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.ThreadUtils;

public class WeiboCommentRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected FailedHandler handler;

	public WeiboCommentRunnable(DataSource dataSource, FailedHandler handler) {
		this.dataSource = dataSource;
		this.handler = handler;
	}

	public void run() {
		DefaultHttpClient client = ThreadUtils.allocateHttpClient();
		for (Weibo w = dataSource.getOneUnfetchedWeibo(); w != null; w = dataSource.getOneUnfetchedWeibo()) {
			OneWeiboInfo wb = new OneWeiboInfo(w.getUrl(), client);
			wb.setHandler(handler);
			try {
				System.out.println("解析评论信息……");
				// 获取所有评论信息，并进行循环处理。
				Elements eles = wb.getDetailDoc().getElementsByAttributeValue("class", "comment_lists").select("dd");
				Comments comments = new Comments(wb.getDetail());
				while (eles != null && eles.size() > 0) {
					Element tmp = null;
					for (int i = 0; i < eles.size(); i++) {
						System.out.println("解析其中一条评论信息……");
						tmp = eles.get(i);
						if (tmp != null) {
							// 获取对应的评论者主页URL。
							try {
								String userInfoUrl = parseToUrl(tmp, wb.getUrl());
								WeiboPersonInfo person = new WeiboPersonInfo(userInfoUrl, client);
								person.setHandler(wb.getHandler());
								WeiboComment comment = new WeiboComment(tmp);
								comment.setHandler(wb.getHandler());
								comment.setWeiboId(wb.getId());
								comment.setPerson(person);
								dataSource.saveComment(comment);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// 加载下一页评论，并进行分析
					eles = loadNextPage(comments, client);
				}
			} catch (Exception e) {
				System.err.println("解析微博[" + wb + "]的评论失败！");
				e.printStackTrace();
			}
		}
		// 将线程释放
		ThreadUtils.freeThread();
		ThreadUtils.finishComment();
	}

	protected Elements loadNextPage(Comments comments, DefaultHttpClient client) {
		// 这里取下一页的URL是否正确！
		Elements eles = Jsoup.parse(comments.getCurrentPage()).getElementsByAttributeValue(	"class",
																							"W_pages W_pages_comment");
		if (eles != null && eles.size() > 0) {
			eles = eles.first().getElementsMatchingOwnText("下一页");
		}
		if (eles != null && eles.size() > 0) {
			// 分页的URL：http://weibo.com/aj/comment/big?id=3488459005692332&max_id=3488471672960730&page=3&_t=0&__rnd=1347199980913
			String url = "http://weibo.com/aj/comment/big?";
			String param = eles.first().attr("action-data");
			if (param != null && param.trim().length() > 0) {
				url = url + param;
				String html = SinaWeiboRequest.request(client, url, handler, FailedNode.COMMENT);
				comments.setCurrentPage(JSONUtils.getSinaHtml(html));
				eles = Jsoup.parse(comments.getCurrentPage())
							.getElementsByAttributeValue("class", "comment_list W_linecolor clearfix");
				if (eles != null) {
					System.out.println("获取下一 页评论信息……");
					return eles;
				}
			}
		}
		System.out.println("当前微博评论信息读取完毕！");
		return null;
	}

	/**
	 * 获取个人信息的URL。
	 * 
	 * @param tmp
	 * @param weiboUrl
	 * @return
	 */
	protected String parseToUrl(Element tmp, String weiboUrl) {
		Elements eles = tmp.getElementsByTag("a");
		String userInfoUrl = eles.get(0).attr("href");
		userInfoUrl = userInfoUrl.startsWith("http://") ? userInfoUrl
				: (userInfoUrl.startsWith("/") ? "http://weibo.com" + userInfoUrl : weiboUrl + userInfoUrl);
		return userInfoUrl;
	}

	/**
	 * 评论内容
	 * 
	 * @param tmp
	 * @return
	 */
	protected String parseToMsg(Elements tmp) {
		boolean hasContent = false;// 是否是评论内容。
		String comment = "";// 评论的内容
		List<TextNode> nl = tmp.first().textNodes();
		for (int i = 0; i < nl.size(); i++) {
			TextNode n = nl.get(i);
			String t = StringUtils.trim(n.text());
			if (StringUtils.isBlank(t)) {
				continue;
			}
			if (hasContent) {
				// 如果已经是评论，则这个文本内容时，表示结束了！
				comment += t;
				break;
			} else {
				// 如果还没有找到评论内容，且这是文本信息，即：首次发现文本信息，则说明这是评论内容的开始！
				hasContent = true;
				t = t.startsWith("：") ? t.substring(1) : t;// 去除评论者后面的冒号
				comment += t;
			}
		}
		return comment;
	}

	/**
	 * 一个完整页面的评论信息，其中包含当前页的评论和下一页的评论
	 * 
	 * @author taofucheng
	 * 
	 */
	public class Comments {
		/** 当前一整页的所有评论信息的页面！ */
		private String currentPage;

		/**
		 * 一个完整页面的评论信息，其中包含当前页的评论和下一页的评论
		 */
		public Comments(String currentPage) {
			this.currentPage = currentPage;
		}

		public String getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(String currentPage) {
			this.currentPage = currentPage;
		}
	}
}
