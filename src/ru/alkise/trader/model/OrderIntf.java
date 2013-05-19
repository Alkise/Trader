package ru.alkise.trader.model;

import java.io.Serializable;
import java.util.List;

public interface OrderIntf extends Serializable {
	// ORDER TYPE ID
	public static final String ORDER_TYPE = "OrderDocumentType";

	// ORGANIZATION ID
	public static final String ORDER_ORGANIZATION = "OrderOrganization";

	// MANAGER ID
	public static final String ORDER_MANAGER = "OrderManager";

	// CLIENT ID
	public static final String ORDER_CLIENT = "OrderClient";

	// POSITIONS
	public static final String ORDER_POSITIONS = "OrderPositions";

	// TABLE NAME
	public static final String TABLE_NAME = "Order";

	DocumentType getOrderDocumentType();

	void setOrderDocumentType(DocumentType orderType);

	void setOrderOrganization(OrganizationIntf organization);

	OrganizationIntf getOrderOrganization();

	void setOrderManager(ManagerIntf manager);

	ManagerIntf getOrderManager();

	void setOrderClient(ClientIntf client);

	ClientIntf getOrderClient();

	List<PositionIntf> getOrderPositions();

	void addPosition(PositionIntf position);

	boolean checkOrder();

	String displayOrder();

	boolean checkNewPosition(PositionIntf position);

}
