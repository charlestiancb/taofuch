package com.tfc.data.access.entity;

import com.tfc.data.access.RepositoryFactory;

/**
 * 二维结构的数据格式。如：int[i][j]
 * 
 * @author taofucheng
 * 
 */
public class FlatFormatData<T> extends AbstractFormatData {
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
		this.instanceName = instanceName + System.nanoTime() + random();
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
	public void save(int x, int y, T value) {
		if (valueClass == null && value != null) {
			valueClass = value.getClass();
		}
		String store = getStoreValue(value);
		RepositoryFactory.save(genarateKey(x, y), store);
	}

	@SuppressWarnings("unchecked")
	public T getValue(int x, int y) {
		if (valueClass == null) {
			return null;
		}
		String value = RepositoryFactory.findValueByKey(genarateKey(x, y));
		if ("NaN".equals(value)) {
			if (Double.class.isAssignableFrom(valueClass)) {
				return (T) new Double("NaN");
			} else if (Float.class.isAssignableFrom(valueClass)) {
				return (T) new Float("NaN");
			}
		}
		return (T) parseToObject(valueClass, value);
	}

	private String genarateKey(int x, int y) {
		return instanceName + "_" + x + "_" + y;
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