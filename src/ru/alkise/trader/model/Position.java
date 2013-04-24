package ru.alkise.trader.model;

public class Position {
	private Goods goods;
	private double count;
	private Warehouse whFrom;
	private Warehouse whTo;

	public Position(Goods goods, double count, Warehouse whFrom, Warehouse whTo) {
		this(goods, count, whFrom);
		this.whTo = whTo;
	}

	public Position(Goods goods, double count, Warehouse whFrom) {
		this.goods = goods;
		this.count = count;
		this.whFrom = whFrom;
	}

	public long getId() {
		return whFrom.getCode() + goods.getCode();
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
		StringBuilder sb = new StringBuilder(goods.getDescr());
		sb.append(' ');
		sb.append(whFrom.getDescr());
		sb.append(' ');
		sb.append(count);
		return sb.toString();
	}
}
