package com.cloudtech.ebusi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController extends BaseController {
	@RequestMapping("/")
	public String home(ModelMap model) {
		return "search/search";
	}
}
