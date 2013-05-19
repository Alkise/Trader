package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.Manager;
import ru.alkise.trader.model.ManagerIntf;

public abstract class ManagerFactory {
	public static ManagerIntf createManager(int code, String descr) {
		return new Manager(code, descr);
	}
}
