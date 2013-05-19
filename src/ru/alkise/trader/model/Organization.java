package ru.alkise.trader.model;


public class Organization implements OrganizationIntf{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int organizationCode;
	private String organizationName;

	public Organization(int code, String descr) {
		this.organizationCode = code;
		this.organizationName = descr.trim();
	}

	@Override
	public int getOrganizationCode() {
		return organizationCode;
	}

	@Override
	public void setOrganizationCode(int code) {
		this.organizationCode = code;
	}

	@Override
	public String getOrganizationName() {
		return organizationName;
	}

	@Override
	public void setOrganizationName(String descr) {
		this.organizationName = descr.trim();
	}

	@Override
	public int hashCode() {
		return organizationCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof OrganizationIntf) {
			return ((OrganizationIntf) o).getOrganizationCode() == organizationCode;
		}
		return false;
	}

	@Override
	public String toString() {
		return organizationName;
	}
}
