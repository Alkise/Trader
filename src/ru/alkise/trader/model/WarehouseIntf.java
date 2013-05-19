package ru.alkise.trader.model;

import java.io.Serializable;

public interface WarehouseIntf extends Serializable {
	// UNIQUE ID
	public static final String WAREHOUSE_CODE = "WarehouseCode";

	// WAREHOUSE NAME
	public static final String WAREHOUSE_NAME = "WarehouseName";

	// TABLE NAME
	public static final String TABLE_NAME = "Warehouse";

	int getWarehouseCode();

	void setWarehouseCode(int code);

	String getWarehouseName();

	void setWarehouseName(String descr);
}
