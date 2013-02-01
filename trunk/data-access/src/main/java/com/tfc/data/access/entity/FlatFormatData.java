package com.tfc.data.access.entity;

import java.text.DecimalFormat;

import com.alibaba.fastjson.JSON;
import com.tfc.data.access.LuceneDataAccess;

/**
 * 二维结构的数据格式。如：int[i][j]
 * 
 * @author taofucheng
 * 
 */
public class FlatFormatData extends AbstractFormatData {
	private static DecimalFormat df = new DecimalFormat("0.000");
	private String instanceName;
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
	public FlatFormatData(String instanceName, int xLen, int yLen) {
		if (instanceName == null) {
			throw new IllegalArgumentException("请指定二维数组的实例名！");
		}
		this.instanceName = instanceName + System.nanoTime();
		this.xLen = xLen;
		this.yLen = yLen;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
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
		LuceneDataAccess.save(genarateKey(x, y), JSON.toJSONString(value));
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
		return (String) get(x, y, String.class);
	}

	public Object get(int x, int y, Class<?> targetElementClass) {
		String value = LuceneDataAccess.findValueByKey(genarateKey(x, y));
		return parseToObject(targetElementClass, value);
	}

	private String genarateKey(int x, int y) {
		return instanceName + "_" + x + "_" + y;
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

	/**
	 * 相当于s.length，其中，s=new int[x][y]
	 * 
	 * @return
	 */
	public int getxLen() {
		return xLen;
	}

	/**
	 * 相当于s[i].length，其中，s=new int[x][y]
	 * 
	 * @return
	 */
	public int getyLen() {
		return yLen;
	}

	public int length() {
		return getxLen();
	}

	public static void main(String[] args) {
		int[][] x = new int[2][3];
		System.out.println(x.length);// 结果2
		System.out.println(x[1].length);// 结果3
	}
}
