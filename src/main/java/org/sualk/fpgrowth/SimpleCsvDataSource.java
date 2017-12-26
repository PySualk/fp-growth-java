package org.sualk.fpgrowth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SimpleCsvDataSource implements DataSource {

	private String delimiter;
	private String currentLine = "";
	private BufferedReader br;
	private File inputFile;

	public SimpleCsvDataSource(File inputFile, String delimiter) {
		this.inputFile = inputFile;
		this.delimiter = delimiter;
		init();
	}

	private void init() {
		try {
			br = new BufferedReader(new FileReader(inputFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		init();
	}

	@Override
	public Transaction next() {
		Transaction t = new Transaction();

		if (this.currentLine.length() == 0) {
			String line;
			try {
				line = br.readLine();
				if (line != null) {
					t.setItems(Arrays.asList(line.split(this.delimiter)));
				} else {
					close();
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			t.setItems(Arrays.asList(this.currentLine.split(this.delimiter)));
			this.currentLine = "";
			return t;
		}

		return t;
	}

	@Override
	public boolean hasNext() {
		String line;
		try {
			line = br.readLine();
			if (line != null) {
				this.currentLine = line;
			} else {
				close();
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
