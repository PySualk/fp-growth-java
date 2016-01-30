package org.sualk.fpgrowth;

import java.util.ArrayList;
import java.util.List;

public class FPtree {

	private String item;
	private List<FPtree> children;
	private Integer count = 1;
	private FPtree parent;
	private Boolean root = false;

	// Links to the node with same item name or null
	private FPtree next = null;

	public FPtree(String item, FPtree parent) {
		this.item = item;
		this.parent = parent;
		this.children = new ArrayList<>();
	}

	public void addChild(FPtree child) {
		this.children.add(child);
	}

	public FPtree getParent() {
		return parent;
	}

	public void setParent(FPtree parent) {
		this.parent = parent;
	}

	public List<FPtree> getChildren() {
		return this.children;
	}

	public String getItem() {
		return this.item;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void incrementCount() {
		this.count++;
	}

	public FPtree getNext() {
		return this.next;
	}

	public void setNext(FPtree next) {
		this.next = next;
	}

	public void setRoot(Boolean root) {
		this.root = root;
	}

	public Boolean isRoot() {
		return this.root;
	}

	public String toString() {
		return "FPtree[" + item + ":" + count + ", Children: " + children + "]";
	}

}
