package com.scoop.crawler.weibo.fetch.info;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedNode;

/**
 * 抓取每条微博的详细信息，如评论、转发等。
 * 
 * @author taofucheng
 * 
 */
public class OneWeiboInfo extends Info {
	/** 当前微博的详细信息 */
	private String detail = null;
	private Document doc;

	// ========下面是微博中解析出来的各项内容！===============//
	/** 微博ID */
	private String id;
	/** 微博内容 */
	private String msg;
	/** 微博内容 */
	private String origin;
	/** 微博转发数 */
	private String rwNum;
	/** 微博评论数 */
	private String commentNum;
	private String publishTime;
	/** 发布者编号 */
	private WeiboPersonInfo publisher;

	/**
	 * 指定具体的微博信息，然后会判断内容是否存在，并将存在的微博内容请求出来备用！
	 * 
	 * @param url
	 * @param client
	 */
	public OneWeiboInfo(String url, DefaultHttpClient client) {
		this.url = url;
		this.client = client;
		if (url == null || url.trim().length() == 0) {
			valid = false;
		}
	}

	protected void requestIfNeccessory() {
		if (hasInit) {
			return;
		}
		try {
			hasInit = true;
			contentHtml = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.SINGLE_WEIBO);
			parseWeiboDetail();
		} catch (Exception e) {
			valid = false;
		}
	}

	private void parseWeiboDetail() {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"pl_content_weiboDetail\",";
		detail = cut(contentHtml, detailStart);
		doc = Jsoup.parse(detail);
	}

	/** 微博的消息ID */
	public String getId() {
		if (!valid) {
			return "";
		}
		if (id == null || "".equals(id)) {
			id = url.substring(url.lastIndexOf("/") + 1);
		}
		return id;
	}

	/** 微博内容 */
	public String getMsg() {
		if (!valid) {
			return "";
		}
		if (msg == null || "".equals(msg)) {
			requestIfNeccessory();
			try {
				Elements eles = doc.getElementsByClass("WB_text").select("p");
				if (eles == null || eles.isEmpty()) {
					// 兼容未升级的信息
					eles = doc.getElementsByClass("content").select("p");
				}
				if (eles != null && eles.size() > 0) {
					msg = StringUtils.trim(eles.get(0).text());
				} else {
					msg = " ";
				}
			} catch (Exception e) {
				msg = " ";// 使其等于空，为了不重复解析，加快速度
			}
		}
		return msg;
	}

	/** 微博来源 */
	public String getOrigin() {
		if (!valid) {
			return "";
		}
		if (origin == null || "".equals(origin)) {
			requestIfNeccessory();
			try {
				Elements eles = doc.getElementsByAttributeValue("class", "WB_from").select(".S_txt2");
				if (eles != null && eles.size() > 0) {
					// 找到“来自”那个标签，然后它的下一个元素就是具体的来自！
					origin = ((Element) eles.get(0).nextSibling()).text();
				} else {
					// 兼容未升级的
					eles = doc.getElementsByAttributeValue("class", "info W_linkb W_textb").select("a");
					if (eles != null && eles.size() > 0) {
						// 第4个元素就是来自
						origin = eles.get(3).text();
					} else {
						origin = " ";
					}
				}
			} catch (Exception e) {
				origin = " ";
			}
		}
		return origin;
	}

	/** 微博转发数 */
	public String getRwNum() {
		if (!valid) {
			return "";
		}
		if (rwNum == null || "".equals(rwNum)) {
			requestIfNeccessory();
			try {
				Elements eles = doc.getElementsByAttributeValue("class", "WB_handle")
									.first()
									.getElementsByAttributeValue("node-type", "forward_counter");
				if (eles == null || eles.isEmpty()) {
					// 兼容升级以前的
					eles = doc.getElementsByAttributeValue("class", "tab_c W_textb")
								.first()
								.getElementsByAttributeValue("node-type", "forward_counter");
				}
				if (eles != null && eles.size() > 0) {
					rwNum = eles.get(eles.size() - 1).text();
					rwNum = rwNum.substring(rwNum.indexOf("(") + 1, rwNum.indexOf(")"));
				} else {
					rwNum = "0";
				}
			} catch (Exception e) {
				rwNum = "0";
			}
		}
		return rwNum;
	}

	/** 微博评论数 */
	public String getCommentNum() {
		if (!valid) {
			return "";
		}
		if (commentNum == null || "".equals(commentNum)) {
			requestIfNeccessory();
			try {
				Elements eles = doc.getElementsByAttributeValue("class", "WB_handle")
									.first()
									.getElementsByAttributeValue("node-type", "comment_counter");
				if (eles == null || eles.isEmpty()) {
					eles = doc.getElementsByAttributeValue("class", "tab_c W_textb")
								.first()
								.getElementsByAttributeValue("node-type", "comment_counter");
				}
				if (eles != null && eles.size() > 0) {
					commentNum = eles.get(eles.size() - 1).text();
					commentNum = commentNum.substring(commentNum.indexOf("(") + 1, commentNum.indexOf(")"));
				} else {
					commentNum = "0";
				}
			} catch (Exception e) {
				commentNum = "0";
			}
		}
		return commentNum;
	}

	public String toString() {
		return url;
	}

	/** 微博详细信息，包括对应的评论信息 */
	public String getDetail() {
		return detail;
	}

	public Document getDetailDoc() {
		return doc;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public WeiboPersonInfo getPublisher() {
		if (!valid) {
			return new WeiboPersonInfo(null, null);
		}
		if (publisher == null) {
			requestIfNeccessory();
			try {
				String userId = contentHtml.substring(contentHtml.indexOf("$CONFIG['oid'] = '"));
				userId = userId.substring("$CONFIG['oid'] = '".length());
				userId = userId.substring(0, userId.indexOf("';"));
				publisher = new WeiboPersonInfo("http://weibo.com/u/" + userId, client);
				publisher.setHandler(getHandler());
				publisher.setId(userId);
			} catch (Exception e) {
				commentNum = "0";
			}
		}
		return publisher;
	}

}
