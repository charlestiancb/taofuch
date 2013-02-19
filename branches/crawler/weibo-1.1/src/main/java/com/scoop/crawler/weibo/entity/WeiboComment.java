package com.scoop.crawler.weibo.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.util.TimeExpUtils;

public class WeiboComment extends Info {
	/** 一条评论的完整的HTML */
	private Element tmp;
	/** 对应的微博编号 */
	private String weiboId;
	/** 评论内容 */
	private String content;
	/** 评论发布时间 */
	private String publishTime;
	/** 评论编号 */
	private String id;
	/** 评论者 */
	private WeiboPersonInfo person;

	public WeiboComment(Element comment) {
		tmp = comment;
	}

	public String getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	public String getContent() {
		if (content == null) {
			boolean hasContent = false;// 是否是评论内容。
			String comment = "";// 评论的内容
			List<TextNode> nl = tmp.textNodes();
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
			content = comment;
		}
		return content;
	}

	public String getPublishTime() {
		if (publishTime == null) {
			Elements nl = tmp.select("span").select(".S_txt2");
			if (nl != null && nl.size() > 0) {
				String timeStr = nl.first().text();
				timeStr = timeStr.startsWith("(") ? timeStr.substring(1) : timeStr;
				timeStr = timeStr.endsWith(")") ? timeStr.substring(0, timeStr.length() - 1) : timeStr;
				timeStr = timeStr.trim();
				publishTime = TimeExpUtils.abstractTime(timeStr);
			} else {
				publishTime = " ";
			}
		}
		return publishTime;
	}

	public void setPerson(WeiboPersonInfo person) {
		this.person = person;
	}

	public WeiboPersonInfo getPerson() {
		return person;
	}

	/** 微博评论编号 */
	public String getId() {
		if (id == null) {
			String commentId = tmp.getElementsMatchingOwnText("回复").last().attr("action-data");
			commentId = commentId.substring(commentId.indexOf("&cid=") + "&cid=".length());
			commentId = commentId.substring(0, commentId.indexOf("&"));
			id = weiboId + "$" + commentId;// weiboId将commentID固定成唯一的！
		}
		return id;
	}
}
