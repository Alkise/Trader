package ru.alkise.trader.model;
import java.util.*;

public enum Order
{
	INSTANCE;
	
	private static Organization organization;
	private static Manager manager;
	private static Client client;
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
	
}
