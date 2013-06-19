package com.cloudtech.template.web.controller.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public void demo(Model model) {
		System.out.println("进入demo页面！");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void login(Model model) {
		System.out.println("进入login页面！");
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(Model model) {
		System.out.println("进入logout页面！");
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public void home(Model model) {
		System.out.println("登录后进入首页");
	}

	@RequestMapping(value = "/bye", method = RequestMethod.GET)
	public void bye(Model model, HttpServletResponse response) throws IOException {
		System.out.println("进入bye-bye页面！");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write("<center>感谢您的使用！</center>");
		response.getWriter().flush();
	}
}
