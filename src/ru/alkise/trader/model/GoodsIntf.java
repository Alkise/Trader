package ru.alkise.trader.model;

import java.io.Serializable;

public interface GoodsIntf extends Serializable {
	// UNIQUE ID
	public static final String GOODS_CODE = "GoodsCode";

	// GOODS NAME
	public static final String GOODS_NAME = "GoodsName";

	// GOODS REMAINS
	public static final String GOODS_REMAINS = "GoodsRemains";

	// TABLE NAME
	public static final String TABLE_NAME = "Goods";

	int getGoodsCode();
	void setGoodsCode(int code);
	String getGoodsName();
	void setGoodsName(String descr);
	double getGoodsRemains();
	void setGoodsRemains(double count);
}
