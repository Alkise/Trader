package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum Order {
	INSTANCE;

	private Organization organization;
	private Manager manager;
	private Client client;
	private static List<Position> positions;
	private static List<OrderObserver> observerList;

	static {
		positions = new ArrayList<Position>();
		observerList = new ArrayList<OrderObserver>();
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void addObserver(OrderObserver observer) {
		observerList.add(observer);
	}
	
	public void removeObserver(OrderObserver observer) {
		observerList.remove(observer);
	}
	
	public void removeObservers() {
		observerList.clear();
	}
	
	private void notifyObservers() {
		for (OrderObserver observer : observerList) {
			observer.update();
		}
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
		notifyObservers();
	}

	public void addPositions(Collection<Position> positions) {
		Order.positions.addAll(positions);
		notifyObservers();
	}

	public void removePosition(Position position) {
		Order.positions.remove(position);
		notifyObservers();
	}

	public void removePosition(int posNum) {
		Order.positions.remove(posNum);
		notifyObservers();
	}
	
	public void removePositions(Collection<Position> positions) {
		Order.positions.removeAll(positions);
		notifyObservers();
	}

	public void clearPositions() {
		positions.clear();
		notifyObservers();
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
