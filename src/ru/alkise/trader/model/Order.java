package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum Order {
	INSTANCE;

	private Organization organization;
	private Manager manager;
	private Client client;
	private List<Position> positions;

	{
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

	public void addPositions(Collection<Position> positions) {
		this.positions.addAll(positions);
	}

	public void removePosition(Position position) {
		positions.remove(position);
	}

	public void removePositions(Collection<Position> positions) {
		this.positions.removeAll(positions);
	}

	public void clearPositions() {
		positions.clear();
	}

	public String displayOrder() {
		StringBuilder sb = new StringBuilder(organization.getDescr());
		sb.append('\n');
		sb.append(manager.getDescr());
		sb.append('\n');
		sb.append((client != null) ? client.getDescr() : "Клиент не выбран");
		for (Position position : positions) {
			sb.append('\n');
			sb.append(position.toString());
		}
		return sb.toString();
	}
}
