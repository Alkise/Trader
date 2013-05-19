package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.List;

public class Order implements OrderIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DocumentType orderType;
	private OrganizationIntf organization;
	private ManagerIntf manager;
	private ClientIntf client;
	private static List<PositionIntf> positions;

	static {
		positions = new ArrayList<PositionIntf>();
	}
	
	public Order() {
		orderType = DocumentType.CONSIGNMENT_NOTE;
	}

	public Order(DocumentType orderType, OrganizationIntf organization,
			ManagerIntf manager, ClientIntf client, List<PositionIntf> positions) {
		this.orderType = orderType;
		this.organization = organization;
		this.manager = manager;
		this.client = client;
		Order.positions = positions;
	}

	@Override
	public DocumentType getOrderDocumentType() {
		return orderType;
	}

	@Override
	public void setOrderDocumentType(DocumentType orderType) {
		this.orderType = orderType;
	}

	@Override
	public void setOrderOrganization(OrganizationIntf organization) {
		this.organization = organization;
	}

	@Override
	public OrganizationIntf getOrderOrganization() {
		return organization;
	}

	@Override
	public void setOrderManager(ManagerIntf manager) {
		this.manager = manager;
	}

	@Override
	public ManagerIntf getOrderManager() {
		return manager;
	}

	@Override
	public void setOrderClient(ClientIntf client) {
		this.client = client;
	}

	@Override
	public ClientIntf getOrderClient() {
		return client;
	}

	@Override
	public List<PositionIntf> getOrderPositions() {
		return positions;
	}

	@Override
	public void addPosition(PositionIntf position) {
		positions.add(position);
	}

	@Override
	public boolean checkOrder() {
		if (client != null && positions.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String displayOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append(orderType.getDocumentTypeCode());
		sb.append('\n');
		sb.append(organization.getOrganizationName());
		sb.append('\n');
		sb.append(manager.getManagerName());
		sb.append('\n');
		sb.append((client != null) ? client.getClientShortName() : "Клиент не выбран");
		for (PositionIntf position : positions) {
			sb.append('\n');
			sb.append(position.getPositionGoods().getGoodsCode());
			sb.append('\n');
			sb.append(position.getPositionCount());
			sb.append('\n');
			sb.append(position.getPositionToWarehouse());
			sb.append('\n');
			sb.append(position.getPositionFromWarehouse());
		}
		return sb.toString();
	}
}
