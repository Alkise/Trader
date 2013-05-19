package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.Goods;
import ru.alkise.trader.model.GoodsIntf;

public abstract class GoodsFactory {
	public static GoodsIntf createGoods(int code, String descr, double count) {
		return new Goods(code, descr, count);
	}
}
