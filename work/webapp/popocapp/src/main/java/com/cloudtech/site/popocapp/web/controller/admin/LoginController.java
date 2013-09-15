package com.cloudtech.site.popocapp.web.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 后台管理登录相关的Controller
 * 
 * @author taofucheng
 * 
 */
@Controller
public class LoginController extends BaseAdminController {
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public void demo(Model model) {
		System.out.println("进入demo页面！");
	}

	@RequestMapping(value = "/login")
	public void login(HttpServletRequest request, Model model) {
		Exception e = (Exception) request
				.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (e != null) {
			// 说明是登录的情形！
			model.addAttribute("message", e.getMessage());
		} else {
			model.addAttribute("message", "请登录！");
		}
		model.addAttribute("j_username", request.getParameter("j_username"));
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(Model model) {
		System.out.println("进入logout页面！");
	}

	@RequestMapping(value = "/home")
	public void home(Model model) {
		System.out.println("登录后进入首页");
	}
}
