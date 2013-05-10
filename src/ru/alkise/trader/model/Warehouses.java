package ru.alkise.trader.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum Warehouses {
	INSTANCE;
	
	private Map<Integer, Warehouse> warehouses;
	
	{
		warehouses = new LinkedHashMap<Integer, Warehouse>();
	}
	
	public void addWarehouse(Warehouse warehouse) {
		warehouses.put(warehouse.getCode(), warehouse);
	}
	
	public Warehouse getWarehouseByCode(int code) {
		return warehouses.get(code);
	}
	
	public Collection<Warehouse> getWarehousesList() {
		return warehouses.values();
	}
	
	public Map<Integer, Warehouse> getWarehouses() {
		return warehouses;
	}
	
	public String printWarehouses() {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, Warehouse> entry : warehouses.entrySet()) {
			sb.append(entry.getValue().toString());
			sb.append('\n');
		}
		return sb.toString();
	}
}
