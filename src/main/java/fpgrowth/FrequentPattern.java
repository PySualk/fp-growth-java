package fpgrowth;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class FrequentPattern {

	private Set<String> items = new HashSet<>();

	private Integer supportCount;
	private Double support;
	private static final String DELIMITER = ",";

	public FrequentPattern(String items, Integer supportCount, Double support) {
		super();

		if (items.contains(",")) {
			StringTokenizer tokenizer = new StringTokenizer(items, DELIMITER);
			while (tokenizer.hasMoreTokens())
				this.items.add(tokenizer.nextToken());
		} else {
			this.items.add(items);
		}

		// Collections.sort(this.items);

		this.supportCount = supportCount;
		this.support = support;
	}

	// public FrequentPattern(List<String> items, Integer supportCount, Double
	// support) {
	public FrequentPattern(Set<String> items, Integer supportCount, Double support) {

		this.items = items;
		this.supportCount = supportCount;
		this.support = support;
	}

	// public List<String> getItems() {
	public Set<String> getItems() {

		return items;
	}

	// public void setItems(List<String> items) {
	public void setItems(Set<String> items) {

		this.items = items;
		// Collections.sort(this.items);
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
