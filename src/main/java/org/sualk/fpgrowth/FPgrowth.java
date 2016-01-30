package org.sualk.fpgrowth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FPgrowth {

	private static final String PATTERN_DELIMITER = ",";
	private Map<String, Integer> itemFrequencies = new HashMap<>();
	private FPtree fpTree;
	private Map<String, FPtree> headerTable = new HashMap<>();
	private Set<FrequentPattern> frequentPatterns = new HashSet<>();
	private Double minSupport;
	private Integer minSupportCount;
	private Integer transactionCount;
	private DataSource dataSource;

	private static final Logger log = LoggerFactory.getLogger(FPgrowth.class);

	public Set<FrequentPattern> findFrequentPattern(Double minSupport,
			DataSource dataSource) {
		this.minSupport = minSupport;
		this.dataSource = dataSource;
		countFrequencyByWord();
		buildFPTree(dataSource);
		findFrequentPatterns();
		log.info("{} Frequent Item Sets found", this.frequentPatterns.size());
		return this.frequentPatterns;
	}

	private void countFrequencyByWord() {

		this.transactionCount = 0;
		while (dataSource.hasNext()) {
			Transaction t = dataSource.next();
			this.transactionCount++;
			for (String word : t.getItems()) {
				if (itemFrequencies.containsKey(word)) {
					Integer oldFrequency = itemFrequencies.get(word);
					itemFrequencies.replace(word, oldFrequency + 1);
				} else {
					itemFrequencies.put(word, 1);
				}
			}
		}

		this.minSupportCount = (int) Math.ceil(minSupport * transactionCount);
		log.debug("minSupport: {}", this.minSupport);
		log.debug("minSupportCount: {}", this.minSupportCount);
		log.debug("transactionCount: {}", transactionCount);

	}

	private void buildFPTree(DataSource dataSource) {

		// Add root to FPTree
		this.fpTree = new FPtree("null", null);
		this.fpTree.setRoot(Boolean.TRUE);

		// Create Header Table
		Map<String, FPtree> headerTable = new HashMap<>();

		// Iterate over transactions but order items by frequency
		dataSource.reset();
		while (dataSource.hasNext()) {
			Transaction t = dataSource.next();
			List<String> orderedWords = orderWordsByFrequency(t.getItems(),
					this.itemFrequencies);

			log.debug("Processing Transaction {}", orderedWords);

			List<Integer> orderedWordsValues = new ArrayList<>();
			for (int i = 0; i < t.getItems().size(); i++) {
				orderedWordsValues.add(1);
			}

			insertFPTree(this.fpTree, orderedWords, orderedWordsValues,
					headerTable);

			this.headerTable = headerTable;
		}

	}

	private List<String> orderWordsByFrequency(List<String> words,
			Map<String, Integer> frequencies) {
		List<String> orderedWords = new LinkedList<>();
		for (String word : words) {
			if (orderedWords.size() == 0) {
				orderedWords.add(word);
			} else {
				int count = frequencies.get(word);
				int i = 0;
				String wordToAdd = "";
				for (String orderedWord : orderedWords) {
					wordToAdd = word;
					if (orderedWord.length() > 0
							&& frequencies.get(orderedWord) < count) {
						break;
					}
					i++;
				}
				orderedWords.add(i, wordToAdd);
			}
		}
		return orderedWords;
	}

	private void insertFPTree(FPtree tree, List<String> words,
			List<Integer> wordValues, Map<String, FPtree> headerTable) {
		if (tree.getChildren().size() == 0) {
			if (words.size() > 0) {
				FPtree subTree = new FPtree(words.get(0), tree);
				subTree.setParent(tree);
				subTree.setCount(wordValues.get(0));
				if (headerTable.containsKey(words.get(0))) {
					subTree.setNext(headerTable.get(words.get(0)));
					headerTable.replace(words.get(0), subTree);
				} else {
					headerTable.put(words.get(0), subTree);
				}
				if (words.size() > 1)
					insertFPTree(subTree, words.subList(1, words.size()),
							wordValues.subList(1, wordValues.size()),
							headerTable);
				tree.addChild(subTree);
			}
		} else {
			for (FPtree child : tree.getChildren()) {
				if (child.getItem().equals(words.get(0))) {
					child.incrementCount();
					if (words.size() > 1)
						insertFPTree(child, words.subList(1, words.size()),
								wordValues.subList(1, wordValues.size()),
								headerTable);
					return;
				}
			}
			FPtree newChild = new FPtree(words.get(0), tree);
			newChild.setParent(tree);
			newChild.setCount(wordValues.get(0));
			if (headerTable.containsKey(words.get(0))) {
				newChild.setNext(headerTable.get(words.get(0)));
				headerTable.replace(words.get(0), newChild);
			} else {
				headerTable.put(words.get(0), newChild);
			}
			if (words.size() > 1)
				insertFPTree(newChild, words.subList(1, words.size()),
						wordValues.subList(1, wordValues.size()), headerTable);
			tree.addChild(newChild);
		}

	}

	private void findFrequentPatterns() {
		fpGrowthStep(this.fpTree, this.headerTable, this.frequentPatterns, "");
	}

	private void fpGrowthStep(FPtree tree, Map<String, FPtree> headerTable,
			Set<FrequentPattern> frequentPatterns, String base) {

		for (String item : headerTable.keySet()) {
			FPtree treeNode = headerTable.get(item);

			String currentPattern = item + PATTERN_DELIMITER + base;
			if (currentPattern.endsWith(PATTERN_DELIMITER))
				currentPattern = currentPattern.substring(0,
						currentPattern.length() - 1);

			log.debug("=============================================");
			log.debug("Start Mining Rules for {}", currentPattern);

			// 1. Step: Conditional Pattern Base
			Map<String, Integer> conditionalPatternBase = new HashMap<String, Integer>();

			// Is the item frequent? (count >= minSupport)
			Integer frequentItemsetCount = 0;

			// Jump from leaf to leaf
			while (treeNode != null) {

				String conditionalPattern = "";
				frequentItemsetCount += treeNode.getCount();
				Integer supportConditionalPattern = treeNode.getCount();

				FPtree parentNode = treeNode.getParent();

				// Work yourself up to the root
				while (!parentNode.isRoot()) {
					conditionalPattern = parentNode.getItem().concat(
							PATTERN_DELIMITER + conditionalPattern);
					parentNode = parentNode.getParent();
				}
				if (conditionalPattern.endsWith(PATTERN_DELIMITER))
					conditionalPattern = conditionalPattern.substring(0,
							conditionalPattern.length() - 1);

				treeNode = treeNode.getNext();

				if (!conditionalPattern.equals(""))
					conditionalPatternBase.put(conditionalPattern,
							supportConditionalPattern);

			}

			// Is the item frequent? (count >= minSupport)
			if (frequentItemsetCount < minSupportCount) {
				// Skip the current item
				log.debug("Refused Item Set: {} ({})", currentPattern,
						frequentItemsetCount);
				continue;
			} else {
				log.debug("Frequent Item Set {}, ({}) found", currentPattern,
						frequentItemsetCount);
				frequentPatterns.add(new FrequentPattern(currentPattern,
						frequentItemsetCount, (double) frequentItemsetCount
								/ transactionCount));
			}

			log.debug("ConditionalPatternBase: {}", conditionalPatternBase);

			// 2. Step: Conditional FP-Tree
			Map<String, Integer> conditionalItemFrequencies = new HashMap<>();
			FPtree conditionalTree = new FPtree("null", null);
			conditionalTree.setRoot(Boolean.TRUE);

			for (String conditionalPattern : conditionalPatternBase.keySet()) {
				StringTokenizer tokenizer = new StringTokenizer(
						conditionalPattern, PATTERN_DELIMITER);

				while (tokenizer.hasMoreTokens()) {
					String conditionalToken = tokenizer.nextToken();
					if (conditionalItemFrequencies
							.containsKey(conditionalToken)) {
						int count = conditionalItemFrequencies
								.get(conditionalToken);
						count += conditionalPatternBase.get(conditionalPattern);
						conditionalItemFrequencies.put(conditionalToken, count);
					} else {
						conditionalItemFrequencies.put(conditionalToken,
								conditionalPatternBase.get(conditionalPattern));
					}
				}
			}

			// Remove not frequent nodes
			Map<String, Integer> tmp = new HashMap<>(conditionalItemFrequencies);
			for (String s : tmp.keySet())
				if (conditionalItemFrequencies.get(s) < minSupportCount)
					conditionalItemFrequencies.remove(s);

			log.debug("ConditionalItemFrequencies: {}",
					conditionalItemFrequencies);

			// Construct Conditional FPTree
			HashMap<String, FPtree> conditionalHeaderTable = new HashMap<>();
			for (String conditionalPattern : conditionalPatternBase.keySet()) {
				StringTokenizer tokenizer = new StringTokenizer(
						conditionalPattern, PATTERN_DELIMITER);
				List<String> path = new ArrayList<>();
				List<Integer> pathValues = new ArrayList<>();

				while (tokenizer.hasMoreTokens()) {
					String conditionalToken = tokenizer.nextToken();
					if (conditionalItemFrequencies
							.containsKey(conditionalToken)) {
						path.add(conditionalToken);
						pathValues.add(conditionalPatternBase
								.get(conditionalPattern));

					}
				}
				if (path.size() > 0) {
					insertFPTree(conditionalTree, path, pathValues,
							conditionalHeaderTable);
				}

			}

			log.debug("End Mining Rules for {}", currentPattern);

			if (!conditionalTree.getChildren().isEmpty())
				fpGrowthStep(conditionalTree, conditionalHeaderTable,
						frequentPatterns, currentPattern);
		}
	}

}