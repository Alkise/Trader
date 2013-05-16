package ru.alkise.trader.model;


public enum DocumentType {
	DEMAND(0, "Заявка"), CONSIGNMENT_NOTE(1, "Накладная");
	
	//UNIQUE ID
	public static final String CODE = "document_type_code";
	
	//ORDER TYPE DESCRIPTION
	public static final String DESCRIPTION = "document_type_description";
	
	//TABLE NAME
	public static final String TABLE_NAME = "document_type_table";

	private int code;
	private String name;
	
	private DocumentType(int code, String name) {
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
