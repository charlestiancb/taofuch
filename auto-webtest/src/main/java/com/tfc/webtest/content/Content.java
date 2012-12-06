package com.tfc.webtest.content;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.repo.Repository;

/**
 * 用例，或数据定义中参数与函数那一列的内容类型
 * 
 * @author taofucheng
 * 
 */
public interface Content {
	/**
	 * 根据内容判断该内容是否是当前类型的数据
	 * 
	 * @param content
	 *            用例数据中指定的内容
	 * @return
	 */
	public boolean isBelongTo(String content);

	/**
	 * 执行并返回执行之后的内容！
	 * 
	 * @param driver
	 * @param line
	 * @param vars
	 * @param repo
	 * @return
	 */
	public Object eval(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo);
}
