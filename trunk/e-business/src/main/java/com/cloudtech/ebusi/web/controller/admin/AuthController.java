package com.cloudtech.ebusi.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudtech.ebusi.web.controller.BaseController;

@Controller
@RequestMapping("/admin")
public class AuthController extends BaseController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		model(model, "q", "登录");
		return "admin/login";
	}
}
