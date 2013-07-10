package com.cloudtech.template.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	public String json(Model model) {
		model.addAttribute("key", "hello");
		return "public/error";
	}

	@RequestMapping(value = "/pic.jpg", method = RequestMethod.GET)
	public void pic(Model model, HttpServletResponse response) throws IOException {
		response.sendRedirect("http://www.iteye.com/upload/top_picture/0000/0016/564654654654.jpg?1372920966");
		return;
	}
}
