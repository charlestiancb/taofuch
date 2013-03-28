package com.scoop.crawler.weibo.request;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.scoop.crawler.weibo.entity.LogonInfo;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 使用浏览器功能进行访问，使用这个需要已经提供登录信息。
 * 
 * @author taofucheng
 * 
 */
public class ExploreRequest {
	public static void main(String[] args) {
		LogonInfo.store("sszcgfss@gmail.com", "jmi2009095");
		// ie();
		// chrome();
		firefox("http://gov.weibo.com/profile.php?uid=sciencenet&ref=");
	}

	/**
	 * 使用IE浏览器访问
	 * 
	 * @param client
	 * @param url
	 * @return
	 */
	public static WebDriver ie(String url) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.ie.driver", "src/main/resources/driver/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			return loginAndRequest(driver, url);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static WebDriver chrome(String url) {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver.exe");
			driver = new ChromeDriver();
			return loginAndRequest(driver, url);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static WebDriver firefox(String url) {
		WebDriver driver = null;
		try {
			//System.setProperty("webdriver.firefox.bin", "D:/Program Files/Mozilla Firefox/firefox.exe");
			driver = new FirefoxDriver();
			return loginAndRequest(driver, url);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 页面登录，然后请求指定页面。
	 * 
	 * @param driver
	 * @param url
	 * @return
	 */
	private static WebDriver loginAndRequest(WebDriver driver, String url) {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			// driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			// driver.manage().timeouts().setScriptTimeout(30,
			// TimeUnit.SECONDS);
			driver.get("http://www.weibo.com/");
			// 这次访问肯定需要登录！因此登录之
			driver.findElement(By.name("username")).sendKeys(LogonInfo.getLogonInfo().getUsername());
			driver.findElement(By.name("password")).sendKeys(LogonInfo.getLogonInfo().getPassword());
			Thread.sleep(2 * 1000);// 2秒，等待是否有验证码出现。
			WebElement verifycode = driver.findElement(By.name("verifycode"));
			if (verifycode != null && verifycode.isDisplayed()) {
				Logger.log("你也看到了，要输入验证码的，我做不了了！我撤了，拜拜~");
				Thread.sleep(5 * 1000);// 等待5s
				driver.quit();
				driver = null;
			} else {
				driver.findElement(By.className("W_btn_g")).click();
				verifycode = driver.findElement(By.name("verifycode"));
				if (verifycode != null && verifycode.isDisplayed()) {
					Logger.log("你也看到了，要输入验证码的，我做不了了！我撤了，拜拜~");
					Thread.sleep(5 * 1000);// 等待5s
					driver.quit();
					driver = null;
				}
				Thread.sleep(5 * 1000);// 等待5s
				if (StringUtils.isNotBlank(url)) {
					// 解析页面
					driver.navigate().to(url);// 打开指定页面
					Thread.sleep(3 * 1000);// 等待5s
				}
			}
			return driver;
		} catch (UnreachableBrowserException e) {
			// 超时的情况不鸟它！
			Logger.log("页面中有超时的链接，应该不影响结果，继续操作！");
		} catch (Throwable e) {
			e.printStackTrace();
			driver.quit();
		}
		return null;
	}

	/**
	 * 获取驱动信息。这个方法主要是方便随时切换浏览器。
	 * 
	 * @param url
	 * @return
	 */
	public static WebDriver getDriver(String url) {
		url = StringUtils.trim(url);
		return firefox(url);
	}

	public static String getPageHtml(WebDriver driver) {
		if (driver == null) {
			return "";
		}
		String html = driver.getPageSource();
		while (html.indexOf("$CONFIG['oid'] = '") == -1) {
			// 如果没有当前人信息，则是没有登录的！
			driver = getDriver(null);
			if (driver == null) {
				Logger.log("登录失败！停止抓取！请重新启动！");
				System.exit(-1);
			}
			html = driver.getPageSource();
		}
		return html;
	}
}
