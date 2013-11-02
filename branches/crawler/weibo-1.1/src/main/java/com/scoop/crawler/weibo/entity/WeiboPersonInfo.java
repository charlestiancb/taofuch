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
import com.scoop.crawler.weibo.util.JSONUtils;
import com.scoop.crawler.weibo.util.Logger;
import com.scoop.crawler.weibo.util.ThreadUtils;

/**
 * 抓取每个微博个人首页的详细信息，如姓名、所在地等。。
 * 
 * @author taofucheng
 * 
 */
public class WeiboPersonInfo extends Info {
	private Document doc_stat;// 统计类的信息，如微博数、关注数等。
	private Document docHeader;// 头部区域
	/** 用户的每个字段的内容 */
	private Elements personInfo;
	// ========下面是微博中解析出来的各项内容！===============//
	/** 微博主人的ID */
	private String id;
	/** 微博主人的姓名 */
	private String name;
	/** 性别 */
	private String gender;
	/** 博客 */
	private String blog;
	/** 邮箱 */
	private String email;
	/** 公司 */
	private String company;
	/** 大学与专业 */
	private String university;
	/** 生日 */
	private String birthday;
	/** 所在地 */
	private String addr;
	/** 简介 */
	private String introduce;
	/** 关注数 */
	private String followNum;
	/** 粉丝数 */
	private String fansNum;
	/** 发布微博数 */
	private String publishNum;
	/** 个人的标签信息 */
	private String tagInfo;
	/** 是否需要抓取其粉丝与关注信息 */
	private boolean needFetchRelation = true;

	//
	/** 粉丝的编号 */
	private List<WeiboPersonInfo> fans;
	/** 被关注者编号 */
	private List<WeiboPersonInfo> follows;

	/**
	 * 指定具体的微博信息，然后会判断内容是否存在，并将存在的微博内容请求出来备用！
	 * 
	 * @param url
	 *            结果类似：http://weibo.com/123454/info
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
			parseHeaderInfo();
			parseStatisticInfo();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void parseHeaderInfo() {
		docHeader = parseToDoc(contentHtml, "pl.header.head.index",
				"Pl_Core_Header__1");
	}

	private void getContentHtml() {
		contentHtml = SinaWeiboRequest.request(client, url, getHandler(),
				FailedNode.PERSON);
		if (contentHtml.indexOf("页面加载中，请稍候……</div>") != -1) {
			String TMP = "<iframe onload=\"clearLoading()\" src=\"";
			// 如果页面中有动态加载页，则取之，并加载！
			String _url = contentHtml.substring(contentHtml.indexOf(TMP)
					+ TMP.length());
			_url = _url.substring(0, _url.indexOf("\""));
			contentHtml = SinaWeiboRequest.request(client, _url, getHandler(),
					FailedNode.PERSON);
		}
		collectInfos();
	}

	private void collectInfos() {
		Document docInfo = parseToDoc(contentHtml, "",
				"Pl_Official_LeftInfo__14");
		if (docInfo == null) {
			personInfo = Jsoup.parse("").select("body");
		} else {
			personInfo = docInfo.getElementsByClass("pf_item");
		}
	}

	private void parseStatisticInfo() {
		doc_stat = parseToDoc(contentHtml, "pl.header.head.index",
				"pl_profile_photo");
		if (doc_stat == null) {
			doc_stat = parseToDoc(contentHtml, "pl.header.head.index",
					"pl_content_litePersonInfo");
		}
		if (doc_stat == null) {
			doc_stat = parseToDoc(contentHtml, "pl.header.head.index",
					"Pl_Official_Header__1");
		}
	}

	/** 微博主人的ID */
	public String getId() {
		if (!valid) {
			return "";
		}
		if (id == null || "".equals(id)) {
			String userUrlPreffix = "http://weibo.com/";
			if (StringUtils.isNotBlank(getUrl())
					&& getUrl().trim().toLowerCase().startsWith(userUrlPreffix)) {
				// 如果是以带有id的链接，则直接解析。
				id = getUrl().trim().toLowerCase()
						.substring(userUrlPreffix.length());
				int idx = id.indexOf("/");
				if (idx != -1) {
					id = id.substring(0, idx);
				} else {
					idx = id.indexOf("?");
					if (idx != -1) {
						id = id.substring(0, idx);
					}
				}
				id = StringUtils.trim(id);
			}
			if (StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
				initIfNeccessory();
				try {
					String tmp = contentHtml.replaceAll(" ", "");
					String userId = tmp.substring(tmp
							.indexOf("$CONFIG['oid']='"));
					userId = userId.substring("$CONFIG['oid']='".length());
					userId = userId.substring(0, userId.indexOf("';"));
					id = userId;
				} catch (Exception e) {
					id = " ";
				}
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
			return " ";
		}
		if (StringUtils.isEmpty(name)) {
			try {
				String tmp = contentHtml.replaceAll(" ", "");
				int idx = tmp.indexOf("$CONFIG['onick']='");
				if (idx > -1) {
					name = tmp.substring(idx);
					name = name.substring("$CONFIG['onick']='".length());
					name = name.substring(0, name.indexOf("';"));
					return name;
				}
				name = parseFieldValue("昵称");
			} catch (Exception e) {
				name = " ";
			}
		}
		return name;
	}

	public String getBlog() {
		return blog = setFieldValue(blog, "博客");
	}

	public String getGender() {
		return gender = setFieldValue(gender, "性别");
	}

	public String getEmail() {
		return email = setFieldValue(email, "邮箱");
	}

	public String getCompany() {
		return company = setFieldValue(company, "公司");
	}

	public String getUniversity() {
		return university = setFieldValue(university, "大学");
	}

	public String getBirthday() {
		return birthday = setFieldValue(birthday, "生日");
	}

	public String getAddr() {
		return addr = setFieldValue(addr, "所在地");
	}

	public String getIntroduce() {
		introduce = setFieldValue(introduce, "简介");
		if (StringUtils.isBlank(introduce)) {
			try {
				Elements es = docHeader.getElementsByAttributeValue("class",
						"username");
				introduce = es.get(0).nextElementSibling().text();
				introduce = introduce.isEmpty() ? " " : introduce;
			} catch (Exception e) {
				introduce = " ";
			}
		}
		return introduce;
	}

	public String getTagInfo() {
		return tagInfo = setFieldValue(tagInfo, "标签");
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
	 * 用于解析统计信息中的关注数、粉丝数、发布的微博数等
	 * 
	 * @param text
	 * @return
	 */
	private String parseStatisticInfo(String text) {
		String tmp = "0";
		try {
			Elements eles = doc_stat.getElementsByClass("user_atten").select(
					"li");
			// 从后往前找，这样避免转发的微博信息
			for (int i = eles.size() - 1; i >= 0; i--) {
				Element e = eles.get(i);
				Elements es = e.getElementsMatchingOwnText("^" + text + "$");
				if (es.size() > 0) {
					// 如果存在，则取之！
					e = es.last().previousElementSibling();
					if (e != null) {
						tmp = StringUtils.trim(e.text());
						if (StringUtils.isBlank(tmp)) {
							tmp = "0";
							continue;
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			tmp = "0";
		}
		return tmp;
	}

	private Document parseToDoc(String html, String ns, String domid) {
		String detailStart = "<script>FM.view({\"ns\":\"" + ns
				+ "\",\"domid\":\"" + domid + "\",";
		String tmp = cut(html, detailStart);
		return StringUtils.isBlank(tmp) ? null : Jsoup.parse(tmp);
	}

	/**
	 * 给指定的字段解析出指定的值，并存储
	 * 
	 * @param fieldValue
	 * @param fieldLabel
	 * @return
	 */
	private String setFieldValue(String fieldValue, String fieldLabel) {
		initIfNeccessory();
		if (!valid) {
			return " ";
		}
		if (StringUtils.isEmpty(fieldValue)) {
			fieldValue = parseFieldValue(fieldLabel);
		}
		return fieldValue;
	}

	/**
	 * 解析指定的字段的信息。如果没有返回" "
	 * 
	 * @param label
	 * @return
	 */
	private String parseFieldValue(String label) {
		if (personInfo != null && personInfo.size() > 0) {
			for (int i = 0; i < personInfo.size(); i++) {
				Element e = personInfo.get(i);
				Elements _tmpLabel = e.getElementsByAttributeValue("class",
						"label S_txt2");
				if (_tmpLabel != null
						&& _tmpLabel.size() > 0
						&& StringUtils.trimToEmpty(_tmpLabel.text()).equals(
								label)) {
					// 如果有值，则表示找到对应的信息。返回该值！
					return StringUtils.trim(e.getElementsByAttributeValue(
							"class", "con").text());
				}
			}
		}
		return " ";
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNeedFetchRelation() {
		return needFetchRelation;
	}

	public void setNeedFetchRelation(boolean needFetchRelation) {
		this.needFetchRelation = needFetchRelation;
	}

	public List<WeiboPersonInfo> getFans() {
		if (fans == null) {
			fans = new ArrayList<WeiboPersonInfo>();
			String url = "http://weibo.com/" + getId() + "/fans";
			if (StringUtils.isNotBlank(getId())) {
				parseRelation(url, FailedNode.FANS, fans);
			}
		}
		return fans;
	}

	public List<WeiboPersonInfo> getFollows() {
		if (follows == null) {
			follows = new ArrayList<WeiboPersonInfo>();
			String url = "http://weibo.com/" + getId() + "/follow";
			if (StringUtils.isNotBlank(getId())) {
				parseRelation(url, FailedNode.FOLLOWS, follows);
			}
		}
		return follows;
	}

	/**
	 * 解析用户的关系
	 * 
	 * @param url
	 * @param list
	 */
	private void parseRelation(String url, FailedNode node,
			List<WeiboPersonInfo> list) {
		try {
			String text = SinaWeiboRequest.request(client, url, getHandler(),
					node);
			String detailStart = "<script>FM.view({\"ns\":\"pl.content.followTab.index\",\"domid\":\"Pl_Official_LeftHisRelation__";
			String content = "";
			int idx = text.indexOf(detailStart);
			if (idx > -1) {
				content = text.substring(idx + detailStart.length());
				idx = content.indexOf(detailEnd);
				content = content.substring(0, idx);
				content = StringUtils.isBlank(content) ? null : content
						.substring(content.indexOf("\",") + 2);
				content = "{" + content;
				content = JSONUtils.getSinaHtml(content);
			}
			Document doc = content == null ? null : Jsoup.parse(content);
			if (doc == null) {
				System.err.println("没有发现用户的" + node + "信息！");
				return;
			}
			Elements eles = doc.getElementsByAttributeValue("node-type",
					"userListBox");
			if (eles.isEmpty()) {
				return;
			}
			eles = eles.first().getElementsByTag("li");
			if (eles.isEmpty()) {
				return;
			}
			Iterator<Element> es = eles.iterator();
			while (es.hasNext()) {
				String userId = es.next().attr("action-data");
				userId = userId.substring("uid=".length(), userId.indexOf("&"));
				WeiboPersonInfo person = new WeiboPersonInfo(
						"http://weibo.com/" + userId + "/info", client);
				person.setId(userId);
				person.setHandler(getHandler());
				list.add(person);
			}
			eles = doc.getElementsByAttributeValue("node-type", "pageList")
					.select("a");
			if (eles.size() > 0) {
				// 如果有下一页，则读取下一页的内容
				Element e = eles.last();
				if ("下一页".equals(e.text())) {
					try {
						// 每页之间停顿1秒，为了不被新浪屏蔽。
						Thread.sleep(ThreadUtils.nextSleepInterval());
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
			Logger.log("解析用户关系信息时失败：" + e);
		}
	}
}
