package ru.alkise.trader.model;

public class Client {
	private String id;
	private String code;
	private String descr;
	private String fullName;
	private ClientType type;

	public Client(String id, String code, String descr, String fullName,
			ClientType type) {
		this.id = id;
		this.code = code;
		this.descr = descr.trim();
		this.fullName = fullName.trim();
		this.type = type;
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
	public String toString() {
		return fullName.trim();
	}
}
