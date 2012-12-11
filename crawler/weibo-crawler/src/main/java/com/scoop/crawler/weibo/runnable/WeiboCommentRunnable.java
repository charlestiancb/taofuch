package com.scoop.crawler.weibo.runnable;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.JSONUtils;

public abstract class WeiboCommentRunnable extends Thread implements Runnable {
	protected DataSource dataSource;
	protected final OneWeiboInfo weibo;

	public WeiboCommentRunnable(DataSource dataSource, OneWeiboInfo weibo) {
		this.dataSource = dataSource;
		this.weibo = weibo;
	}

	protected Elements loadNextPage(Comments comments, DefaultHttpClient client) {
		// 这里取下一页的URL是否正确！
		Elements eles = Jsoup.parse(comments.getCurrentPage()).getElementsByAttributeValue("class",
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
				String html = SinaWeiboRequest.request(client, url, weibo.getHandler(), FailedNode.COMMENT);
				comments.setCurrentPage(JSONUtils.getSinaHtml(html));
				eles = Jsoup.parse(comments.getCurrentPage()).getElementsByAttributeValue("class",
						"comment_list W_linecolor clearfix");
				if (eles != null) {
					return eles;
				}
			}
		}
		return null;
	}

	/**
	 * 获取个人信息的URL。
	 * 
	 * @param tmp
	 * @param weiboUrl
	 * @return
	 */
	protected String parseToUrl(Elements tmp, String weiboUrl) {
		Elements eles = tmp.get(0).getElementsByTag("a");
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
