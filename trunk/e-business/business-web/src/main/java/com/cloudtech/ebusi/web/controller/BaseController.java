package com.cloudtech.ebusi.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;

public abstract class BaseController {
	/**
	 * 指定消息写到客户端去
	 * 
	 * @param message
	 * @param response
	 */
	protected void render(Object message, HttpServletResponse response) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(JSON.toJSONString(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将指定的内容放进ModelMap中。
	 * 
	 * @param model
	 * @param nameValuePare
	 *            名称与变量值对应关系，如："q",q,"name",ComName
	 */
	protected void model(ModelMap model, Object... nameValuePare) {
		if (nameValuePare == null || nameValuePare.length == 0) {
			return;
		}
		if (nameValuePare.length % 2 == 1) {
			// 如果有余数，说明变量名称与值不匹配
			throw new IllegalArgumentException("nameValuePare 没有配对！");
		}
		int len = nameValuePare.length / 2;
		for (int i = 0; i < len; i++) {
			model.addAttribute(String.valueOf(nameValuePare[2 * i]), nameValuePare[2 * i + 1]);
		}
	}
}
