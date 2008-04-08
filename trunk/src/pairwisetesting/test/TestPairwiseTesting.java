package pairwisetesting.test;

import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.Matrix;
import pairwisetesting.engine.am.oaprovider.Rp_OLS_p2_OAProvider;
import pairwisetesting.engine.am.oaprovider.Rp_OLS_pu_OAProvider;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.exception.EngineException;
import pairwisetesting.exception.MetaParameterException;
import pairwisetesting.test.mock.MockMetaParameterProvider;
import pairwisetesting.test.mock.MockOAEngine;
import pairwisetesting.test.mock.MockTestCasesGenerator;

public class TestPairwiseTesting extends TestCase {
	private Factor f1, f2, f3, f4, f5;

	protected void setUp() throws Exception {
		super.setUp();
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f1.addLevel("Red Hat 9");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox", "Opera" });
		f3 = new Factor("Memory", new String[] { "255M", "1G", "2G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle", "DB2" });
		f5 = new Factor("Server", new String[] { "WebLogic", "JBoss", "Tomcat", "GlassFish" });
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

		assertEquals(3, mp.getNumOfFactors());
		
		assertEquals(3, mp.getMaxNumOfLevels());
		mp.addFactor(f5);
		assertEquals(4, mp.getMaxNumOfLevels());

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
		} catch (EngineException e) {
			e.printStackTrace();
			fail("Should not throw EngineException" + e);
		}
	}

	public void testMatrix() {
		Matrix m = new Matrix(2, 2);
		m.setElement(1, 1, 2);
		m.setElement(1, 2, 3);
		m.setElement(2, 1, 1);
		m.setElement(2, 2, 0);

		assertEquals(2, m.getElement(1, 1));
		assertEquals(1, m.getElement(2, 1));

		assertEquals(2, m.getNumOfRows());
		assertEquals(2, m.getNumOfColumns());

		Matrix m1 = new Matrix(2, 2);
		m1.setElement(1, 1, 2);
		m1.setElement(1, 2, 3);
		m1.setElement(2, 1, 1);
		m1.setElement(2, 2, 0);

		assertEquals(m1, m);

		Matrix m2 = new Matrix(3, 2);
		m2.setElement(1, 1, -1);
		m2.setElement(1, 2, 1);
		m2.setElement(2, 1, 2);
		m2.setElement(2, 2, -3);
		m2.setElement(3, 1, 1);
		m2.setElement(3, 2, 0);

		Matrix expected = new Matrix(6, 4);
		expected.setElement(1, 1, -2);
		expected.setElement(1, 2, 2);
		expected.setElement(1, 3, -3);
		expected.setElement(1, 4, 3);
		expected.setElement(2, 1, 4);
		expected.setElement(2, 2, -6);
		expected.setElement(2, 3, 6);
		expected.setElement(2, 4, -9);
		expected.setElement(3, 1, 2);
		expected.setElement(3, 2, 0);
		expected.setElement(3, 3, 3);
		expected.setElement(3, 4, 0);
		expected.setElement(4, 1, -1);
		expected.setElement(4, 2, 1);
		expected.setElement(4, 3, 0);
		expected.setElement(4, 4, 0);
		expected.setElement(5, 1, 2);
		expected.setElement(5, 2, -3);
		expected.setElement(5, 3, 0);
		expected.setElement(5, 4, 0);
		expected.setElement(6, 1, 1);
		expected.setElement(6, 2, 0);
		expected.setElement(6, 3, 0);
		expected.setElement(6, 4, 0);

		Matrix res = m1.directProduct(m2);
		// System.out.println(res);
		assertEquals(expected, res);

		int[][] expected1 = new int[][] { { 2, -3, 3 }, { -6, 6, -9 },
				{ 0, 3, 0 }, { 1, 0, 0 }, { -3, 0, 0 }, { 0, 0, 0 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
				res.to2DArray(2)));

		Matrix H2 = new Matrix(2, 2);
		H2.setElement(1, 1, 1);
		H2.setElement(1, 2, 1);
		H2.setElement(2, 1, 1);
		H2.setElement(2, 2, -1);
		Matrix H4 = H2.directProduct(H2);
		Matrix H8 = H2.directProduct(H4);
		assertEquals(1, H8.getElement(3, 1));
		assertEquals(1, H8.getElement(3, 2));
		assertEquals(-1, H8.getElement(3, 3));
		assertEquals(-1, H8.getElement(3, 4));
		assertEquals(1, H8.getElement(3, 5));
		assertEquals(1, H8.getElement(3, 6));
		assertEquals(-1, H8.getElement(3, 7));
		assertEquals(-1, H8.getElement(3, 8));
		assertEquals(1, H8.getElement(6, 1));
		assertEquals(-1, H8.getElement(6, 2));
		assertEquals(1, H8.getElement(6, 3));
		assertEquals(-1, H8.getElement(3, 4));
		assertEquals(-1, H8.getElement(6, 5));
		assertEquals(1, H8.getElement(3, 6));
		assertEquals(-1, H8.getElement(3, 7));
		assertEquals(1, H8.getElement(6, 8));
	}

	public void testH_2S_OAProvider() {
		OAProvider provider = new H_2s_OAProvider();

		int[][] rawTestData = provider.get(3);
		int[][] expected = { { 1, 1, 1 }, { 2, 1, 2 }, { 1, 2, 2 }, { 2, 2, 1 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		// System.out.println(Arrays.deepToString(rawTestData));

		rawTestData = provider.get(7);
		int[][] expected2 = { { 1, 1, 1, 1, 1, 1, 1 }, { 2, 1, 2, 1, 2, 1, 2 },
				{ 1, 2, 2, 1, 1, 2, 2 }, { 2, 2, 1, 1, 2, 2, 1 },
				{ 1, 1, 1, 2, 2, 2, 2 }, { 2, 1, 2, 2, 1, 2, 1 },
				{ 1, 2, 2, 2, 2, 1, 1 }, { 2, 2, 1, 2, 1, 1, 2 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));
	}

	public void testRp_OLS_p2_OAProvider() {

		OAProvider provider = new Rp_OLS_p2_OAProvider(3);

		// L9(3^4)
		int[][] rawTestData = provider.get(4);
		int[][] expected = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 2, 1, 2, 3 }, { 2, 2, 3, 1 }, { 2, 3, 1, 2 }, { 3, 1, 3, 2 },
				{ 3, 2, 1, 3 }, { 3, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));

		// L9(3^3)
		rawTestData = provider.get(3);
		int[][] expected2 = { { 1, 1, 1 }, { 1, 2, 2 }, { 1, 3, 3 },
				{ 2, 1, 2 }, { 2, 2, 3 }, { 2, 3, 1 }, { 3, 1, 3 },
				{ 3, 2, 1 }, { 3, 3, 2 } };
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));

		provider = new Rp_OLS_p2_OAProvider(5);
		// L25(5^6)
		rawTestData = provider.get(6);
		int[][] expected3 = { { 1, 1, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2, 2 },
				{ 1, 3, 3, 3, 3, 3 }, { 1, 4, 4, 4, 4, 4 },
				{ 1, 5, 5, 5, 5, 5 }, { 2, 1, 2, 3, 4, 5 },
				{ 2, 2, 3, 4, 5, 1 }, { 2, 3, 4, 5, 1, 2 },
				{ 2, 4, 5, 1, 2, 3 }, { 2, 5, 1, 2, 3, 4 },
				{ 3, 1, 3, 5, 2, 4 }, { 3, 2, 4, 1, 3, 5 },
				{ 3, 3, 5, 2, 4, 1 }, { 3, 4, 1, 3, 5, 2 },
				{ 3, 5, 2, 4, 1, 3 }, { 4, 1, 4, 2, 5, 3 },
				{ 4, 2, 5, 3, 1, 4 }, { 4, 3, 1, 4, 2, 5 },
				{ 4, 4, 2, 5, 3, 1 }, { 4, 5, 3, 1, 4, 2 },
				{ 5, 1, 5, 4, 3, 2 }, { 5, 2, 1, 5, 4, 3 },
				{ 5, 3, 2, 1, 5, 4 }, { 5, 4, 3, 2, 1, 5 },
				{ 5, 5, 4, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				rawTestData));

		// L25(5^4)
		rawTestData = provider.get(4);
		int[][] expected4 = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 1, 4, 4, 4 }, { 1, 5, 5, 5 }, { 2, 1, 2, 3 }, { 2, 2, 3, 4 },
				{ 2, 3, 4, 5 }, { 2, 4, 5, 1 }, { 2, 5, 1, 2 }, { 3, 1, 3, 5 },
				{ 3, 2, 4, 1 }, { 3, 3, 5, 2 }, { 3, 4, 1, 3 }, { 3, 5, 2, 4 },
				{ 4, 1, 4, 2 }, { 4, 2, 5, 3 }, { 4, 3, 1, 4 }, { 4, 4, 2, 5 },
				{ 4, 5, 3, 1 }, { 5, 1, 5, 4 }, { 5, 2, 1, 5 }, { 5, 3, 2, 1 },
				{ 5, 4, 3, 2 }, { 5, 5, 4, 3 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				rawTestData));

		provider = new Rp_OLS_p2_OAProvider(2);
		// L4(2^3)
		rawTestData = provider.get(3);
		int[][] expected5 = { { 1, 1, 1 }, { 1, 2, 2 }, { 2, 1, 2 },
				{ 2, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected5,
				rawTestData));

		// L4(2^2)
		rawTestData = provider.get(2);
		int[][] expected6 = { { 1, 1 }, { 1, 2 }, { 2, 1 }, { 2, 2 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected6,
				rawTestData));

	}
	
	public void testRp_OLS_pu_OAProvider() {
		OAProvider provider = new Rp_OLS_pu_OAProvider(3);

		// L9(3^4)
		int[][] rawTestData = provider.get(4);
		int[][] expected = { { 1, 1, 1, 1 }, { 1, 2, 2, 2 }, { 1, 3, 3, 3 },
				{ 2, 1, 2, 3 }, { 2, 2, 3, 1 }, { 2, 3, 1, 2 }, { 3, 1, 3, 2 },
				{ 3, 2, 1, 3 }, { 3, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				rawTestData));
		
		// L27(3^5)
		rawTestData = provider.get(5);
		int[][] expected2 = {
				{1, 1, 1, 1, 1}, 
				{1, 1, 2, 2, 2}, 
				{1, 1, 3, 3, 3}, 
				{1, 2, 1, 2, 3}, 
				{1, 2, 2, 3, 1}, 
				{1, 2, 3, 1, 2}, 
				{1, 3, 1, 3, 2}, 
				{1, 3, 2, 1, 3}, 
				{1, 3, 3, 2, 1}, 
				{2, 1, 1, 1, 1}, 
				{2, 1, 2, 2, 2}, 
				{2, 1, 3, 3, 3}, 
				{2, 2, 1, 2, 3}, 
				{2, 2, 2, 3, 1}, 
				{2, 2, 3, 1, 2}, 
				{2, 3, 1, 3, 2}, 
				{2, 3, 2, 1, 3}, 
				{2, 3, 3, 2, 1}, 
				{3, 1, 1, 1, 1}, 
				{3, 1, 2, 2, 2}, 
				{3, 1, 3, 3, 3}, 
				{3, 2, 1, 2, 3}, 
				{3, 2, 2, 3, 1}, 
				{3, 2, 3, 1, 2}, 
				{3, 3, 1, 3, 2}, 
				{3, 3, 2, 1, 3}, 
				{3, 3, 3, 2, 1}
				};
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				rawTestData));
		
		provider = new Rp_OLS_pu_OAProvider(5);
		// L25(5^6)
		rawTestData = provider.get(6);
		int[][] expected3 = { { 1, 1, 1, 1, 1, 1 }, { 1, 2, 2, 2, 2, 2 },
				{ 1, 3, 3, 3, 3, 3 }, { 1, 4, 4, 4, 4, 4 },
				{ 1, 5, 5, 5, 5, 5 }, { 2, 1, 2, 3, 4, 5 },
				{ 2, 2, 3, 4, 5, 1 }, { 2, 3, 4, 5, 1, 2 },
				{ 2, 4, 5, 1, 2, 3 }, { 2, 5, 1, 2, 3, 4 },
				{ 3, 1, 3, 5, 2, 4 }, { 3, 2, 4, 1, 3, 5 },
				{ 3, 3, 5, 2, 4, 1 }, { 3, 4, 1, 3, 5, 2 },
				{ 3, 5, 2, 4, 1, 3 }, { 4, 1, 4, 2, 5, 3 },
				{ 4, 2, 5, 3, 1, 4 }, { 4, 3, 1, 4, 2, 5 },
				{ 4, 4, 2, 5, 3, 1 }, { 4, 5, 3, 1, 4, 2 },
				{ 5, 1, 5, 4, 3, 2 }, { 5, 2, 1, 5, 4, 3 },
				{ 5, 3, 2, 1, 5, 4 }, { 5, 4, 3, 2, 1, 5 },
				{ 5, 5, 4, 3, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				rawTestData));
		
		provider = new Rp_OLS_pu_OAProvider(2);
		// L8(2^4)
		rawTestData = provider.get(4);
		int[][] expected4 = { 
			{1, 1, 1, 1},
			{1, 1, 2, 2},
			{1, 2, 1, 2},
			{1, 2, 2, 1}, 
			{2, 1, 1, 1},
			{2, 1, 2, 2},
			{2, 2, 1, 2},
			{2, 2, 2, 1}
		};
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				rawTestData));
	}

	public void testAMEngine() throws EngineException {
		Engine engine = new AMEngine();
		
		//
		// AMEngine with H_2s_OAProvider
		//
		Factor f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		Factor f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		Factor f3 = new Factor("Memory", new String[] { "1G", "2G" });
		Factor f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
		Factor f5 = new Factor("PC", new String[] { "DELL", "HP" });
		Factor f6 = new Factor("Server", new String[] { "WebLogic", "Tomcat" });
		Factor f7 = new Factor("HardDisk", new String[] { "40G", "80G" });
		MetaParameter mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);
		mp.addFactor(f5);
		mp.addFactor(f6);
		mp.addFactor(f7);

		String[][] testData = engine.generateTestData(mp);
		String[][] expected = {
				{ "Windows XP", "IE", "1G", "MySQL", "DELL", "WebLogic", "40G" },
				{ "Solaris 10", "IE", "2G", "MySQL", "HP", "WebLogic", "80G" },
				{ "Windows XP", "Firefox", "2G", "MySQL", "DELL", "Tomcat",
						"80G" },
				{ "Solaris 10", "Firefox", "1G", "MySQL", "HP", "Tomcat", "40G" },
				{ "Windows XP", "IE", "1G", "Oracle", "HP", "Tomcat", "80G" },
				{ "Solaris 10", "IE", "2G", "Oracle", "DELL", "Tomcat", "40G" },
				{ "Windows XP", "Firefox", "2G", "Oracle", "HP", "WebLogic",
						"40G" },
				{ "Solaris 10", "Firefox", "1G", "Oracle", "DELL", "WebLogic",
						"80G" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				testData));
		
		//
		// AMEngine with Rp_OLS_p2_OAProvider
		//
		mp = new MockMetaParameterProvider().get();
		testData = engine.generateTestData(mp);
		String[][] expected2 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "Opera", "2G", "DB2" },
				{ "Solaris 10", "IE", "1G", "DB2" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Opera", "255M", "Oracle" },
				{ "Red Hat 9", "IE", "2G", "Oracle" },
				{ "Red Hat 9", "Firefox", "255M", "DB2" },
				{ "Red Hat 9", "Opera", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected2,
				testData));
		
		// with missing values
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox"});
		f3 = new Factor("Memory", new String[] {"255M", "1G", "2G"});
		f4 = new Factor("DB", new String[] {"MySQL", "Oracle"});
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);
		
		testData = engine.generateTestData(mp);
		String[][] expected3 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "IE", "2G", "MySQL" },
				{ "Solaris 10", "IE", "1G", "Oracle" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Firefox", "255M", "Oracle" },
				{ "Windows XP", "IE", "2G", "Oracle" },
				{ "Solaris 10", "Firefox", "255M", "MySQL" },
				{ "Windows XP", "IE", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected3,
				testData));
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
