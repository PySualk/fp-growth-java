package org.sualk.fpgrowth;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FrequentPattern {

	private List<String> items = new ArrayList<>();

	private Integer supportCount;
	private Double support;
	private static final String DELIMITER = ",";

	public FrequentPattern(String items, Integer supportCount, Double support) {
		if (items.contains(",")) {
			StringTokenizer tokenizer = new StringTokenizer(items, DELIMITER);
			while (tokenizer.hasMoreTokens())
				this.items.add(tokenizer.nextToken());
		} else {
			this.items.add(items);
		}

		this.supportCount = supportCount;
		this.support = support;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public Integer getSupportCount() {
		return supportCount;
	}

	public void setSupportCount(Integer support) {
		this.supportCount = support;
	}

	public Double getSupport() {
		return support;
	}

	public void setSupport(Double support) {
		this.support = support;
	}

	public String toString() {
		return "FrequentPattern[" + items + ":" + supportCount + "]";
	}

}
