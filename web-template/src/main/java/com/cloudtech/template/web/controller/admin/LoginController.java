package com.cloudtech.template.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudtech.template.web.controller.BaseController;

/**
 * 后台管理登录相关的Controller
 * 
 * @author taofucheng
 * 
 */
@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController {
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return null;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Model model) {
		return null;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		return null;
	}
}
