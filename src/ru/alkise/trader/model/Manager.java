package ru.alkise.trader.model;

import java.io.Serializable;

public class Manager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//UNIUQE ID
	public static final String CODE = "manager_code";
	
	//MANAGER NAME
	public static final String NAME = "manager_name";
	
	//TABLE_NAME
	public static final String TABLE_NAME = "manager";

	private int code;
	private String descr;

	public Manager(int code, String descr) {
		this.code = code;
		this.descr = descr.trim();
	}

	public int getCode() {
		return code;
	}

	public String getStringCode() {
		return String.valueOf(code);
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
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Manager) {
			return ((Manager) o).getCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return descr;
	}

}
