package ru.alkise.trader.model;


public class Goods implements GoodsIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String descr;
	private double count;

	public Goods(int code, String descr, double count) {
		this.code = code;
		this.descr = descr.trim();
		this.count = count;
	}

	@Override
	public int getGoodsCode() {
		return code;
	}

	@Override
	public void setGoodsCode(int code) {
		this.code = code;
	}

	@Override
	public String getGoodsName() {
		return descr;
	}

	@Override
	public void setGoodsName(String descr) {
		this.descr = descr.trim();
	}

	@Override
	public double getGoodsRemains() {
		return count;
	}

	@Override
	public void setGoodsRemains(double count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof GoodsIntf) {
			return ((GoodsIntf) o).getGoodsCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return code + " " + descr;
	}
}
