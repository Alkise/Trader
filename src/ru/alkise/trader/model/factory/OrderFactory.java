package ru.alkise.trader.model.factory;

import java.util.List;

import ru.alkise.trader.model.ClientIntf;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.ManagerIntf;
import ru.alkise.trader.model.Order;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.OrganizationIntf;
import ru.alkise.trader.model.PositionIntf;

public abstract class OrderFactory {
	public static OrderIntf createOrder() {
		return new Order();
	}

	public static OrderIntf createOrder(DocumentType orderType,
			OrganizationIntf organization, ManagerIntf manager,
			ClientIntf client, List<PositionIntf> positions) {
		return new Order(orderType, organization, manager, client, positions);
	}
}
