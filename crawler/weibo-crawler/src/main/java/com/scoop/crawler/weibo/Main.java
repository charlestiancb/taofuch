package com.scoop.crawler.weibo;

import java.io.UnsupportedEncodingException;

import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;

/**
 * 微博抓取的入口程序
 * 
 * @author taofucheng
 * 
 */
public class Main {
	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// 常见的个人微博页面
		// String url = "http://weibo.com/yaoyaoxiaojing";//
		// 加V的个人微博官网形式
		// String url="http://gov.weibo.com/profile.php?uid=sciencenet&ref=";
		// 微博搜索页面，以下示例中{0}就是words中每一行的内容
		String url = "http://s.weibo.com/weibo/{0}&Refer=STopic_box";
		// 微博搜索用户
		// url =
		// "http://s.weibo.com/user/%25E7%25A4%25BE%25E4%25BC%259A%25E8%25AE%25A1%25E7%25AE%2597";
		// 抓取指定页面的微博
		FetchSinaWeibo.fetch("hellohank1@gmail.com", "hellohank", url, "F:/tmp/words.txt");
	}
}
