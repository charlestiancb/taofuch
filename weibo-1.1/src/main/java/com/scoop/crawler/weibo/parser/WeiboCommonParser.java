package com.scoop.crawler.weibo.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.request.SinaWeiboRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.JSONUtils;

/**
 * 普通方式的微博，如：http://weibo.com/u/2675686781，这种页面的微博内容是以JSON格式组织的。
 * 
 * @author taofucheng
 * 
 */
public class WeiboCommonParser extends JsonStyleParser {
	private String curUrl;

	public String getCurUrl() {
		return curUrl;
	}

	public void setCurUrl(String curUrl) {
		this.curUrl = curUrl;
	}

	private String contentStart = "<script>STK && STK.pageletM && STK.pageletM.view({\"pid\":\"pl_content_hisFeed\",";

	/**
	 * 普通方式的JSON微博，如：
	 * 
	 * @param client
	 * @param csvWriter
	 * @param csvFile
	 */
	public WeiboCommonParser(DefaultHttpClient client, DataSource dataSource) {
		super(client, dataSource);
	}

	@Override
	public boolean isBelong(String html) {
		return html.indexOf(contentStart) > -1;
	}

	public void parse(String html) throws IOException {
		int idx = html.indexOf(contentStart);
		if (idx == -1) {
			return;
		}
		System.out.println("解析用户主页的微博");
		String weiboList = html.substring(idx + contentStart.length());
		weiboList = weiboList.substring(0, weiboList.indexOf(contentEnd));
		weiboList = "{" + weiboList;// 补齐为JSON格式
		// 解析出用户编号！
		String uid = "$CONFIG['oid'] = '";
		uid = html.substring(html.indexOf(uid)) + uid.length();
		uid = uid.substring(0, uid.indexOf("';"));
		uid = uid.substring(uid.lastIndexOf("'") + 1);
		// 将map中的html内容拿出来！
		weiboList = JSONUtils.getSinaHtml(weiboList);
		Document doc = Jsoup.parse(weiboList);
		Elements eles = doc.getElementsByAttributeValue("action-type", "feed_list_item");
		if (eles != null && eles.size() > 0) {
			List<String> ids = new ArrayList<String>();
			// 如果这样的格式存在，则说明是那种HTML格式的
			for (int i = 0; i < eles.size(); i++) {
				System.out.println("解析用户主页上的每一条微博");
				String tmp = parseId(eles.get(i));
				if (StringUtils.isNotBlank(tmp)) {
					ids.add(tmp);
				}
				// 一条条的微博进行处理，解析每条微博的信息
				parseWeibo(	StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
							StringUtils.trim(parseMsgPublishTime(eles.get(i))),
							client,
							dataSource);
			}
			// 加载下一屏的内容，并进行处理
			eles = loadNextPage(uid, ids);
		}
	}

	/**
	 * 解析给定的元素中的mid属性值
	 * 
	 * @param e
	 * @return
	 */
	private String parseId(Element e) {
		try {
			return e.attr("mid");
		} catch (Exception e1) {
			return null;
		}
	}

	/**
	 * 加载下一个ajax片段内容。
	 * 
	 * @param html
	 *            通过这个获取uid，
	 * @param ids
	 * @return
	 */
	private Elements loadNextPage(String uid, List<String> ids) {
		try {
			// ajax加载的URL：http://weibo.com/aj/mblog/mbloglist?count=15&page=1&pre_page=1&max_id=3502936598455264&end_id=3503329034093613&uid=2185608961
			String url = "http://weibo.com/aj/mblog/mbloglist?count=15";
			String maxId = "";
			String endId = "";
			int page = Integer.parseInt(StringUtils.trim(curUrl.substring(curUrl.lastIndexOf("page=")
					+ "page=".length())));
			int prePage = page == 1 ? 1 : page - 1;
			//
			Collections.sort(ids);
			maxId = ids.get(0);
			endId = ids.get(ids.size() - 1);
			//
			url = url + "&page=" + page + "&pre_page=" + prePage + "&max_id=" + maxId + "&end_id=" + endId + "&uid="
					+ uid;
			String json = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.USER_WEIBO);
			String _html = (String) JSON.parseObject(json, HashMap.class).get("data");
			return Jsoup.parse(_html).getElementsByAttributeValue("action-type", "feed_list_item");
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 用于对指定URL的重试！
	 * 
	 * @param client
	 * @param url
	 * @param handler
	 * @param node
	 */
	public void reTry(DefaultHttpClient client, String url, FailedHandler handler, FailedNode node) {
		int idx = url.lastIndexOf("&uid=");
		if (idx == -1) {
			return;
		}
		String uid = StringUtils.trimToEmpty(url.substring(idx + "&uid=".length()));
		String json = SinaWeiboRequest.request(client, url, getHandler(), FailedNode.USER_WEIBO);
		String _html = (String) JSON.parseObject(json, HashMap.class).get("data");
		Elements eles = Jsoup.parse(_html).getElementsByAttributeValue("action-type", "feed_list_item");
		while (eles != null && eles.size() > 0) {
			List<String> ids = new ArrayList<String>();
			// 如果这样的格式存在，则说明是那种HTML格式的
			for (int i = 0; i < eles.size(); i++) {
				System.out.println("解析用户主页上的每一条微博");
				String tmp = parseId(eles.get(i));
				if (StringUtils.isNotBlank(tmp)) {
					ids.add(tmp);
				}
				// 一条条的微博进行处理，解析每条微博的信息
				try {
					parseWeibo(	StringUtils.trim(parseMsgUrlFromJSONStyle(eles.get(i))),
								StringUtils.trim(parseMsgPublishTime(eles.get(i))),
								client,
								dataSource);
				} catch (IOException e) {
				}
			}
			// 加载下一屏的内容，并进行处理
			eles = loadNextPage(uid, ids);
		}
	}
}
