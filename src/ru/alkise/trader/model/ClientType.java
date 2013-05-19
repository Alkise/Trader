package ru.alkise.trader.model;

import java.util.HashMap;
import java.util.Map;

public enum ClientType {
	_2F2("2F2", "Физ.лицо"), _2F3("2F3", "Юр.лицо"), _2F6("2F6", "ИП");

	// CLIENT TYPE CODE IN MSSQL BASE
	public static final String CLIENT_TYPE_CODE = "ClientTypeCode";

	// CLIENT TYPE DESCRIPTION
	public static final String CLIENT_TYPE_DESCRIPTION = "ClientTypeDescription";

	// TABLE NAME
	public static final String TABLE_NAME = "ClientType";

	private String code;
	private String value;
	private static Map<String, ClientType> idToClientTypeMap;

	private ClientType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	public static ClientType getClientTypeById(String id) {
		if (idToClientTypeMap == null) {
			initMap();
		}
		return idToClientTypeMap.get(id.trim());
	}

	private static void initMap() {
		idToClientTypeMap = new HashMap<String, ClientType>();
		for (ClientType clientType : values()) {
			idToClientTypeMap.put(clientType.code, clientType);
		}
	}

	public String getClientTypeCode() {
		return code;
	}

	public String getClientTypeDescription() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
