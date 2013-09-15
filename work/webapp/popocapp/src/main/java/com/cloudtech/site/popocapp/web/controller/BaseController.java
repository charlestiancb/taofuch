package com.cloudtech.site.popocapp.web.controller;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

public abstract class BaseController {
	/**
	 * 指定消息写到客户端去
	 * 
	 * @param message
	 * @param response
	 */
	protected void renderJson(Object message, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(JSON.toJSONString(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
