package ru.alkise.trader.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum SearchResults {
	INSTANCE;
	public static boolean IS_EMPTY = true;

	private Goods goods;
	private static Map<Warehouse, Double> remains;
	private static List<SearchResult> results;

	static {
		remains = new LinkedHashMap<Warehouse, Double>();
		results = new ArrayList<SearchResult>();
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
		if (goods != null) {
			IS_EMPTY = false;
		}
	}

	public Map<Warehouse, Double> getRemains() {
		return remains;
	}

	public void addRemain(Warehouse warehouse, Double remain) {
		remains.put(warehouse, remain);
	}

	public List<SearchResult> getResults() {
		if (!results.isEmpty()) {
			results.clear();
		}
		
		for (Map.Entry<Warehouse, Double> entry : remains.entrySet()) {
			results.add(new SearchResult(entry.getKey(), entry.getValue()));
		}
		return results;
	}

	public void clear() {
		goods = null;
		IS_EMPTY = true;
		remains.clear();
	}

	public String displayRemains() {
		StringBuilder sb = new StringBuilder(goods != null ? goods.toString()
				: "null");
		if (remains != null && remains.size() > 0) {
			for (Entry<Warehouse, Double> entry : remains.entrySet()) {
				sb.append('\n');
				sb.append(entry.getKey().toString());
				sb.append(' ');
				sb.append(entry.getValue().toString());
			}
		}
		return sb.toString();
	}
}
