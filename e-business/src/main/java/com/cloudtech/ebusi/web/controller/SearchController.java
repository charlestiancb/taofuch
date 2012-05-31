package com.cloudtech.ebusi.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchController extends BaseController {
	/**
	 * 进入搜索页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(ModelMap model) {
		return "search/search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(String q, HttpServletResponse response, ModelMap model) {
		model(model, "q", q);
		return "search/list";
	}
}
