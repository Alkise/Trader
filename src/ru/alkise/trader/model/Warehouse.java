package ru.alkise.trader.model;

public class Warehouse {
	private String id;
	private int code;
	private String descr;
	
	public Warehouse(String id, int code, String descr) {
		this.id = id;
		this.code = code;
		this.descr = descr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	@Override
	public String toString() {
		return descr;
	}
}
