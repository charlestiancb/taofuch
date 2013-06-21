package com.scoop.crawler.weibo.request.failed;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.fetch.FetchSinaWeibo;
import com.scoop.crawler.weibo.parser.CommentParser;
import com.scoop.crawler.weibo.parser.UserWeiboParser;
import com.scoop.crawler.weibo.repository.DataSource;
import com.scoop.crawler.weibo.repository.mysql.EntityTransfer;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.request.ExploreRequest;
import com.scoop.crawler.weibo.util.Logger;

/**
 * 请求失败的请求处理。
 * 
 * @author taofucheng
 * 
 */
public class RequestFailedHandler extends FailedHandler {
	public RequestFailedHandler(DefaultHttpClient client, DataSource dataSource) {
		super(client, dataSource);
	}

	private boolean hasTry = false;

	/**
	 * 记录失败的链接！
	 * 
	 * @param request
	 */
	public void record(FailedRequest request) {
		if (request != null) {
			getDataSource().saveFailedRequest(request);
		}
	}

	@Override
	public void reTry() {
		if (hasTry) {
			// 如果已经在运行了，则不再运行！
			return;
		}
		hasTry = true;
		Thread t = new Thread(new HandlerRunnable());
		t.setName(RequestFailedHandler.class.getSimpleName() + "#reTry");
		t.start();
	}

	class HandlerRunnable implements Runnable {

		public void run() {
			FailedRequest req = getDataSource().pop();
			if (req == null || req.getRecId() == null) {
				Logger.log("没有失败的记录，继续……");
			}
			// 处理每条失败的请求！
			try {
				if (req == null || req.getRecId() == null) {
					Logger.log("没有失败的记录，继续……");
					// 如果没有失败记录，则等待30分钟！
					Thread.sleep(30 * 60 * 1000);
				} else {
					Logger.log("有失败的记录，线程进入激活抓取状态！");
					fetch(req);
				}
				req = getDataSource().pop();
			} catch (Exception e) {
			}
		}

	}

	private void fetch(FailedRequest req) {
		// 存储失败记录，则处理！
		try {
			FailedNode fn = FailedNode.valueOf(req.getFailedNode());
			if (fn != null) {
				switch (fn) {
				case MAIN:
					FetchSinaWeibo.fetch(getClient(), getDataSource(), req.getUrl(), null);
					break;
				case USER_WEIBO:
					new UserWeiboParser(getDataSource(), this).reTry(getClient(), req.getUrl(), fn);
					break;
				case SINGLE_WEIBO:
					getDataSource().saveWeibo(new OneWeiboInfo(req.getUrl(), getClient()));
					break;
				case COMMENT:
					WebDriver driver = ExploreRequest.getDriver(null);
					try {
						OneWeiboInfo weibo = new OneWeiboInfo(req.getUrl(), getClient());
						new CommentParser(getDataSource(), this).fetchWeiboComments(driver,
																					EntityTransfer.parseWeibo(weibo),
																					getClient());
					} catch (Exception e) {
					} finally {
						driver.quit();
					}
					break;
				case PERSON:
					WeiboPersonInfo person = new WeiboPersonInfo(req.getUrl(), getClient());
					getDataSource().savePerson(person);
					break;
				case FOLLOWS:
				case FANS:
					String url = req.getUrl();
					url = url.substring(0, url.lastIndexOf("/"));
					url = url + "/info";
					WeiboPersonInfo _person = new WeiboPersonInfo(url, getClient());
					getDataSource().savePerson(_person);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			req.setFailedReason(StringUtils.substring(ExceptionUtils.getFullStackTrace(e), 0, 4000));
			record(req);
		}

	}
}
