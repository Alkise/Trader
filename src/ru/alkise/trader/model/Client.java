package ru.alkise.trader.model;

public class Client {
	private String id;
	private String code;
	private String descr;
	private String fullName;
	
	public Client(String id, String code, String descr, String fullName) {
		this.id = id;
		this.code = code;
		this.descr = descr;
		this.fullName = fullName;
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
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		return fullName.trim();
	}
}
