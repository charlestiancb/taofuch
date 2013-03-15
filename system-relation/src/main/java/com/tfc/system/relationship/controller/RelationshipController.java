package com.tfc.system.relationship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tfc.system.relationship.service.RelationshipService;

@Controller
@RequestMapping("/relationship")
public class RelationshipController {
	@Autowired
	private RelationshipService relationService;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String add(Model model) {
		return "relation/all";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String add(String[] relations, Model model) {
		return "relation/all";
	}

	/**
	 * 查看指定的系统的调用关系
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(String id, Model model) {
		return "relation/show";
	}

	/**
	 * 查看指定的所有的系统的调用关系
	 * 
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/relations", method = RequestMethod.GET)
	public String showRelations(String[] ids, Model model) {
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
	public String showAll(Model model) {
		return "relation/all";
	}
}