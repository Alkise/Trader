package ru.alkise.trader.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public enum Warehouses {
	INSTANCE;
	
	private Map<Integer, WarehouseIntf> warehouses;
	
	{
		warehouses = new LinkedHashMap<Integer, WarehouseIntf>();
	}
	
	public void addWarehouse(WarehouseIntf warehouse) {
		warehouses.put(warehouse.getWarehouseCode(), warehouse);
	}
	
	public WarehouseIntf getWarehouseByCode(int code) {
		return warehouses.get(code);
	}
	
	public Collection<WarehouseIntf> getWarehousesList() {
		return warehouses.values();
	}
	
	public Map<Integer, WarehouseIntf> getWarehouses() {
		return warehouses;
	}
	
	public String printWarehouses() {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, WarehouseIntf> entry : warehouses.entrySet()) {
			sb.append(entry.getValue().toString());
			sb.append('\n');
		}
		return sb.toString();
	}
}
