package com.ss.language.model.gibblda;

import java.text.DecimalFormat;

/**
 * 矩阵形式的数字的数据结构，即二维数组形式的结构。具体一定的缓存能力！
 * 
 * @author taofucheng
 * 
 */
public class FlatNumberDictionary {
	private static DecimalFormat df = new DecimalFormat("0.000");
	private String instanceName;
	private MysqlDataAccess dataAccess;
	private int xLen = 0;
	private int yLen = 0;

	/**
	 * 定义一个矩阵结构
	 * 
	 * @param instanceName
	 *            矩阵实例名称
	 * @param xLen
	 *            x坐标的长度
	 * @param yLen
	 *            y坐标的长度
	 */
	public FlatNumberDictionary(String instanceName, int xLen, int yLen) {
		if (instanceName == null) {
			throw new IllegalArgumentException("请指定二维数组的实例名！");
		}
		this.instanceName = instanceName;
		this.xLen = xLen;
		this.yLen = yLen;
		dataAccess = MysqlDataAccess.getInstance(this.instanceName);
	}

	/**
	 * 保存数据到指定的点上！
	 * 
	 * @param x
	 *            横坐标，从0开始计数
	 * @param y
	 *            纵坐标，从0开始计数
	 * @param value
	 */
	public void save(int x, int y, Object value) {
		if (value instanceof Double) {
			// 将小数点保留到后三位即可！
			value = df.format(value);
		}
		dataAccess.save(x + "_" + y, String.valueOf(value));
	}

	/**
	 * 获取指定平面上具体的点上的数据
	 * 
	 * @param x
	 *            横坐标，从0开始计数
	 * @param y
	 *            纵坐标，从0开始计数
	 * @return
	 */
	public String fetch(int x, int y) {
		return dataAccess.findValueByKey(x + "_" + y);
	}

	/**
	 * 获取指定坐标上的double类型的返回值
	 * 
	 * @param x
	 *            横坐标，从0开始计数
	 * @param y
	 *            纵坐标，从0开始计数
	 * @return
	 */
	public double getDouble(int x, int y) {
		String r = fetch(x, y);
		if (r == null || r.trim().isEmpty()) {
			return 0.0;
		} else {
			return Double.parseDouble(r);
		}
	}

	/**
	 * 获取指定坐标上的int类型的返回值
	 * 
	 * @param x
	 *            横坐标，从0开始计数
	 * @param y
	 *            纵坐标，从0开始计数
	 * @return
	 */
	public int getInt(int x, int y) {
		String r = fetch(x, y);
		if (r == null || r.trim().isEmpty()) {
			return 0;
		} else {
			return Integer.parseInt(r);
		}
	}

	public int getxLen() {
		return xLen;
	}

	public int getyLen() {
		return yLen;
	}

}
