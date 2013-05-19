package ru.alkise.trader.model;


public class Warehouse implements WarehouseIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String descr;

	public Warehouse(int code, String descr) {
		this.code = code;
		this.descr = descr.trim();
	}

	@Override
	public int getWarehouseCode() {
		return code;
	}

	@Override
	public void setWarehouseCode(int code) {
		this.code = code;
	}

	@Override
	public String getWarehouseName() {
		return descr;
	}

	@Override
	public void setWarehouseName(String descr) {
		this.descr = descr.trim();
	}

	@Override
	public String toString() {
		return descr;
	}
	
	@Override
	public int hashCode() {
		return getWarehouseCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WarehouseIntf) {
			return ((WarehouseIntf) o).getWarehouseCode() == code;
		}
		return false;
	}
}
