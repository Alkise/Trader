package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.GoodsIntf;
import ru.alkise.trader.model.Position;
import ru.alkise.trader.model.PositionIntf;
import ru.alkise.trader.model.WarehouseIntf;

public abstract class PositionFactory {
	public static PositionIntf createPosition(GoodsIntf goods, double count, WarehouseIntf whFrom, WarehouseIntf whTo) {
		return new Position(goods, count, whFrom, whTo);
	}
}
