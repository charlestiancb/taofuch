package com.tfc.system.relationship.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tfc.system.relationship.service.RelationshipService;
import com.tfc.system.relationship.service.SystemService;

@Controller
@RequestMapping("/relationship")
public class RelationshipController {
	@Autowired
	private SystemService sysService;
	@Autowired
	private RelationshipService relationService;

	@RequestMapping(value = "/conf", method = RequestMethod.GET)
	public String conf(Model model) {
		model.addAttribute("systems", sysService.getAll());
		return "relation/conf";
	}

	/**
	 * 配置指定系统的关系
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{sysId}/conf", method = RequestMethod.GET)
	public String modify(@PathVariable String sysId, Model model) {
		model.addAttribute(sysService.getById(sysId));
		model.addAttribute("systems", sysService.getAll());
		return "relation/modify";
	}

	@RequestMapping(value = "/{sysId}/conf", method = RequestMethod.POST)
	public void modify(
			@PathVariable String sysId,
			String selectedSysId,
			String introduce,
			int RelationType,
			int operType,
			HttpServletResponse response) throws IOException {
		String msg = "";
		try {
			relationService.modifyRelation(Long.parseLong(sysId), Long.parseLong(selectedSysId), introduce,
					RelationType, operType);
			msg = "更新成功！";
		} catch (Exception e) {
			msg = "更新失败！" + e;
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String conf(String[] relations, String[] introduces, Model model) {
		try {
			relationService.modify(relations, introduces);
		} catch (Exception e) {
			model.addAttribute("systems", sysService.getAll());
			return "relation/conf";
		}
		return "redirect:/relationship";
	}

	/**
	 * 查看指定的系统的调用关系
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable String id, Model model) {
		model.addAttribute("systems", sysService.getByIds(id));
		return "relation/show";
	}

	/**
	 * 查看指定的所有的系统的调用关系
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/relations", method = RequestMethod.POST)
	public String showRelations(String[] ids, Model model) {
		model.addAttribute("systems", sysService.getByIds(ids));
		return "relation/show";
	}

	/**
	 * 查看所有系统的调用关系
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showAll(Long groupId, Model model) {
		if (groupId != null) {
			model.addAttribute("systems", sysService.getByGroupId(groupId));
		} else {
			model.addAttribute("systems", sysService.getAll());
		}
		return "relation/all";
	}
}