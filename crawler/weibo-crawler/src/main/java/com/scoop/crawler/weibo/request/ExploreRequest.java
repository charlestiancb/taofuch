package com.scoop.crawler.weibo.request;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * 使用浏览器功能进行访问，使用这个需要已经提供登录信息。
 * 
 * @author taofucheng
 * 
 */
public class ExploreRequest {
	public static void main(String[] args) {
		// ie();
		// chrome();
		// firefox();
	}

	/**
	 * 使用IE浏览器访问
	 * 
	 * @param client
	 * @param url
	 * @return
	 */
	public static WebDriver ie(DefaultHttpClient client, String url) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.ie.driver", "src/main/java/driver/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			return requestWithCookie(client, url, driver);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static WebDriver chrome(DefaultHttpClient client, String url) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.chrome.driver", "src/main/java/driver/chromedriver.exe");
			driver = new ChromeDriver();
			return requestWithCookie(client, url, driver);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static WebDriver firefox(DefaultHttpClient client, String url) {
		WebDriver driver = null;
		try {
			driver = new FirefoxDriver();
			return requestWithCookie(client, url, driver);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用cookie方式进行请求！
	 * 
	 * @param client
	 * @param url
	 * @param driver
	 * @return
	 */
	private static WebDriver requestWithCookie(DefaultHttpClient client, String url, WebDriver driver) {
		try {
			List<Cookie> cs = client.getCookieStore().getCookies();
			if (cs != null && cs.size() > 0) {
				for (Cookie c : cs) {
					driver.manage().addCookie(new org.openqa.selenium.Cookie(c.getName(), c.getValue()));
				}
			}
			driver.get(url);
		} catch (Exception e) {
			driver.close();
			System.exit(0);
		}
		return driver;
	}
}
