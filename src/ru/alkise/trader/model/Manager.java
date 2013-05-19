package ru.alkise.trader.model;


public class Manager implements ManagerIntf {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String descr;

	public Manager(int code, String descr) {
		this.code = code;
		this.descr = descr.trim();
	}
	
	@Override
	public int getManagerCode() {
		return code;
	}

	@Override
	public void setManagerCode(int code) {
		this.code = code;
	}

	@Override
	public String getManagerName() {
		return descr;
	}
	
	@Override
	public void setManagerName(String descr) {
		this.descr = descr.trim();
	}

	@Override
	public int hashCode() {
		return code;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ManagerIntf) {
			return ((ManagerIntf) o).getManagerCode() == code;
		}
		return false;
	}

	@Override
	public String toString() {
		return descr;
	}

}
