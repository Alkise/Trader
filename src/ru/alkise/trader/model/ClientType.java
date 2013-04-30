package ru.alkise.trader.model;


public enum ClientType {
	_2F2("   2F2   ", "Физ.лицо"), _2F3("   2F3   ", "Юр.лицо"), _2F6(
			"   2F6   ", "ИП");

	private String code;
	private String value;
	
	private ClientType(String code, String value) {
		this.code = code;
		this.value = value;
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
