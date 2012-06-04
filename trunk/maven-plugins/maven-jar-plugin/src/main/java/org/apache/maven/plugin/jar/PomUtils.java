package org.apache.maven.plugin.jar;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * POM文件工具类
 * 
 * @author taofucheng
 * 
 */
public class PomUtils {
	private static final SAXBuilder builder = new SAXBuilder();

	/**
	 * 设置项目的一些属性配置，如artifactId等
	 * 
	 * @param propertyValuePair
	 *            属性与值的配置
	 */
	public static File setProjectProperty(File pomFile, String... propertyValuePair) {
		if (!pomFile.isFile()) {
			return pomFile;
		}
		if (propertyValuePair == null || propertyValuePair.length % 2 == 1) {
			System.err.println("propertyValuePair is not correct!");
			return pomFile;
		}
		Document doc = null;
		Element root = null;
		try {
			doc = builder.build(pomFile);
			root = doc.getRootElement();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (doc == null || root == null) {
			return pomFile;
		}
		for (int i = 0; i < propertyValuePair.length / 2; i++) {
			String prop = StringUtils.trim(propertyValuePair[2 * i]);
			String value = StringUtils.trim(propertyValuePair[2 * i + 1]);
			if (StringUtils.isEmpty(prop) || StringUtils.isEmpty(value)) {
				continue;
			}
			Element target = root.getChild(prop, root.getNamespace());
			if (target != null) {
				target.setText(value);
			}
		}
		try {
			File tmpFile = new File(pomFile.getParentFile(), "tmp_" + FilenameUtils.getName(pomFile.getName()));
			if (tmpFile.isFile()) {
				FileUtils.forceDelete(tmpFile);
			}
			XMLOutputter outp = new XMLOutputter();
			outp.output(doc, new FileOutputStream(tmpFile));
			return tmpFile;
		} catch (Exception e) {
			return pomFile;
		}
	}
}
