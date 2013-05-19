package ru.alkise.trader.model;

import java.io.Serializable;

public interface OrganizationIntf extends Serializable {
	// UNIQUE ID
	public static final String ORGANIZATION_CODE = "OrganizationCode";

	// ORGANIZATION NAME
	public static final String ORGANIZATION_NAME = "OrganizationName";

	// TABLE NAME
	public static final String TABLE_NAME = "Organization";

	int getOrganizationCode();

	void setOrganizationCode(int code);

	String getOrganizationName();

	void setOrganizationName(String descr);
}
