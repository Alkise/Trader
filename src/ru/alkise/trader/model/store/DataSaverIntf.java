package ru.alkise.trader.model.store;

import ru.alkise.trader.model.OrderIntf;

public interface DataSaverIntf {

	void store(OrderIntf order);

	OrderIntf load();

}
