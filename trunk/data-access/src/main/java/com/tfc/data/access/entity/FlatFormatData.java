package com.tfc.data.access.entity;

import com.tfc.data.access.RepositoryFactory;

/**
 * 二维结构的数据格式。如：int[i][j]
 * 
 * @author taofucheng
 * 
 */
public class FlatFormatData<T> extends AbstractFormatData<T> {
	private int xLen = 0;
	private int yLen = 0;

	private int xCurLen = 0;
	private int yCurLen = 0;

	public FlatFormatData() {
		this("def", 0, 0);
	}

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
		setInstanceName(instanceName + System.nanoTime() + random());
		this.xLen = xLen;
		this.yLen = yLen;
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
	public void save(int x, int y, T value) {
		if (getValueClass() == null && value != null) {
			setValueClass(value.getClass());
		}
		String store = getStoreValue(value);
		boolean ret = RepositoryFactory.save(genarateKey(x, y), store);
		if (ret) {
			xCurLen = x > xCurLen ? x : xCurLen;
			yCurLen = y > yCurLen ? y : yCurLen;
		}
	}

	public T getValue(int x, int y) {
		if (getValueClass() == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(x, y));
		return parseValue(value);
	}

	private String genarateKey(int x, int y) {
		return getInstanceName() + "_" + x + "_" + y;
	}

	/**
	 * 相当于s.length，其中，s=new int[x][y]
	 * 
	 * @return
	 */
	public int getxLen() {
		return xLen > xCurLen ? xLen : xCurLen;
	}

	/**
	 * 相当于s[i].length，其中，s=new int[x][y]
	 * 
	 * @return
	 */
	public int getyLen() {
		return yLen > yCurLen ? yLen : yCurLen;
	}

	/**
	 * 相当于s.length，其中，s=new int[x][y]
	 * 
	 * @return
	 */
	public int length() {
		return getxLen();
	}

	public static void main(String[] args) {
		int[][] x = new int[2][3];
		System.out.println(x.length);// 结果2
		System.out.println(x[1].length);// 结果3
	}
}
