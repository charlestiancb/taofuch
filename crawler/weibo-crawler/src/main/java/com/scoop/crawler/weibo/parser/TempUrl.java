package com.scoop.crawler.weibo.parser;

public class TempUrl {
	private String origUrl;
	private String reqUrl;

	public TempUrl() {
	}

	public TempUrl(String origUrl, String reqUrl) {
		setOrigUrl(origUrl);
		setReqUrl(reqUrl);
	}

	public String getOrigUrl() {
		return origUrl;
	}

	public void setOrigUrl(String origUrl) {
		this.origUrl = origUrl;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
}