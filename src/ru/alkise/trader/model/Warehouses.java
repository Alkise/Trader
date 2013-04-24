package ru.alkise.trader.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum Warehouses {
	INSTANCE;
	
	private Map<String, Warehouse> warehouses;
	
	{
		warehouses = new LinkedHashMap<String, Warehouse>();
	}
	
	public void addWarehouse(Warehouse warehouse) {
		warehouses.put(warehouse.getId(), warehouse);
	}
	
	public Warehouse getWarehouseById(String id) {
		return warehouses.get(id);
	}
	
	public Collection<Warehouse> getWarehousesList() {
		return warehouses.values();
	}
	
	public Map<String, Warehouse> getWarehouses() {
		return warehouses;
	}
	
	public String printWarehouses() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Warehouse> entry : warehouses.entrySet()) {
			sb.append(entry.getValue().toString());
			sb.append('\n');
		}
		return sb.toString();
	}
}
