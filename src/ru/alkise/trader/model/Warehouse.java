package ru.alkise.trader.model;

import java.io.Serializable;

public class Warehouse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private int code;
	private String descr;

	public Warehouse(String id, int code, String descr) {
		this.id = id;
		this.code = code;
		this.descr = descr.trim();
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
