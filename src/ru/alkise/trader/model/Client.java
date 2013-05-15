package ru.alkise.trader.model;

public class Client {
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
