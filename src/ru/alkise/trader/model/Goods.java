package ru.alkise.trader.model;

import java.io.Serializable;

public class Goods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private int code;
	private String descr;
	private double count;

	public Goods(int code, String descr, double count) {
		this(code,descr);
		this.count = count;
	}
	
	public Goods(int code, String descr) {

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

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Goods) {
			return ((Goods) o).getCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return code + " " + descr;
	}
}
