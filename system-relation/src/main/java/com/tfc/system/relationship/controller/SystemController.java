package com.tfc.system.relationship.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tfc.system.relationship.entity.SystemInfo;
import com.tfc.system.relationship.service.GroupService;
import com.tfc.system.relationship.service.SystemService;

@Controller
@RequestMapping("/sys")
public class SystemController {
	@Autowired
	private SystemService sysService;
	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(Long groupId, Model model) {
		SystemInfo sys = new SystemInfo();
		if (groupId != null) {
			sys.setGroupId(groupId);
		}
		model.addAttribute(sys);
		model.addAttribute("groups", groupService.getAll());
		return "sys/add";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(SystemInfo sys, Model model) {
		try {
			sysService.add(sys);
			return "redirect:/sys/";
		} catch (ConstraintViolationException e) {
			model.addAttribute("errMsg", "添加失败！该名称的系统的已经存在！");
		} catch (Exception e) {
			model.addAttribute("errMsg", "添加失败！原因：" + e);
		}
		model.addAttribute("groups", groupService.getAll());
		return "sys/add";
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable String id) {
		sysService.delete(id);
		return "redirect:/sys/";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String modify(@PathVariable String id, Model model) {
		model.addAttribute(sysService.getById(id));
		model.addAttribute("groups", groupService.getAll());
		return "sys/modify";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String modify(SystemInfo sys, Model model) {
		try {
			sysService.modify(sys);
		} catch (Exception e) {
			model.addAttribute("errMsg", "修改失败！原因：" + e.getMessage());
			model.addAttribute("groups", groupService.getAll());
			return "sys/modify";
		}
		return "redirect:/sys/";
	}

	@RequestMapping(value = "/{id}/show", method = RequestMethod.GET)
	public String show(@PathVariable String id, Model model) {
		model.addAttribute(sysService.getById(id));
		return "sys/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("groups", groupService.getAll());
		return "sys/list";
	}

	@RequestMapping(value = "/{sysId}/order", method = RequestMethod.POST)
	public void changeOrder(@PathVariable String sysId, Long orderNum, HttpServletResponse response) throws IOException {
		String msg = "";
		try {
			sysService.changeOrder(sysId, orderNum);
			msg = "SUCC!";
		} catch (Exception e) {
			msg = "FAILED! " + e;
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
	}
}
