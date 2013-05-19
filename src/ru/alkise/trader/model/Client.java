package ru.alkise.trader.model;


public class Client implements ClientIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String descr;
	private String fullName;
	private ClientType type;

	public Client(int code, String descr, String fullName) {
		this(code, descr, fullName, null);
	}
	
	public Client(int code, String descr, String fullName,
			ClientType type) {

		this.code = code;
		this.descr = descr.trim();
		this.fullName = fullName.trim();
		this.type = type;
	}

	@Override
	public int getClientCode() {
		return code;
	}
	
	@Override
	public void setClientCode(int code) {
		this.code = code;
	}
	
	@Override
	public String getClientShortName() {
		return descr;
	}

	@Override
	public void setClientShortName(String descr) {
		this.descr = descr.trim();
	}

	@Override
	public String getClientFullName() {
		return fullName;
	}

	@Override
	public void setClientFullName(String fullName) {
		this.fullName = fullName.trim();
	}

	@Override
	public ClientType getClientType() {
		return type;
	}

	@Override
	public void setClientType(ClientType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ClientIntf) {
			return ((ClientIntf) o).getClientCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return fullName;
	}
}
