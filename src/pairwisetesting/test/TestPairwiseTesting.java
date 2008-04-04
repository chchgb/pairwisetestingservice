package pairwisetesting.test;

import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.JennyEngine;
import pairwisetesting.exception.EngineException;
import pairwisetesting.exception.MetaParameterException;
import pairwisetesting.test.mock.MockMetaParameterProvider;
import pairwisetesting.test.mock.MockOAEngine;
import pairwisetesting.test.mock.MockTestCasesGenerator;

public class TestPairwiseTesting extends TestCase {
	private Factor f1, f2, f3, f4;

	protected void setUp() throws Exception {
		super.setUp();
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f1.addLevel("Red Hat 9");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox", "Opera" });
		f3 = new Factor("Memory", new String[] { "255M", "1G", "2G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle", "DB2" });
	}

	public void testFactor() {
		assertEquals("OS", f1.getName());

		assertEquals("Solaris 10", f1.getLevel(1));

		assertEquals(3, f1.getNumOfLevels());

		String[] expectedLevels = new String[3];
		expectedLevels[0] = "Windows XP";
		expectedLevels[1] = "Solaris 10";
		expectedLevels[2] = "Red Hat 9";
		assertTrue(Arrays.equals(expectedLevels, f1.getLevels()));

		assertEquals("Browser", f2.getName());

		expectedLevels[0] = "IE";
		expectedLevels[1] = "Firefox";
		expectedLevels[2] = "Opera";
		assertTrue(Arrays.equals(expectedLevels, f2.getLevels()));
	}

	public void testMetaParameter() {
		MetaParameter mp = new MetaParameter();

		mp.setStrength(2);
		assertEquals(2, mp.getStrength());

		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		assertEquals(f1, mp.getFactor("OS"));
		assertEquals(f3, mp.getFactor("Memory"));

		assertEquals(f1, mp.getFactors()[0]);
		assertEquals(f3, mp.getFactors()[2]);

		assertEquals("OS", mp.getFactorNames()[0]);
		assertEquals("Memory", mp.getFactorNames()[2]);

		assertEquals("Windows XP", mp.getLevelOfFactor("OS", 0));

		MetaParameter mp2 = new MetaParameter(3);
		assertEquals(3, mp2.getStrength());
	}

	public void testPairwiseTestingToolkit() {
		PairwiseTestingToolkit toolkit = new PairwiseTestingToolkit();
		toolkit.setMetaParameterProvider(new MockMetaParameterProvider());
		toolkit.setEngine(new MockOAEngine());
		toolkit.setTestCasesGenerator(new MockTestCasesGenerator());

		String[][] testData = null;
		try {
			testData = toolkit.generateTestData();
		} catch (MetaParameterException e) {
			fail("Should not throw MetaParameterException" + e);
		} catch (EngineException e) {
			fail("Should not throw EngineException" + e);
		}
		assertEquals(f1.getLevel(0), testData[0][0]);
		assertEquals(f1.getLevel(1), testData[3][0]);
		assertEquals(f1.getLevel(2), testData[6][0]);
		assertEquals(f2.getLevel(0), testData[6][1]);
		assertEquals(f2.getLevel(1), testData[7][1]);
		assertEquals(f2.getLevel(2), testData[8][1]);
		assertEquals(f3.getLevel(1), testData[3][2]);
		assertEquals(f3.getLevel(2), testData[4][2]);
		assertEquals(f3.getLevel(0), testData[5][2]);
		assertEquals(f4.getLevel(0), testData[0][3]);
		assertEquals(f4.getLevel(1), testData[1][3]);
		assertEquals(f4.getLevel(2), testData[2][3]);

		String testCases = null;
		try {
			testCases = toolkit.generateTestCases();
			// System.out.println(testCases);
		} catch (MetaParameterException e) {
			fail("Should not throw MetaParameterException" + e);
		} catch (EngineException e) {
			fail("Should not throw EngineException" + e);
		}
		String expectedTestCases = "OS	Browser	Memory	DB	\n"
				+ "Windows XP	IE	255M	MySQL	\n"
				+ "Windows XP	Firefox	1G	Oracle	\n"
				+ "Windows XP	Opera	2G	DB2	\n" + "Solaris 10	IE	1G	DB2	\n"
				+ "Solaris 10	Firefox	2G	MySQL	\n"
				+ "Solaris 10	Opera	255M	Oracle	\n"
				+ "Red Hat 9	IE	2G	Oracle	\n" + "Red Hat 9	Firefox	255M	DB2	\n"
				+ "Red Hat 9	Opera	1G	MySQL	\n";
		assertEquals(expectedTestCases, testCases);
	}

	public void testJennyEngine() throws MetaParameterException {
		Engine engine = new JennyEngine();
		IMetaParameterProvider provider = new MockMetaParameterProvider();
		ITestCasesGenerator generator = new MockTestCasesGenerator();
		MetaParameter mp = provider.get();
		String[][] testData = null;
		try {
			testData = engine.generateTestData(mp);
			assertNotNull(testData);
			String testCases = generator.generate(mp, testData);
			String expectedTestCases = "OS	Browser	Memory	DB	\n"
					+ "Windows XP\tOpera\t2G\tMySQL\t\n"
					+ "Solaris 10\tFirefox\t255M\tDB2\t\n"
					+ "Red Hat 9\tIE\t1G\tOracle\t\n"
					+ "Windows XP\tIE\t255M\tOracle\t\n"
					+ "Solaris 10\tIE\t2G\tDB2\t\n"
					+ "Red Hat 9\tFirefox\t1G\tMySQL\t\n"
					+ "Solaris 10\tOpera\t1G\tDB2\t\n"
					+ "Red Hat 9\tOpera\t255M\tDB2\t\n"
					+ "Windows XP\tFirefox\t2G\tOracle\t\n"
					+ "Solaris 10\tIE\t255M\tMySQL\t\n"
					+ "Windows XP\tIE\t1G\tDB2\t\n"
					+ "Red Hat 9\tOpera\t2G\tOracle\t\n"
					+ "Solaris 10\tOpera\t1G\tOracle\t\n";
			assertEquals(expectedTestCases, testCases);
//			for (int i = 0; i != testData.length; ++i) {
//				System.out.println(Arrays.toString(testData[i]));
//			}
		} catch (EngineException e) {
			e.printStackTrace();
			fail("Should not throw EngineException" + e);
		}

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
