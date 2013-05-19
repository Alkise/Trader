package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.Client;
import ru.alkise.trader.model.ClientIntf;
import ru.alkise.trader.model.ClientType;

public abstract class ClientFactory {
	public static ClientIntf createClient(int code, String descr,
			String fullName, ClientType type) {
		return new Client(code, descr, fullName, type);
	}

	public static ClientIntf createClient(int code, String descr,
			String fullName) {
		return new Client(code, descr, fullName);
	}
}
