package com.tfc.webtest.content;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.repo.Repository;
import com.tfc.webtest.utils.RegUtils;

/**
 * 普通文本的内容，包含变量的处理
 * 
 * @author taofucheng
 * 
 */
public class TextContent implements Content {

	public boolean isBelongTo(String content) {
		return true;
	}

	public Object eval(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo) {
		return RegUtils.replace(line.getContent(), vars);
	}

}
