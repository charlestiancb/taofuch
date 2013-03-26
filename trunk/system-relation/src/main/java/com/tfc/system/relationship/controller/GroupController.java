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

import com.tfc.system.relationship.entity.SystemGroup;
import com.tfc.system.relationship.service.GroupService;

@Controller
@RequestMapping("/group")
public class GroupController {
	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(Model model) {
		return "group/add";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(SystemGroup group, Model model) {
		try {
			groupService.add(group);
		} catch (ConstraintViolationException e) {
			model.addAttribute("errMsg", "添加失败！该名称的分组信息已经存在！" + e);
			return "group/add";
		} catch (Exception e) {
			model.addAttribute("errMsg", "添加失败！原因：" + e);
			return "group/add";
		}
		return "redirect:/group/";
	}

	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String delete(@PathVariable String id) {
		groupService.delete(id);
		return "redirect:/group/";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String modify(@PathVariable String id, Model model) {
		model.addAttribute(groupService.getById(id));
		return "group/modify";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String modify(SystemGroup group, Model model) {
		try {
			groupService.modify(group);
		} catch (Exception e) {
			model.addAttribute("errMsg", "修改失败！原因：" + e.getMessage());
			return "group/modify";
		}
		return "redirect:/group/";
	}

	@RequestMapping(value = "/{id}/show", method = RequestMethod.GET)
	public String show(@PathVariable String id, Model model) {
		model.addAttribute(groupService.getById(id));
		return "group/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("groups", groupService.getAll());
		return "group/list";
	}

	@RequestMapping(value = "/{groupId}/order", method = RequestMethod.POST)
	public void changeOrder(@PathVariable String groupId, Long orderNum, HttpServletResponse response)
			throws IOException {
		String msg = "";
		try {
			groupService.changeOrder(groupId, orderNum);
			msg = "SUCC!";
		} catch (Exception e) {
			msg = "FAILED! " + e;
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
	}
}
