package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.Warehouse;
import ru.alkise.trader.model.WarehouseIntf;

public abstract class WarehouseFactory {
	public static WarehouseIntf createWarehouse(int code, String descr) {
		return new Warehouse(code, descr);
	}
}
