package org.sualk.fpgrowth;

public interface DataSource {

	Transaction next();

	boolean hasNext();

	void reset();

}
