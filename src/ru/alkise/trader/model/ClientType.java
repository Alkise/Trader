package ru.alkise.trader.model;

import java.util.HashMap;
import java.util.Map;


public enum ClientType {
	_2F2("   2F2   ", "Физ.лицо"), _2F3("   2F3   ", "Юр.лицо"), _2F6(
			"   2F6   ", "ИП");

	private String code;
	private String value;
	private Map<String, ClientType> idToClientTypeMap;
	
	private ClientType(String code, String value) {
		this.code = code;
		this.value = value;
	}
	
	public ClientType getClientTypeById(String id) {
		if (idToClientTypeMap == null) {
			initMap();
		}
		return idToClientTypeMap.get(id);
	}
	
	private void initMap() {
		idToClientTypeMap = new HashMap<String, ClientType>();
		for (ClientType clientType : values()) {
			idToClientTypeMap.put(clientType.code, clientType);
		}
	}
	

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
