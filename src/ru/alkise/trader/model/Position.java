package ru.alkise.trader.model;

import java.io.Serializable;

public class Position implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//GOODS ID
	public static final String GOODS_CODE = "position_goods_code";
	
	//CURRENT COUNT
	public static final String COUNT = "position_count";
	
	//FROM WAREHOUSE ID
	public static final String FORM_WAREHOUSE_CODE = "position_from_warehouse_code";
	
	//TO WAREHOUSE ID
	public static final String TO_WAREHOUSE_CODE = "position_to_warehouse_code";
	
	//TABLE NAME
	public static final String TABLE_NAME = "position";
	
	private Goods goods;
	private double count;
	private Warehouse whFrom;
	private Warehouse whTo;

	public Position(Goods goods, double count, Warehouse whFrom, Warehouse whTo) {
		this.goods = goods;
		this.count = count;
		this.whFrom = whFrom;
		this.whTo = whTo;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public Warehouse getWhFrom() {
		return whFrom;
	}

	public void setWhFrom(Warehouse whFrom) {
		this.whFrom = whFrom;
	}

	public Warehouse getWhTo() {
		return whTo;
	}

	public void setWhTo(Warehouse whTo) {
		this.whTo = whTo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(whFrom.getDescr());
		sb.append(' ');
		sb.append(count);
		return sb.toString();
	}
}
