package com.cloudtech.template.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public void json(Model model) {
		model.addAttribute("key", "hello");
	}
}
