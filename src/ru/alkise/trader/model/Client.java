package ru.alkise.trader.model;

import java.io.Serializable;

public class Client implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//UNIQUE ID
	public static final String CODE = "client_code";
	
	//CLIENT SHORT NAME
	public static final String SHORT_NAME = "client_short_name";
	
	//CLIENT FULL NAME
	public static final String FULL_NAME = "client_full_name";
	
	//CLIENT TYPE
	public static final String TYPE_CODE = "client_type_id";
	
	//TABLE NAME
	public static final String TABLE_NAME = "client_table";
	
	private int code;
	private String descr;
	private String fullName;
	private ClientType type;

	public Client(int code, String descr, String fullName,
			ClientType type) {

		this.code = code;
		this.descr = descr.trim();
		this.fullName = fullName.trim();
		this.type = type;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName.trim();
	}

	public ClientType getType() {
		return type;
	}

	public void setType(ClientType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Client) {
			return ((Client) o).getCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return fullName.trim();
	}
}
