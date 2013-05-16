package ru.alkise.trader.model;

import java.io.Serializable;

public class Warehouse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//UNIQUE ID
	public static final String ID = "_id";
	
	//WAREHOUSE NAME
	public static final String NAME = "warehouse_name";
	
	//TABLE NAME
	public static final String TABLE_NAME = "warehouse_table";
	
	private int code;
	private String descr;

	public Warehouse(int code, String descr) {
		this.code = code;
		this.descr = descr.trim();
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
		this.descr = descr.trim();
	}

	@Override
	public String toString() {
		return descr;
	}
	
	@Override
	public int hashCode() {
		return getCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Warehouse) {
			return ((Warehouse) o).getCode() == code;
		}
		return false;
	}
}
