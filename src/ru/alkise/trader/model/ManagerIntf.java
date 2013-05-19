package ru.alkise.trader.model;

import java.io.Serializable;

public interface ManagerIntf extends Serializable {
	// UNIUQE ID
	public static final String MANAGER_CODE = "ManagerCode";

	// MANAGER NAME
	public static final String MANAGER_NAME = "ManagerName";

	// TABLE_NAME
	public static final String TABLE_NAME = "Manager";

	int getManagerCode();

	void setManagerCode(int code);

	String getManagerName();

	void setManagerName(String descr);
}
