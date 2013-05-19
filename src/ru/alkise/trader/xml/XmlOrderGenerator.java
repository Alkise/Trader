package ru.alkise.trader.xml;

import java.util.List;

import ru.alkise.trader.model.ClientIntf;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.ManagerIntf;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.OrganizationIntf;
import ru.alkise.trader.model.PositionIntf;

public class XmlOrderGenerator {
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

	public static String getXmlText(OrderIntf order) {
		DocumentType orderType = order.getOrderDocumentType();
		OrganizationIntf organization = order.getOrderOrganization();
		ManagerIntf manager = order.getOrderManager();
		ClientIntf client = order.getOrderClient();
		List<PositionIntf> positions = order.getOrderPositions();
		
		StringBuilder sb = new StringBuilder(XML);
		sb.append('\n');
//		<ORDER>
		sb.append(startTag(TAG_LEVEL_0, ORDER));
//			<TYPE>
		sb.append(fieldWithValue(TAG_LEVEL_1, TYPE, String.valueOf(orderType.getDocumentTypeCode())));
//			</TYPE>
//			<ORGANIZATION>
		sb.append(startTag(TAG_LEVEL_1, ORGANIZATION));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2,CODE, String.valueOf(organization.getOrganizationCode())));
//				</CODE>
		sb.append(endTag(TAG_LEVEL_1, ORGANIZATION));
//			</ORGANIZATION>
//			<MANAGER>
		sb.append(startTag(TAG_LEVEL_1,MANAGER));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2, CODE, String.valueOf(manager.getManagerCode())));
//				</CODE>
//			</MANAGER>
		sb.append(endTag(TAG_LEVEL_1, MANAGER));
//			<CLIENT>
		sb.append(startTag(TAG_LEVEL_1,CLIENT));
//				<CODE>
		sb.append(fieldWithValue(TAG_LEVEL_2,CODE, String.valueOf(client.getClientCode())));
//				</CODE>
//				<TYPE>
		sb.append(fieldWithValue(TAG_LEVEL_2, TYPE, (client.getClientType() != null ? client.getClientType().getClientTypeCode() : "")));
//				</TYPE>
//				<SHORT_NAME>
		sb.append(fieldWithValue(TAG_LEVEL_2, SHORT_NAME, client.getClientShortName()));
//				</SHORT_NAME>
//				<FULL_NAME>
		sb.append(fieldWithValue(TAG_LEVEL_2, FULL_NAME, client.getClientFullName()));
//				</FULL_NAME>
//			</CLIENT>
		sb.append(endTag(TAG_LEVEL_1, CLIENT));
//			<POSITIONS>
		sb.append(startTag(TAG_LEVEL_1, POSITIONS));
		for(PositionIntf position : positions) {
//				<POSITION>
			sb.append(startTag(TAG_LEVEL_2, POSITION));
//					<CODE>
			sb.append(fieldWithValue(TAG_LEVEL_3, CODE, String.valueOf(position.getPositionGoods().getGoodsCode())));
//					</CODE>
//					<COUNT>
			sb.append(fieldWithValue(TAG_LEVEL_3, COUNT, String.valueOf(position.getPositionCount())));
//					</COUNT>
//					<WAREHOUSE_TO>
			sb.append(fieldWithValue(TAG_LEVEL_3, WAREHOUSE_TO, String.valueOf(position.getPositionToWarehouse().getWarehouseCode())));
//					</WAREHOUSE_TO>
//					<WAREHOUSE_FROM>
			sb.append(fieldWithValue(TAG_LEVEL_3, WAREHOUSE_FROM, String.valueOf(position.getPositionFromWarehouse().getWarehouseCode())));
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
}
