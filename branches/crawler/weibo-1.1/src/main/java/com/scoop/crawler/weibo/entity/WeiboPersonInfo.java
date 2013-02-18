package com.scoop.crawler.weibo.entity;

import java.util.Iterator;

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
			parseBaseInfo();
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
			String userUrlPreffix = "http://weibo.com/u/";
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
			return "";
		}
		if (StringUtils.isEmpty(name)) {
			try {
				try {
					name = StringUtils.trimToEmpty(doc_base
							.getElementsByAttributeValue("class", "pf_name bsp clearfix").select(".name").text());
				} catch (Exception e1) {
				}
				if (StringUtils.isBlank(name)) {
					try {
						name = StringUtils.trimToEmpty(doc_base
								.getElementsByAttributeValue("class", "tit_prf clearFix").select(".lf").text());
					} catch (Exception e) {
					}
				}
				if (StringUtils.isBlank(name)) {
					try {
						name = _doc.getElementsByClass("title_big").get(0).text().trim();
					} catch (Exception e) {
					}
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
				try {
					addr = StringUtils.trim(StringUtils.replace(
							doc_base.getElementsByAttributeValue("class", "pf_tags bsp").select(".tags").text(),
							"&nbsp;", ""));
				} catch (Exception e) {
				}
				if (StringUtils.isBlank(addr)) {
					try {
						addr = StringUtils.trim(StringUtils.replace(doc_base.getElementsByClass("info").text(),
								"&nbsp;", ""));
					} catch (Exception e) {
					}
				}
				addr = addr.endsWith("标签") ? addr.substring(0, addr.lastIndexOf("标签")) : addr;
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
				Elements eles = null;
				try {
					eles = _doc.getElementById("pl_profile_extraInfo")
							.getElementsByAttributeValue("class", "pf_star_info bsp S_txt2").select("p");
				} catch (Exception e) {
				}
				if (eles == null || eles.isEmpty()) {
					try {
						eles = doc_stat.getElementsByAttributeValue("class", "pf_verified_info bsp S_txt2")
								.select("dd");
					} catch (Exception e) {
					}
				}
				if (eles == null || eles.isEmpty()) {
					favorite = " ";
					return favorite;
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
				try {
					intro = StringUtils.trim(StringUtils.replace(
							doc_base.getElementsByAttributeValue("class", "pf_intro bsp").select(".S_txt2").text(),
							"&nbsp;", ""));
				} catch (Exception e) {
				}
				if (StringUtils.isBlank(intro)) {
					try {
						intro = StringUtils.trim(StringUtils.replace(
								doc_base.getElementsByAttributeValue("class", "tCon MIB_txtb MIB_linkb")
										.select("#epintro").text(), "&nbsp;", ""));
					} catch (Exception e) {
					}
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
			Elements eles = null;
			try {
				eles = doc_base.getElementsByAttributeValue("class", "pf_tags bsp").first()
						.getElementsByAttributeValue("class", "layer_menulist_tags S_line3 S_bg5").select("li")
						.select(".S_func1").select("span");
			} catch (Exception e) {
			}
			try {
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
			} catch (Exception e) {
				tagInfo = " ";
			}
		}
		return tagInfo;
	}

	private Document parseToDoc(String html, String contentPart) {
		String detailStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"" + contentPart + "\",";
		String tmp = cut(html, detailStart);
		return StringUtils.isBlank(tmp) ? null : Jsoup.parse(tmp);
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
