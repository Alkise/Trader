package ru.alkise.trader.model;


public class Position implements PositionIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GoodsIntf goods;
	private double count;
	private WarehouseIntf whFrom;
	private WarehouseIntf whTo;

	public Position(GoodsIntf goods, double count, WarehouseIntf whFrom, WarehouseIntf whTo) {
		this.goods = goods;
		this.count = count;
		this.whFrom = whFrom;
		this.whTo = whTo;
	}
	
	@Override
	public GoodsIntf getPositionGoods() {
		return goods;
	}

	@Override
	public void setPositionGoods(GoodsIntf goods) {
		this.goods = goods;
	}

	@Override
	public double getPositionCount() {
		return count;
	}

	@Override
	public void setPositionCount(double count) {
		this.count = count;
	}

	@Override
	public WarehouseIntf getPositionFromWarehouse() {
		return whFrom;
	}

	@Override
	public void setPositionFromWarehouse(WarehouseIntf whFrom) {
		this.whFrom = whFrom;
	}

	@Override
	public WarehouseIntf getPositionToWarehouse() {
		return whTo;
	}

	@Override
	public void setPositionToWarehouse(WarehouseIntf whTo) {
		this.whTo = whTo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(whFrom.getWarehouseName());
		sb.append(' ');
		sb.append(count);
		return sb.toString();
	}
}
