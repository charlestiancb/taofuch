package com.ss.language.model.pipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ss.language.model.data.DatabaseConfig;

/**
 * 每个管道节点
 * 
 * @author taofucheng
 * 
 */
public abstract class PipeNode {
	/** 每次处理数据数量 */
	protected static final int perPageRecords = 50;

	/** 处理入口 */
	public abstract void process();

	public String getName() {
		return this.getClass().getName();
	}

	protected long count(String countSql, Object... args) throws SQLException {
		Connection conn = DatabaseConfig.openConn();
		PreparedStatement ps = conn.prepareStatement(countSql);
		if (args != null && args.length > 0) {
			int i = 1;
			for (Object arg : args) {
				ps.setObject(i++, arg);
			}
		}
		ResultSet rs = ps.executeQuery();
		long count = 0;
		while (rs.next()) {
			count = rs.getLong(1);
		}
		rs.close();
		ps.close();
		return count;
	}
}
