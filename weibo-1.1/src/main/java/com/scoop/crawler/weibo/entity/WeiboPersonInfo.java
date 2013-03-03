package com.scoop.crawler.weibo.entity;

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
	private Document doc_stat;
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
			parseStatisticInfo();
		} catch (Exception e) {
			throw new IllegalStateException(e);
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
		Document docInfo = parseToDoc(contentHtml, "plc_main");
		if (docInfo != null) {
			personInfo = docInfo.getElementsByAttributeValue("class", "profile_pinfo");
			if (personInfo != null && personInfo.size() > 0) {
				personInfo = personInfo.first().getElementsByAttributeValue("class", "infoblock");
				if (personInfo != null && personInfo.size() > 0) {
					personInfo = personInfo.select(".pf_item");// 每一项信息
				}
			}
		}
	}

	private void parseStatisticInfo() {
		doc_stat = parseToDoc(contentHtml, "pl_profile_photo");
		if (doc_stat == null) {
			doc_stat = parseToDoc(contentHtml, "pl_content_litePersonInfo");
		}
	}

	/** 微博主人的ID */
	public String getId() {
		if (!valid) {
			return "";
		}
		if (id == null || "".equals(id)) {
			String userUrlPreffix = "http://weibo.com/";
			if (StringUtils.isNotBlank(getUrl()) && getUrl().trim().toLowerCase().startsWith(userUrlPreffix)) {
				// 如果是以带有id的链接，则直接解析。
				id = getUrl().trim().toLowerCase().substring(userUrlPreffix.length());
				int idx = id.indexOf("/");
				if (idx != -1) {
					id = id.substring(0, idx);
				} else {
					idx = id.indexOf("?");
					if (idx != -1) {
						id = id.substring(0, idx);
					}
				}
			} else {
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
				int idx = contentHtml.indexOf("$CONFIG['onick'] = '");
				if (idx > -1) {
					name = contentHtml.substring(idx);
					name = name.substring("$CONFIG['onick'] = '".length());
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
				Elements _tmpLabel = e.getElementsByAttributeValue("class", "label S_txt2");
				if (_tmpLabel != null && _tmpLabel.size() > 0
						&& StringUtils.trimToEmpty(_tmpLabel.text()).equals(label)) {
					// 如果有值，则表示找到对应的信息。返回该值！
					return StringUtils.trim(e.getElementsByAttributeValue("class", "con").text());
				}
			}
		}
		return " ";
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
		return introduce = setFieldValue(introduce, "简介");
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
			Elements eles = doc_stat.getElementsByClass("user_atten").select("li");
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

	private Document parseToDoc(String html, String contentPart) {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + contentPart + "\",";
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

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNeedFetchRelation() {
		return needFetchRelation;
	}

	public void setNeedFetchRelation(boolean needFetchRelation) {
		this.needFetchRelation = needFetchRelation;
	}
}
