package com.scoop.crawler.weibo.fetch;

import com.scoop.crawler.weibo.parser.TempUrl;

public abstract class FetchSina {
	/** 选取最大的页数！-1表示不限制 */
	protected static int maxLimit = -1;
	private static ThreadLocal<TempUrl> baseUrl = new InheritableThreadLocal<TempUrl>();

	/**
	 * 保存用户给定的当前URL。
	 * 
	 * @param url
	 */
	public static void saveBaseUrl(TempUrl url) {
		baseUrl.set(url);
	}

	/**
	 * 获取用户给定的当前URL。
	 * 
	 * @return
	 */
	public static TempUrl getBaseUrl() {
		return baseUrl.get();
	}
}
