package ru.alkise.trader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ORDER TYPE ID
	public static final String ORDER_TYPE_CODE = "order_document_type_code";
	
	//ORGANIZATION ID
	public static final String ORGANIZATION_CODE = "order_organization_code";
	
	//MANAGER ID
	public static final String MANAGER_CODE = "order_manager_code";
	
	//CLIENT ID
	public static final String CLIENT_CODE = "order_client_code";
	
	//TABLE NAME
	public static final String TABLE_NAME = "order";
	
	private DocumentType orderType;
	private Organization organization;
	private Manager manager;
	private Client client;
	private List<Position> positions;

	public Order() {
		positions = new ArrayList<Position>();
		orderType = DocumentType.CONSIGNMENT_NOTE;
	}
	
	public DocumentType getOrderType() {
		return orderType;
	}

	public void setOrderType(DocumentType orderType) {
		this.orderType = orderType;
	}
	
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public Manager getManager() {
		return manager;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void addPosition(Position position) {
		positions.add(position);
	}

	public boolean checkOrder() {
		if (client != null && positions.size() > 0) {
			return true;
		}
		return false;
	}

	public String displayOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append(orderType.getCode());
		sb.append('\n');
		sb.append(organization.getDescr());
		sb.append('\n');
		sb.append(manager.getDescr());
		sb.append('\n');
		sb.append((client != null) ? client.getDescr() : "Клиент не выбран");
		for (Position position : positions) {
			sb.append('\n');
			sb.append(position.getGoods().getCode());
			sb.append('\n');
			sb.append(position.getCount());
			sb.append('\n');
			sb.append(position.getWhTo());
			sb.append('\n');
			sb.append(position.getWhFrom());
		}
		return sb.toString();
	}

}
