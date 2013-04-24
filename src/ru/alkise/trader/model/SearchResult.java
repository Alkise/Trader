package ru.alkise.trader.model;

public class SearchResult {
	Warehouse wh;
	double count;

	public SearchResult(Warehouse wh, double count) {
		this.wh = wh;
		this.count = count;
	}

	public Warehouse getWarehouse() {
		return wh;
	}

	public void setWarehouse(Warehouse wh) {
		this.wh = wh;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return wh.getDescr() + " " + count;
	}

}
