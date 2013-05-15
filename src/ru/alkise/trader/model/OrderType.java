package ru.alkise.trader.model;


public enum OrderType {
	DEMAND(0, "Заявка"), CONSIGNMENT_NOTE(1, "Накладная");

	private int code;
	private String name;
	
	private OrderType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
