package com.scoop.crawler.weibo.request;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.scoop.crawler.weibo.entity.LogonInfo;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.request.failed.FailedHandler;
import com.scoop.crawler.weibo.request.failed.FailedNode;
import com.scoop.crawler.weibo.util.Logger;
import com.scoop.crawler.weibo.util.ThreadUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
public class SinaWeiboRequest {
	private static ThreadLocal<String> validCode = new ThreadLocal<String>();
	/** 是否已经判断过代理情况！ */
	private static boolean hasProcProxy = false;
	private static final String INPUT_CODE_TIP = "%CE%AA%C1%CB%C4%FA%B5%C4%D5%CA%BA%C5%B0%B2%C8%AB%A3%AC%C7%EB%CA%E4%C8%EB%D1%E9%D6%A4%C2%EB";
	private static final String CODE_ERR_TIP = "%CA%E4%C8%EB%B5%C4%D1%E9%D6%A4%C2%EB%B2%BB%D5%FD%C8%B7";
	private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
	/** 等待的时间长度 */
	private static long waitInterval = 20 * 60 * 1000;// 20分钟
	private static int timeout = 30 * 1000;// 30s

	/***
	 * 用户名转码
	 * 
	 * @param account
	 *            用户名
	 * @return 转码后的用户名
	 */
	private static String encodeAccount(String account) {
		try {
			return Base64.encode(URLEncoder.encode(account, "UTF-8").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return account;
		}
	}

	/***
	 * 随机码生成
	 * 
	 * @param len
	 *            生成位数
	 * @return 随机码
	 */
	private static String makeNonce(int len) {
		String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str = "";
		for (int i = 0; i < len; i++) {
			str += x.charAt((int) (Math.ceil(Math.random() * 1000000) % x.length()));
		}
		return str;
	}

	/***
	 * 获取ServerTime
	 * 
	 * @return 系统时间/1000
	 */
	private static String getServerTime() {

		long servertime = new Date().getTime() / 1000;

		return String.valueOf(servertime);

	}

	/**
	 * 根据用户名与密码，获取带有登录Cookie的请求。
	 * 
	 * @param username
	 *            微博用户名
	 * @param password
	 *            微博密码
	 * @return
	 * @throws Exception
	 */
	public static DefaultHttpClient getHttpClient(String username, String password) {
		try {
			LogonInfo.store(username, password);
			// httpClient实例
			DefaultHttpClient client = newProxy();

			// 新浪微博登录js地址
			HttpPost post = new HttpPost("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.5)");

			// 获得ServerTime
			String data = getServerTime();

			// 获得6位随机码
			String nonce = makeNonce(6);

			// 新浪微博登录参数设定
			List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
			nvps.add(new BasicNameValuePair("entry", "weibo"));
			nvps.add(new BasicNameValuePair("gateway", "1"));
			nvps.add(new BasicNameValuePair("from", ""));
			nvps.add(new BasicNameValuePair("savestate", "7"));
			nvps.add(new BasicNameValuePair("useticket", "1"));
			nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
			nvps.add(new BasicNameValuePair("vsnf", "1"));
			nvps.add(new BasicNameValuePair("vsnval", ""));
			nvps.add(new BasicNameValuePair("su", encodeAccount(username)));
			nvps.add(new BasicNameValuePair("service", "miniblog"));
			nvps.add(new BasicNameValuePair("servertime", data));
			nvps.add(new BasicNameValuePair("nonce", nonce));
			nvps.add(new BasicNameValuePair("pwencode", "rsa2"));// 原值：wsse
			nvps.add(new BasicNameValuePair("rsakv", "1330428213"));
			nvps.add(new BasicNameValuePair("prelt", "65"));
			nvps.add(new BasicNameValuePair("sp", new SinaSSOEncoder().encode(password, data, nonce)));
			nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
			nvps.add(new BasicNameValuePair("returntype", "META"));
			nvps.add(new BasicNameValuePair("url",
											"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
			HttpEntity entity = new UrlEncodedFormEntity(nvps, "UTF-8");
			post.setEntity(entity);
			// 获取跳转登录
			HttpResponse res = client.execute(post);
			// 获取真实跳转登录地址（Sample程序，所以采用硬编码方式）
			String tmp = EntityUtils.toString(res.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(res.getEntity());
			// doCrossLogin(client, tmp);
			int fromIndex = tmp.indexOf("http://weibo.com/ajaxlogin.php?");
			if (tmp.indexOf("retcode=0") == -1) {
				fromIndex = tmp.indexOf("&reason=");
				tmp = tmp.substring(fromIndex);
				tmp = tmp.substring(tmp.indexOf("=") + 1);
				tmp = tmp.substring(0, tmp.indexOf("&#39;\"/>"));
				String _tmp = URLDecoder.decode(tmp, "GBK");
				Logger.log("后台代码登录失败，网站提示信息：" + _tmp + "。 \n使用浏览器进行登录尝试！");
				if (INPUT_CODE_TIP.equals(tmp) || CODE_ERR_TIP.equals(tmp)) {
					validCode.set("输入验证码");
					// return getHttpClient(username, password);
				}
				// 使用网页方式登录
				setCookie(client);
				String userId = "2210871673";
				res = client.execute(new HttpGet("http://weibo.com/u/" + userId + ""));
				String html = EntityUtils.toString(res.getEntity(), "UTF-8");
				EntityUtils.consumeQuietly(res.getEntity());
				if (html.replaceAll(" ", "").indexOf("$CONFIG['oid']='" + userId + "';") == -1) {
					// 登录失败，退出操作！
					Logger.log("浏览器方式登录也失败，只能停止工作了~");
					System.exit(0);
				}
			} else {
				String url = tmp.substring(fromIndex, tmp.indexOf("retcode=0", fromIndex) + 9);
				// 登录新浪微博
				HttpGet loginMethod = new HttpGet(url);
				res = client.execute(loginMethod);
				Logger.log("登录结果：" + EntityUtils.toString(res.getEntity(), "UTF-8"));
				EntityUtils.consumeQuietly(res.getEntity());
			}
			// 将客户端放到池中，方便调用
			ThreadUtils.setClient(client);
			Logger.log("程序登录成功！开始工作！");
			return client;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static void setCookie(DefaultHttpClient client) {
		WebDriver driver = null;
		try {
			driver = ExploreRequest.getDriver(null);
			setCookieToClient(client, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * 将driver中的cookie设置给client类。
	 * 
	 * @param client
	 * @param driver
	 */
	public static void setCookieToClient(DefaultHttpClient client, WebDriver driver) {
		if (driver == null || client == null) {
			return;
		}
		Set<org.openqa.selenium.Cookie> cs = driver.manage().getCookies();
		CookieStore cookieStore = client.getCookieStore();
		for (org.openqa.selenium.Cookie c : cs) {
			BasicClientCookie2 cookie = new BasicClientCookie2(c.getName(), c.getValue());
			cookie.setDomain(c.getDomain());
			cookie.setExpiryDate(c.getExpiry());
			cookie.setPath(c.getPath());
			cookieStore.addCookie(cookie);
		}
		client.setCookieStore(cookieStore);
	}

	/**
	 * 请求URL，并返回请求的页面信息。
	 * 
	 * @param client
	 *            请求
	 * @param url
	 *            目标URL
	 * @param handler
	 *            如果请求不成功时的处理方式！
	 * @return
	 */
	public static String request(DefaultHttpClient client, String url, FailedHandler handler, FailedNode node) {
		try {
			// 设置需要登录才能访问的微博地址（由于httpClient已经拥有登录后的cookie，所以此处可以直接访问）
			HttpGet weiboMethod = new HttpGet(url);
			HttpParams ps = weiboMethod.getParams();
			HttpConnectionParams.setConnectionTimeout(ps, timeout);
			HttpConnectionParams.setSoTimeout(ps, timeout);
			weiboMethod.setParams(ps);

			HttpParams hps = client.getParams();
			HttpConnectionParams.setConnectionTimeout(hps, timeout);
			HttpConnectionParams.setSoTimeout(hps, timeout);
			client.setParams(hps);
			HttpResponse res = client.execute(weiboMethod);
			if (res.getStatusLine().getStatusCode() != 200) {
				String msg = new Date() + "：你妹的，还请求有问题[code:" + res.getStatusLine().getStatusCode() + "]！页面内容："
						+ EntityUtils.toString(res.getEntity(), "UTF-8");
				Logger.log(msg);
				if (handler != null && res.getStatusLine().getStatusCode() >= 500) {
					handler.record(new FailedRequest(url, node, msg));
				}
				EntityUtils.consumeQuietly(res.getEntity());
				return "";
			}
			// 获取登录过的微博页面
			String html = EntityUtils.toString(res.getEntity(), "UTF-8");
			EntityUtils.consumeQuietly(res.getEntity());
			if (!html.startsWith("{\"") && html.replaceAll(" ", "").indexOf("$CONFIG['oid']='") == -1) {
				// 不是登录状态。
				Logger.log("现在[" + url + "]是非登录状态，自动重新登录……");
				client = getHttpClient(LogonInfo.getLogonInfo().getUsername(), LogonInfo.getLogonInfo().getPassword());
				return request(client, url, handler, node);
			}
			if (html.indexOf("<p class=\\\"code_tit\\\">\\u4f60\\u7684\\u884c\\u4e3a\\u6709\\u4e9b\\u5f02\\u5e38\\uff0c\\u8bf7\\u8f93\\u5165\\u9a8c\\u8bc1\\u7801\\uff1a<\\/p>\\n") > -1) {
				// 如果存在这样的提示信息，则表示账号不能正常访问，所以，应该提示并等待！
				Logger.log("账号无法进行正常使用，被封了！休息一会儿！");
				sleep();
				return request(client, url, handler, node);
			} else {
				Logger.log("页面信息获取成功！" + url);
			}
			return html;
		} catch (Throwable e) {
			e.printStackTrace();
			return "";
		}
	}

	private static void sleep() {
		try {
			Logger.log("开始休息，休息一会儿！");
			Thread.sleep(waitInterval);
			Logger.log("休息完了，继续干活！");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 对登录成功的页面中的所有js链接作相应的请求，以获取最全的cookie信息！
	 * 
	 * @param client
	 * @param jsContent
	 */
	protected static void doCrossLogin(HttpClient client, String jsContent) {
		if (jsContent == null || jsContent.trim().length() == 0) {
			// 如果内容为空，则不操作
			return;
		}
		String startStr = "\"arrURL\":";
		int idx = jsContent.indexOf(startStr);
		if (idx > -1) {
			String urls = jsContent.substring(idx + startStr.length());
			urls = urls.substring(0, urls.indexOf("]") + 1);
			List<String> realUrls = JSON.parseArray(urls, String.class);
			if (realUrls != null && !realUrls.isEmpty()) {
				for (String url : realUrls) {
					try {
						HttpGet tmpMethod = new HttpGet(url);
						client.execute(tmpMethod);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						client.getConnectionManager().closeExpiredConnections();
					}
				}
			}
		}
	}

	/**
	 * 判断是否需要代理访问网络！因为我是在公司开发，公司访问网络是需要代理的！
	 */
	private static DefaultHttpClient newProxy() {
		DefaultHttpClient client = new DefaultHttpClient();
		// 连接超时设置5000
		HttpParams ps = client.getParams();
		ps.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		ps.setParameter("Referer", "http://weibo.com/");
		HttpConnectionParams.setConnectionTimeout(ps, timeout);
		HttpConnectionParams.setSoTimeout(ps, timeout);
		client.setParams(ps);
		if (hasProcProxy) {
			setProxy(client);
		} else {
			try {
				HttpResponse response = client.execute(new HttpGet("http://www.cnki.net/"));
				String charset = "UTF-8";
				String html = EntityUtils.toString(response.getEntity(), charset);
				if (response.getStatusLine().getStatusCode() == 403
						&& html.indexOf("http://oa.vemic.com/system_support/trouble_ticket_add.php") > -1) {
					setProxy(client);
					hasProcProxy = true;
				}
				EntityUtils.consumeQuietly(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	private static void setProxy(DefaultHttpClient client) {
		// 代理方式访问网络
		client.getCredentialsProvider().setCredentials(	new AuthScope("192.168.16.187", 8080),
														new UsernamePasswordCredentials("taofucheng", "taofuchok"));
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("192.168.16.187", 8080));
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.err.println(URLDecoder.decode(	"%CE%AA%C1%CB%C4%FA%B5%C4%D5%CA%BA%C5%B0%B2%C8%AB%A3%AC%C7%EB%CA%E4%C8%EB%D1%E9%D6%A4%C2%EB",
												"GBK"));
	}
}
