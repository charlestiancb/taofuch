package com.scoop.crawler.weibo.util;

import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import com.scoop.crawler.weibo.parser.TempUrl;

public class RegUtils {
	/**
	 * 根据指定的带有变量的url解析出当前查询的url的内容！
	 * 
	 * @param origUrl
	 * @param queryUrl
	 * @return
	 */
	public static String parseToQuery(String origUrl, String queryUrl) {
		if (StringUtils.isBlank(origUrl) || StringUtils.isBlank(queryUrl)) {
			return origUrl;
		}
		origUrl = StringUtils.trim(origUrl);
		queryUrl = StringUtils.trim(queryUrl);
		String[] extra = origUrl.split("\\{\\d+\\}");
		for (String s : extra) {
			queryUrl = StringUtils.replaceOnce(queryUrl, s, ",");
		}
		while (queryUrl.indexOf(",,") != -1) {
			queryUrl = queryUrl.replaceAll(",,", ",");
		}
		queryUrl = queryUrl.startsWith(",") ? queryUrl.substring(1) : queryUrl;
		queryUrl = queryUrl.endsWith(",") ? queryUrl.substring(0, queryUrl.length() - 1) : queryUrl;
		try {
			queryUrl = URLDecoder.decode(queryUrl, "UTF-8");
		} catch (Exception e) {
		}
		return StringUtils.isBlank(queryUrl) ? origUrl : queryUrl;
	}

	public static String parseToQuery(TempUrl baseUrl) {
		if (baseUrl == null) {
			return "【没有内容】";
		}
		return parseToQuery(baseUrl.getOrigUrl(), baseUrl.getReqUrl());
	}
}
