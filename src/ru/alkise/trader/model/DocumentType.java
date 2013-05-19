package ru.alkise.trader.model;

import android.util.SparseArray;

public enum DocumentType {
	DEMAND(0, "Заявка"), CONSIGNMENT_NOTE(1, "Накладная");

	// UNIQUE ID
	public static final String DOCUMENT_TYPE_CODE = "DocumentTypeCode";

	// ORDER TYPE DESCRIPTION
	public static final String DOCUMENT_TYPE_DESCRIPTION = "DocumentTypeDescription";

	// TABLE NAME
	public static final String TABLE_NAME = "DocumentType";

	private static SparseArray<DocumentType> codeToTypeMapping;
	
	public static DocumentType getTypeByCode(int code) {
		if (codeToTypeMapping == null) {
			initMap();
		}

		return codeToTypeMapping.get(code);
	}
	
	private static void initMap() {
		codeToTypeMapping = new SparseArray<DocumentType>();
		for (DocumentType type : values()) {
			codeToTypeMapping.append(type.getDocumentTypeCode(), type);
		}
	}

	private int code;
	private String name;

	private DocumentType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getDocumentTypeCode() {
		return code;
	}

	public String getDocumentTypeDescription() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
