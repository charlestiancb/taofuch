/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tfc.evolve.service.EvolveService;

/**
 * 智能入口
 * 
 * @author taofucheng
 */
@Controller
@RequestMapping("/evolution")
public class EvolveController {
    @Autowired
    private EvolveService evolveService;

    @RequestMapping(value = "/talk", method = RequestMethod.GET)
    public String talk(Model model) {
        model.addAttribute("welcome", evolveService.welcome());
        return "evolve/entry";
    }

    @RequestMapping(value = "/talk", method = RequestMethod.POST)
    public String talk(String content, Model model) {
        model.addAttribute(evolveService.alternate(content));
        return "evolve/talk";
    }
}
