package ru.alkise.trader.model;

public class Organization {
	private int code;
	private String descr;

	public Organization(int code, String descr) {
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
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Organization) {
			return ((Organization) o).getCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return descr;
	}
}
