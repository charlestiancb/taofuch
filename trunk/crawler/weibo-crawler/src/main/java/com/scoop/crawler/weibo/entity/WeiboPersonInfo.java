package com.scoop.crawler.weibo.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedNode;

/**
 * 抓取每个微博个人首页的详细信息，如姓名、所在地等。。
 * 
 * @author taofucheng
 * 
 */
public class WeiboPersonInfo extends Info {
	private Document _doc;
	private Document doc_base;
	private Document doc_stat;
	// ========下面是微博中解析出来的各项内容！===============//
	/** 微博主人的ID */
	private String id;
	/** 微博主人的姓名 */
	private String name;
	/** 所在地 */
	private String addr;
	/** 兴趣爱好 */
	private String favorite;
	/** 简介 */
	private String intro;
	/** 关注数 */
	private String followNum;
	/** 粉丝数 */
	private String fansNum;
	/** 发布微博数 */
	private String publishNum;
	/** 个人的标签信息 */
	private String tagInfo;
	/** 粉丝的编号 */
	private List<WeiboPersonInfo> fans;
	/** 被关注者编号 */
	private List<WeiboPersonInfo> follows;

	/**
	 * 指定具体的微博信息，然后会判断内容是否存在，并将存在的微博内容请求出来备用！
	 * 
	 * @param url
	 * @param client
	 */
	public WeiboPersonInfo(String url, DefaultHttpClient client) {
		this.url = url;
		this.client = client;
		if (url == null || url.trim().length() == 0) {
			valid = false;
			return;
		}
	}

	private void initIfNeccessory() {
		if (hasInit) {
			// 如果已经初始化，则不再初始化
			return;
		}
		try {
			hasInit = true;
			getContentHtml();
			parseBaseInfo();
			parseStatisticInfo();
		} catch (Exception e) {
			valid = false;
		}
	}

	private void getContentHtml() {
		contentHtml = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.PERSON);
		if (contentHtml.indexOf("页面加载中，请稍候……</div>") != -1) {
			String TMP = "<iframe onload=\"clearLoading()\" src=\"";
			// 如果页面中有动态加载页，则取之，并加载！
			String _url = contentHtml.substring(contentHtml.indexOf(TMP) + TMP.length());
			_url = _url.substring(0, _url.indexOf("\""));
			contentHtml = SinaWeiboRequest.request(client, _url, getHandler(), FailedNode.PERSON);
		}
		_doc = Jsoup.parse(contentHtml);
	}

	private void parseStatisticInfo() {
		doc_stat = parseToDoc(contentHtml, "pl_profile_photo");
		if (doc_stat == null) {
			doc_stat = parseToDoc(contentHtml, "pl_content_litePersonInfo");
		}
		if (doc_stat == null) {
			doc_stat = _doc;
		}
	}

	private void parseBaseInfo() {
		doc_base = parseToDoc(contentHtml, "pl_profile_hisInfo");
		if (doc_base == null) {
			doc_base = parseToDoc(contentHtml, "pl_content_hisPersonalInfo");
		}
		if (doc_base == null) {
			doc_base = _doc;
		}
	}

	/** 微博主人的ID */
	public String getId() {
		if (!valid) {
			return "";
		}
		if (id == null || "".equals(id)) {
			initIfNeccessory();
			try {
				String userId = contentHtml.substring(contentHtml.indexOf("$CONFIG['oid'] = '"));
				userId = userId.substring("$CONFIG['oid'] = '".length());
				userId = userId.substring(0, userId.indexOf("';"));
				id = userId;
			} catch (Exception e) {
				id = " ";
			}
		}
		return id;
	}

	public String toString() {
		return url;
	}

	public String getName() {
		initIfNeccessory();
		if (!valid) {
			return "";
		}
		if (StringUtils.isEmpty(name)) {
			try {
				name = StringUtils.trimToEmpty(doc_base.getElementsByAttributeValue("class", "pf_name bsp clearfix")
														.select(".name")
														.text());
				if (StringUtils.isBlank(name)) {
					name = StringUtils.trimToEmpty(doc_base.getElementsByAttributeValue("class", "name clearfix")
															.text());
				}
				name = name.endsWith("(设置备注)") ? name.substring(0, name.length() - 6) : name;
				name = StringUtils.trim(name);
			} catch (Exception e) {
				name = " ";
			}
		}
		return name;
	}

	public String getInfo() {
		initIfNeccessory();
		if (!valid) {
			return "";
		}
		if (StringUtils.isEmpty(addr)) {
			try {
				addr = StringUtils.trim(StringUtils.replace(doc_base.getElementsByAttributeValue("class", "pf_tags bsp")
																	.select(".tags")
																	.select("em")
																	.text(),
															"&nbsp;",
															""));
				if (StringUtils.isBlank(addr)) {
					addr = StringUtils.trim(StringUtils.replace(doc_base.getElementsByClass("info").text(),
																"&nbsp;",
																""));
				}
			} catch (Exception e) {
				addr = " ";
			}
		}
		return addr;
	}

	public String getFavorite() {
		initIfNeccessory();
		if (!valid) {
			return "";
		}
		if (StringUtils.isEmpty(favorite)) {
			try {
				Elements eles = _doc.getElementById("plc_profile_header")
									.getElementsByClass("pf_info_right")
									.select("p");
				if (eles == null || eles.isEmpty()) {
					eles = doc_stat.getElementsByClass("W_sina_vip").select("dd");
				}
				for (int i = 0; i < eles.size(); i++) {
					String tmp = StringUtils.trim(StringUtils.replace(eles.get(i).text(), "&nbsp;", ""));
					if (tmp.startsWith("兴趣：")) {
						favorite = tmp.substring(3);
						break;
					}
				}
				if (StringUtils.isEmpty(favorite)) {
					favorite = " ";
				}
			} catch (Exception e) {
				favorite = " ";
			}
		}
		return favorite;
	}

	public String getIntro() {
		initIfNeccessory();
		if (!valid) {
			return "";
		}
		if (StringUtils.isEmpty(intro)) {
			try {
				intro = StringUtils.trim(StringUtils.replace(	doc_base.getElementsByAttributeValue(	"class",
																										"pf_intro bsp")
																		.select(".S_txt2")
																		.text(),
																"&nbsp;",
																""));
				if (StringUtils.isBlank(intro)) {
					intro = StringUtils.trim(StringUtils.replace(	doc_base.getElementsByClass("text").text(),
																	"&nbsp;",
																	""));
				}
				intro = intro.startsWith("简介：") ? intro.substring(3) : intro;
				intro = intro.endsWith("更多资料>>") ? intro.substring(0, intro.length() - 6) : intro;
			} catch (Exception e) {
				intro = " ";
			}
		}
		return intro;
	}

	/**
	 * 关注数
	 * 
	 * @return
	 */
	public String getFollowNum() {
		initIfNeccessory();
		if (!valid) {
			return "0";
		}
		if (StringUtils.isBlank(followNum)) {
			followNum = parseStatisticInfo("关注");
		}
		return followNum;
	}

	/**
	 * 胜于解析统计信息中的关注数、粉丝数、发布的微博数等
	 * 
	 * @param attr
	 * @param value
	 * @return
	 */
	private String parseStatisticInfo(String text) {
		String tmp = "0";
		try {
			Elements eles = doc_stat.getElementsByTag("li");
			// 从后往前找，这样避免转发的微博信息
			for (int i = eles.size() - 1; i >= 0; i--) {
				Element e = eles.get(i);
				Elements es = e.getElementsMatchingOwnText(text);
				if (es.size() > 0) {
					// 如果存在，则取之！
					e = es.last().previousElementSibling();
					if (e != null) {
						tmp = StringUtils.trim(e.text());
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return tmp;
	}

	/**
	 * 粉丝数
	 * 
	 * @return
	 */
	public String getFansNum() {
		initIfNeccessory();
		if (!valid) {
			return "0";
		}
		if (StringUtils.isBlank(fansNum)) {
			fansNum = parseStatisticInfo("粉丝");
		}
		return fansNum;
	}

	/**
	 * 发布的微博数
	 * 
	 * @return
	 */
	public String getPublishNum() {
		initIfNeccessory();
		if (!valid) {
			return "0";
		}
		if (StringUtils.isBlank(publishNum)) {
			publishNum = parseStatisticInfo("微博");
		}
		return publishNum;
	}

	/**
	 * 解析个人的标签信息
	 * 
	 * @return
	 */
	public String getTagInfo() {
		initIfNeccessory();
		if (!valid) {
			return "";
		}
		if (StringUtils.isEmpty(tagInfo)) {
			Elements eles = doc_base.getElementsByAttributeValue("class", "pf_tags bsp")
									.first()
									.getElementsByAttributeValue("class", "layer_menulist_tags S_line3 S_bg5")
									.select("li")
									.select(".S_func1")
									.select("span");
			if (eles == null || eles.isEmpty()) {
				Document doc = parseToDoc(contentHtml, "pl_content_hisTags");
				eles = doc.getElementsByTag("a");
			}
			Iterator<Element> es = eles.iterator();
			StringBuffer sb = new StringBuffer();
			while (es.hasNext()) {
				Element e = es.next();
				sb.append(",");
				sb.append(StringUtils.trimToEmpty(e.text()));
			}
			tagInfo = sb.length() > 0 ? sb.substring(1) : "";
		}
		return tagInfo;
	}

	public List<WeiboPersonInfo> getFans() {
		initIfNeccessory();
		if (!valid) {
			return new ArrayList<WeiboPersonInfo>();
		}
		if (fans == null) {
			fans = new ArrayList<WeiboPersonInfo>();
			String url = "http://weibo.com/" + getId() + "/fans";
			parseRelation(url, FailedNode.FANS, fans);
		}
		return fans;
	}

	public List<WeiboPersonInfo> getFollows() {
		initIfNeccessory();
		if (!valid) {
			return new ArrayList<WeiboPersonInfo>();
		}
		if (follows == null) {
			follows = new ArrayList<WeiboPersonInfo>();
			String url = "http://weibo.com/" + getId() + "/follow";
			parseRelation(url, FailedNode.FOLLOWS, follows);
		}
		return follows;
	}

	/**
	 * 解析用户的关系
	 * 
	 * @param url
	 * @param list
	 */
	private void parseRelation(String url, FailedNode node, List<WeiboPersonInfo> list) {
		try {
			String text = SinaWeiboRequest.request(client, url, getHandler(), node);
			Document doc = null;
			if (FailedNode.FANS.compareTo(node) == 0) {
				doc = parseToDoc(text, "pl_relation_hisFans");
			} else {
				doc = parseToDoc(text, "pl_relation_hisFollow");
			}
			Elements eles = doc.getElementsByAttributeValue("node-type", "userListBox").first().getElementsByTag("li");
			Iterator<Element> es = eles.iterator();
			while (es.hasNext()) {
				String userId = es.next().attr("action-data");
				userId = userId.substring("uid=".length(), userId.indexOf("&"));
				WeiboPersonInfo person = new WeiboPersonInfo("http://weibo.com/u/" + userId, client);
				person.setId(userId);
				person.setHandler(getHandler());
				list.add(person);
			}
			eles = doc.getElementsByAttributeValue("class", "W_pages W_pages_comment").select(".W_btn_a");
			if (eles.size() > 0) {
				// 如果有下一页，则读取下一页的内容
				Element e = eles.last();
				if ("下一页".equals(e.text())) {
					try {
						// 每页之间停顿1秒，为了不被新浪屏蔽。
						Thread.sleep(1000);
					} catch (Exception e1) {
					}
					String _url = e.attr("href");
					_url = _url.startsWith("/") ? "http://weibo.com" + _url
							: url.substring(0, url.lastIndexOf("/") + 1) + _url;
					parseRelation(_url, node, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document parseToDoc(String html, String contentPart) {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + contentPart + "\",";
		String tmp = cut(html, detailStart);
		return StringUtils.isBlank(tmp) ? null : Jsoup.parse(tmp);
	}

	public void setId(String id) {
		this.id = id;
	}
}
