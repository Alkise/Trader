package ru.alkise.trader.model;

import java.io.Serializable;
import java.util.List;

public class Demand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Organization organization;
	private Manager manager;
	private Client client;
	private List<Position> positions;
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
	private static final String END_EMPTY_TAG = "/>";

	public Demand(Organization organization, Manager manager, Client client,
			List<Position> positions) {
		this.organization = organization;
		this.manager = manager;
		this.client = client;
		this.positions = positions;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	
	private String startTag(String tag) {
		return START_OPEN_TAG + tag + END_TAG + '\n';
	}
	
	private String endTag(String tag) {
		return START_CLOSE_TAG + tag + END_TAG + '\n';
	}
	
	/*private String empltyTag(String tag) {
		return S
	}*/
	
	public String getXmlText() {
		StringBuilder sb = new StringBuilder(XML);
		sb.append('\n');
//		<ORDER>
		sb.append(START_OPEN_TAG);
		sb.append(ORDER);
		sb.append(END_TAG);
		sb.append('\n');
		
//		<ORGANIZATION>
		sb.append(START_OPEN_TAG);
		sb.append(ORGANIZATION);
//		<CODE>
//		</CODE>
//		</ORGANIZATION>
		
//		</ORDER>
		sb.append(START_CLOSE_TAG);
		sb.append(ORDER);
		sb.append(END_TAG);
		return sb.toString();
	}
}
