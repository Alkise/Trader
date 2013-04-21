package ru.alkise.trader.model;

public class Organization {
	private String id;
	private String code;
	private String descr;

	public Organization(String id , String code ,String descr) {
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
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
		return descr.trim();
	}
}
