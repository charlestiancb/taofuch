package com.scoop.crawler.weibo.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {
	private static final String[] special = new String[] { "&amp;", "&lt;", "&gt;", "&quot;", "&nbsp;" };
	private static final String[] plain = new String[] { "&", "<", ">", "\"", " " };

	@SuppressWarnings({ "rawtypes" })
	public static String getSinaHtml(String jsonContent) {
		if (StringUtils.isBlank(jsonContent)) {
			return null;
		}
		try {
			HashMap map = JSON.parseObject(jsonContent, HashMap.class);
			Object result = map.get("html");
			if (result == null) {
				result = map.get("data");
				if (result != null) {
					if (result instanceof JSONObject) {
						result = ((JSONObject) result).getString("html");
					}
				}
			}
			return (String) result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String unEscapeHtml(String html) {
		if (StringUtils.isBlank(html)) {
			return html;
		}
		for (int i = 0; i < special.length; i++) {
			html = html.replaceAll(special[i], plain[i]);
		}
		return html;
	}
}
