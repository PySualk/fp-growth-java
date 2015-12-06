package fpgrowth;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FPgrowthTest {

	static Logger log = LoggerFactory.getLogger(FPgrowthTest.class);

	private FPgrowth fpGrowth;

	@Before
	public void setup() {
		fpGrowth = new FPgrowth();
	}

	@Test
	public void testExample1() {

		Set<FrequentPattern> expectedPatterns;
		expectedPatterns = new HashSet<>();
		expectedPatterns.add(new FrequentPattern("Diaper", 4, null));
		expectedPatterns.add(new FrequentPattern("Beer", 4, null));
		expectedPatterns.add(new FrequentPattern("Nuts", 3, null));
		expectedPatterns.add(new FrequentPattern("Eggs", 3, null));
		expectedPatterns.add(new FrequentPattern("Milk", 2, null));
		expectedPatterns.add(new FrequentPattern("Beer,Diaper", 3, null));
		expectedPatterns.add(new FrequentPattern("Diaper,Nuts", 2, null));
		expectedPatterns.add(new FrequentPattern("Diaper,Eggs", 2, null));
		expectedPatterns.add(new FrequentPattern("Nuts,Eggs", 2, null));

		Double minSupport = 0.2;
		File exampleTransactions1 = new File("exampleTransactions1.csv");
		Set<FrequentPattern> frequentPatterns = fpGrowth.findFrequentPattern(exampleTransactions1, minSupport);

		HashSet<String> actual = new HashSet<>();
		for (FrequentPattern fp : frequentPatterns) {
			actual.add(fp.toString());
		}

		HashSet<String> expected = new HashSet<>();
		for (FrequentPattern fp : expectedPatterns) {
			expected.add(fp.toString());
		}

		assertEquals(9, frequentPatterns.size());
		assertEquals(actual, expected);
	}

	/**
	 * Example data is from the book 'Introduction to Data Mining'
	 * (http://www-users.cs.umn.edu/~kumar/dmbook/index.php)
	 */
	@Test
	public void testExample2() {

		Set<FrequentPattern> expectedPatterns;
		expectedPatterns = new HashSet<>();
		expectedPatterns.add(new FrequentPattern("a", 8, 0.8));
		expectedPatterns.add(new FrequentPattern("a,b", 5, 0.5));
		expectedPatterns.add(new FrequentPattern("a,c", 2, 0.2));
		expectedPatterns.add(new FrequentPattern("a,d", 4, 0.4));
		expectedPatterns.add(new FrequentPattern("a,e", 2, 0.2));
		expectedPatterns.add(new FrequentPattern("a,b,c", 3, 0.3));
		expectedPatterns.add(new FrequentPattern("a,b,d", 2, 0.2));
		expectedPatterns.add(new FrequentPattern("a,c,d", 2, 0.2));
		expectedPatterns.add(new FrequentPattern("a,d,e", 2, 0.2));

		expectedPatterns.add(new FrequentPattern("b", 7, 0.7));
		expectedPatterns.add(new FrequentPattern("b,c", 5, 0.5));
		expectedPatterns.add(new FrequentPattern("b,d", 3, 0.3));
		expectedPatterns.add(new FrequentPattern("b,c,d", 2, 0.2));

		expectedPatterns.add(new FrequentPattern("c", 6, 0.6));
		expectedPatterns.add(new FrequentPattern("c,d", 3, 0.3));
		expectedPatterns.add(new FrequentPattern("c,e", 2, 0.2));

		expectedPatterns.add(new FrequentPattern("d", 5, 0.5));
		expectedPatterns.add(new FrequentPattern("d,e", 2, 0.2));

		expectedPatterns.add(new FrequentPattern("e", 3, 0.3));

		Double minSupport = 0.2;
		File exampleTransactions1 = new File("exampleTransactions2.csv");
		Set<FrequentPattern> frequentPatterns = fpGrowth.findFrequentPattern(exampleTransactions1, minSupport);

		HashSet<String> actual = new HashSet<>();
		for (FrequentPattern fp : frequentPatterns) {
			actual.add(fp.toString());
		}

		HashSet<String> expected = new HashSet<>();
		for (FrequentPattern fp : expectedPatterns) {
			expected.add(fp.toString());
		}

		assertEquals(19, frequentPatterns.size());
		assertEquals(actual, expected);
	}

}
