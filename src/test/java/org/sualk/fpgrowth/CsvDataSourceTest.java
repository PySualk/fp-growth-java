package org.sualk.fpgrowth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class CsvDataSourceTest {

	private File inputFile;

	@Before
	public void setup() {
		this.inputFile = new File("src/main/resources/exampleTransactions1.csv");
	}

	@Test
	public void readFileTest() {
		DataSource dataSource = new SimpleCsvDataSource(inputFile, ",");
		int transactionCounter = 0;
		while (dataSource.hasNext()) {
			Transaction t = dataSource.next();
			boolean hasItems = t.getItems().size() > 0;
			assertTrue(hasItems);
			transactionCounter++;
		}
		assertEquals(6, transactionCounter);
	}

	@Test
	public void callNextWhenNoLineLeftTest() {
		DataSource dataSource = new SimpleCsvDataSource(inputFile, ",");
		for (int i = 0; i < 6; i++) {
			dataSource.next();
		}
		assertEquals(null, dataSource.next());
	}
}
