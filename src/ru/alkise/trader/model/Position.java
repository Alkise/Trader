package ru.alkise.trader.model;

public class Position {
	private Goods goods;
	private double count;
	private long id;
	private Warehouse whFrom;
	private Warehouse whTo;

	public Position(Goods goods, double count, Warehouse whFrom, Warehouse whTo) {
		this.goods = goods;
		this.count = count;
		this.whFrom = whFrom;
		this.whTo = whTo;
		id = whFrom.getCode() + goods.getCode() + whTo.getCode() + (int) count;
	}

	public long getId() {
		return id;
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
		sb.append(count);
		sb.append(' ');
		sb.append(whTo.getDescr());
		sb.append(' ');
		sb.append(whFrom.getDescr());
		return sb.toString();
	}
}
