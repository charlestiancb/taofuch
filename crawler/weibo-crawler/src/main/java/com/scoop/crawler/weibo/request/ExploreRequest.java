package com.scoop.crawler.weibo.request;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.scoop.crawler.weibo.entity.LogonInfo;

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
			System.setProperty("webdriver.ie.driver", "src/main/java/driver/IEDriverServer.exe");
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
			System.setProperty("webdriver.chrome.driver", "src/main/java/driver/chromedriver.exe");
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
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			driver.get("http://www.weibo.com/");
			// 这次访问肯定需要登录！因此登录之
			driver.findElement(By.name("loginname")).sendKeys(LogonInfo.getLogonInfo().getUsername());
			driver.findElement(By.name("password")).sendKeys(LogonInfo.getLogonInfo().getPassword());
			Thread.sleep(2 * 1000);// 2秒
			WebElement door = driver.findElement(By.name("door"));
			if (door != null && door.isDisplayed()) {
				Thread.sleep(5 * 1000);// 等待5s
				System.out.println("你也看到了，要输入验证码的，我做不了了！我撤了，拜拜~");
				driver.quit();
				System.exit(0);
			} else {
				driver.findElement(By.className("W_btn_d")).click();
				Thread.sleep(5 * 1000);// 等待5s
				// 解析页面
				driver.navigate().to(url);// 打开指定页面
				Thread.sleep(5 * 1000);// 等待5s
			}
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
			driver.quit();
			System.exit(0);
		}
		return null;
	}
}
