package ru.alkise.trader.model;

import java.io.Serializable;

public interface ClientIntf extends Serializable {
	// UNIQUE ID
	public static final String CLIENT_CODE = "ClientCode";

	// CLIENT SHORT NAME
	public static final String CLIENT_SHORT_NAME = "ClientShortName";

	// CLIENT FULL NAME
	public static final String CLIENT_FULL_NAME = "ClientFullName";

	// CLIENT TYPE
	public static final String CLIENT_TYPE_CODE = "ClientType";

	// TABLE NAME
	public static final String TABLE_NAME = "Client";
	
	int getClientCode();
	void setClientCode(int code);
	String getClientShortName();
	void setClientShortName(String descr);
	String getClientFullName();
	void setClientFullName(String fullName);
	ClientType getClientType();
	void setClientType(ClientType type);
}
