package ru.alkise.trader.model;

public class Position
{
	private String code;
	private String name;
	private String quantity;
	private String warehouseCode;
	private String warehouseName;
	private String fromWarehouseCode;
	private String fromWarehouseName;

	public void setFromWarehouseName(String fromWarehouseName) {
		this.fromWarehouseName = fromWarehouseName;
	}

	public String getFromWarehouseName() {
		return fromWarehouseName;
	}

	public void setFromWarehouseCode(String fromWarehouseCode) {
		this.fromWarehouseCode = fromWarehouseCode;
	}

	public String getFromWarehouseCode() {
		return fromWarehouseCode;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantity() {
		return quantity;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setName(String name) {
		this.name = name;
	} 
	
	public String getName() {
		return name;
	}
	
	
}
