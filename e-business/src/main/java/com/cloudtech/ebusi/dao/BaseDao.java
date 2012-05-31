package com.cloudtech.ebusi.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoader;

public abstract class BaseDao {
	protected Log log = LogFactory.getLog(this.getClass());
	/** 数据所在的文件夹 */
	private static Resource data = null;

	protected List<String> readFile(String fileName) {
		if (data == null) {
			data = ContextLoader.getCurrentWebApplicationContext().getResource("/WEB-INF/data/");
		}
		List<String> lines = new ArrayList<String>();
		try {
			List<String> tmp = FileUtils.readLines(new File(data.getFile(), fileName), "UTF-8");
			if (tmp != null && !tmp.isEmpty()) {
				for (String l : tmp) {
					if (StringUtils.isNotEmpty(l) && !l.startsWith("--") && !l.startsWith("//")) {
						// 该行不为空，且不为注释，即--开头
						lines.add(l);
					}
				}
			}
		} catch (IOException e) {
			log.error("", e);
		}
		return lines;
	}
}
