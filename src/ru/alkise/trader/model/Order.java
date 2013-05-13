package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.List;

public enum Order {
	INSTANCE;

	private Organization organization;
	private Manager manager;
	private Client client;
	private static List<Position> positions;

	static {
		positions = new ArrayList<Position>();
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
		StringBuilder sb = new StringBuilder(organization.getDescr());
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
