package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.List;

public enum Order {
	INSTANCE;

	public static final int TYPE_DEMAND = 0;
	public static final int TYPE_CONSIGNMENT_NOTE = 1;
	public static final String ORDER = "ORDER";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String MANAGER = "MANAGER";
	public static final String CLIENT = "CLIENT";
	public static final String POSITIONS = "POSITITONS";
	public static final String POSITION = "POSITION";
	public static final String CODE = "CODE";
	public static final String TYPE = "TYPE";
	public static final String SHORT_NAME = "SHORT_NAME";
	public static final String FULL_NAME = "FULL_NAME";
	public static final String COUNT = "COUNT";
	public static final String WAREHOUSE_FROM = "WAREHOUSE_FROM";
	public static final String WAREHOUSE_TO = "WAREHOUSE_TO";
	private static final String XML = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";
	private static final String START_OPEN_TAG = "<";
	private static final String END_TAG = ">";
	private static final String START_CLOSE_TAG = "</";
	private static final String TAG_LEVEL_0 = "";
	private static final String TAG_LEVEL_1 = "\t";
	private static final String TAG_LEVEL_2 = "\t\t";
	private static final String TAG_LEVEL_3 = "\t\t\t";
	
	private static OrderType orderType;
	private static Organization organization;
	private static Manager manager;
	private static Client client;
	private static List<Position> positions;

	static {
		positions = new ArrayList<Position>();
		orderType = OrderType.CONSIGNMENT_NOTE;
	}
	
	public static void setOrganization(Organization organization) {
		Order.organization = organization;
	}

	public static Organization getOrganization() {
		return organization;
	}

	public static void setManager(Manager manager) {
		Order.manager = manager;
	}

	public static Manager getManager() {
		return manager;
	}

	public static void setClient(Client client) {
		Order.client = client;
	}

	public static Client getClient() {
		return client;
	}

	public static List<Position> getPositions() {
		return positions;
	}

	public static void addPosition(Position position) {
		positions.add(position);
	}

	public static boolean checkOrder() {
		if (client != null && positions.size() > 0) {
			return true;
		}
		return false;
	}

	public static String displayOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append(orderType.getCode());
		sb.append('\n');
		sb.append(organization.getDescr());
		sb.append('\n');
		sb.append(manager.getDescr());
		sb.append('\n');
		sb.append((client != null) ? client.getDescr() : "Клиент не выбран");
		for (Position position : positions) {
			sb.append('\n');
			sb.append(position.getGoods().getCode());
			sb.append('\n');
			sb.append(position.getCount());
			sb.append('\n');
			sb.append(position.getWhTo());
			sb.append('\n');
			sb.append(position.getWhFrom());
		}
		return sb.toString();
	}
	
	private static String startTagWithoutNewLine(String tagLevel, String tag) {
		return tagLevel + START_OPEN_TAG + tag + END_TAG;
	}
	
	private static String startTag(String tagLevel, String tag) {
		return startTagWithoutNewLine(tagLevel, tag) + '\n';
	}

	private static String endTag(String tagLevel, String tag) {
		return tagLevel + START_CLOSE_TAG + tag + END_TAG + '\n';
	}
	
	private static String fieldWithValue(String tagLevel, String field, String value) {
		StringBuilder sb = new StringBuilder(startTagWithoutNewLine(tagLevel, field));
		sb.append(value);
		sb.append(endTag(TAG_LEVEL_0, field));
		return sb.toString();
	}

	public static String getXmlText() {
		StringBuilder sb = new StringBuilder(XML);
		sb.append('\n');
//		<ORDER>
		sb.append(startTag(TAG_LEVEL_0, ORDER));
//			<TYPE>
		sb.append(fieldWithValue(TAG_LEVEL_1, TYPE, String.valueOf(Order.getOrderType().getCode())));
//			</TYPE>
//			<ORGANIZATION>
		sb.append(startTag(TAG_LEVEL_1, ORGANIZATION));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2,CODE, String.valueOf(organization.getCode())));
//				</CODE>
		sb.append(endTag(TAG_LEVEL_1, ORGANIZATION));
//			</ORGANIZATION>
//			<MANAGER>
		sb.append(startTag(TAG_LEVEL_1,MANAGER));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2, CODE, String.valueOf(manager.getCode())));
//				</CODE>
//			</MANAGER>
		sb.append(endTag(TAG_LEVEL_1, MANAGER));
//			<CLIENT>
		sb.append(startTag(TAG_LEVEL_1,CLIENT));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2,CODE, String.valueOf(client.getCode())));
//				</CODE>
//				<TYPE>
		sb.append(fieldWithValue(TAG_LEVEL_2, TYPE, (client.getType() != null ? client.getType().getCode() : "")));
//				</TYPE>
//				<SHORT_NAME>
		sb.append(fieldWithValue(TAG_LEVEL_2, SHORT_NAME, client.getDescr()));
//				</SHORT_NAME>
//				<FULL_NAME>
		sb.append(fieldWithValue(TAG_LEVEL_2, FULL_NAME, client.getFullName()));
//				</FULL_NAME>
//			</CLIENT>
		sb.append(endTag(TAG_LEVEL_1, CLIENT));
//			<POSITIONS>
		sb.append(startTag(TAG_LEVEL_1, POSITIONS));
		for(Position position : positions) {
//				<POSITION>
			sb.append(startTag(TAG_LEVEL_2, POSITION));
//					<CODE>
			sb.append(fieldWithValue(TAG_LEVEL_3, CODE, String.valueOf(position.getGoods().getCode())));
//					</CODE>
//					<COUNT>
			sb.append(fieldWithValue(TAG_LEVEL_3, COUNT, String.valueOf(position.getCount())));
//					</COUNT>
//					<WAREHOUSE_TO>
			sb.append(fieldWithValue(TAG_LEVEL_3, WAREHOUSE_TO, String.valueOf(position.getWhTo().getCode())));
//					</WAREHOUSE_TO>
//					<WAREHOUSE_FROM>
			sb.append(fieldWithValue(TAG_LEVEL_3, WAREHOUSE_FROM, String.valueOf(position.getWhFrom().getCode())));
//					</WAREHOUSE_FROM>
//				</POSITION>
			sb.append(endTag(TAG_LEVEL_2, POSITION));
		}
//			</POSITIONS>
		sb.append(endTag(TAG_LEVEL_1, POSITIONS));
//		</ORDER>
		sb.append(endTag(TAG_LEVEL_0, ORDER));
		return sb.toString();
	}

	public static OrderType getOrderType() {
		return orderType;
	}

	public static void setOrderType(OrderType orderType) {
		Order.orderType = orderType;
	}

}
