package ru.alkise.trader.model.factory;

import ru.alkise.trader.model.Organization;
import ru.alkise.trader.model.OrganizationIntf;

public abstract class OrganizationFactory {
	public static OrganizationIntf createOrganization(int code, String descr) {
		return new Organization(code, descr);
	}
}
