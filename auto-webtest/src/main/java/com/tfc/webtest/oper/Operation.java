package com.tfc.webtest.oper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tfc.webtest.constants.LocationStyle;
import com.tfc.webtest.constants.OperType;
import com.tfc.webtest.content.Content;
import com.tfc.webtest.content.ContentFactory;
import com.tfc.webtest.entity.CaseLine;
import com.tfc.webtest.repo.Repository;

/**
 * 操作类型
 * 
 * @author taofucheng
 * 
 */
public abstract class Operation {
	private Object content;

	/**
	 * 操作的名称
	 * 
	 * @return
	 */
	public abstract OperType getName();

	/**
	 * 执行具体的操作。这里的参数都确定不为null
	 * 
	 * @param driver
	 *            具体的浏览器驱动
	 * @param line
	 *            当前操作所在那一行的数据
	 * @param vars
	 *            用于存储临时变量
	 * @param repo
	 *            数据库操作类
	 */
	public void exec(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo) {
		execute(driver, line, vars, repo);
		if (StringUtils.isNotBlank(line.getVar())) {
			// 如果指定了变量名，则存储变量
			vars.put(line.getVar().trim().toUpperCase(), content);
		}
	}

	/**
	 * 执行具体的操作。这里的参数都确定不为null
	 * 
	 * @param driver
	 *            具体的浏览器驱动
	 * @param line
	 *            当前操作所在那一行的数据
	 * @param vars
	 *            用于存储临时变量
	 * @param repo
	 *            数据库操作类
	 */
	protected abstract void execute(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo);

	/**
	 * 解析参数与函数一列的数据
	 * 
	 * @param driver
	 * @param line
	 * @param vars
	 * @param repo
	 * @return
	 */
	protected Object parseContent(WebDriver driver, CaseLine line, Map<String, Object> vars, Repository repo) {
		String _content = line.getContent();
		Content c = ContentFactory.findInstance(_content);
		if (c != null) {
			content = c.eval(driver, line, vars, repo);
		}
		return content;
	}

	/**
	 * 寻找元素。
	 * 
	 * @param pageEle
	 * @param locStyle
	 */
	protected WebElement findElment(WebDriver driver, String pageEle, String locStyle) {
		if (driver == null) {
			System.err.println("查找页面元素[" + locStyle + "=" + pageEle + "]时，发现WebDriver为空！");
			return null;
		}
		By b = null;
		try {
			LocationStyle style = LocationStyle.getByValue(Integer.parseInt(locStyle));
			if (style == null) {
				System.err.println("");
				return null;
			}
			switch (style) {
			case id:
				b = By.id(pageEle);
				break;
			case name:
				b = By.name(pageEle);
				break;
			case tag_name:
				b = By.tagName(pageEle);
				break;
			case class_name:
				b = By.className(pageEle);
				break;
			case css:
				b = By.cssSelector(pageEle);
				break;
			case xpath:
				b = By.xpath(pageEle);
				break;
			case link_text:
				b = By.linkText(pageEle);
				break;
			default:
				System.err.println("页面元素[" + pageEle + "]的定位方式[" + locStyle + "]不支持！");
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return driver.findElement(b);
	}

	/** 对于这个操作来说，参数那一列是不是可接收用户输入的内容 */
	public abstract boolean canInput();

}
