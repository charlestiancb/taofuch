package com.cloudtech.ebusi.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloudtech.ebusi.web.controller.BaseController;

/**
 * 瓟虫管理器
 * 
 * @author taofucheng
 * 
 */
@Controller
@RequestMapping("/admin")
public class CrawlerController extends BaseController {
	@RequestMapping(value = "/crawler/config", method = RequestMethod.GET)
	public void config() {

	}
}
