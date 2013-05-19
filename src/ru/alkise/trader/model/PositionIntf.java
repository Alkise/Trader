package ru.alkise.trader.model;

import java.io.Serializable;

public interface PositionIntf extends Serializable {
	
	//POSITION CODE
	public static final String POSITION_CODE = "PositionCode";
	
	// GOODS ID
	public static final String POSITION_GOODS = "PositionGoods";

	// CURRENT COUNT
	public static final String POSITION_COUNT = "PositionCount";

	// FROM WAREHOUSE ID
	public static final String POSITION_FROM_WAREHOUSE = "PositionFromWarehouse";

	// TO WAREHOUSE ID
	public static final String POSITION_TO_WAREHOUSE = "PositionToWarehouse";

	// TABLE NAME
	public static final String TABLE_NAME = "Position";

	GoodsIntf getPositionGoods();

	void setPositionGoods(GoodsIntf goods);

	double getPositionCount();

	void setPositionCount(double count);

	WarehouseIntf getPositionFromWarehouse();

	void setPositionFromWarehouse(WarehouseIntf whFrom);

	WarehouseIntf getPositionToWarehouse();

	void setPositionToWarehouse(WarehouseIntf whTo);

}
