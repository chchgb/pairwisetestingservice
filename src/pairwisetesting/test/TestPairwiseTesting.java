package pairwisetesting.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import pairwisetesting.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.Matrix;
import pairwisetesting.engine.am.oaprovider.OAProviderFactory;
import pairwisetesting.engine.am.oaprovider.Rp_OLS_p2_OAProvider;
import pairwisetesting.engine.am.oaprovider.Rp_OLS_pu_OAProvider;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.execution.Invoke;
import pairwisetesting.execution.InvokeSequence;
import pairwisetesting.execution.TestCaseTemplateEngine;
import pairwisetesting.execution.TestCaseTemplateParameter;
import pairwisetesting.metaparameterprovider.XMLMetaParameterProvider;
import pairwisetesting.test.mock.MockMetaParameterProvider;
import pairwisetesting.test.mock.MockOAEngine;
import pairwisetesting.test.mock.MockOAProviderFactory;
import pairwisetesting.test.mock.MockTestCasesGenerator;
import pairwisetesting.testcasesgenerator.TXTTestCasesGenerator;
import pairwisetesting.testcasesgenerator.XMLTestCasesGenerator;
import pairwisetesting.util.ArrayUtil;
import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.MathUtil;
import pairwisetesting.util.TextFile;
import pairwisetesting.xml.MetaParameterXMLSerializer;

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
		f5 = new Factor("Server", new String[] { "WebLogic", "JBoss", "Tomcat",
				"GlassFish" });
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

		mp.addConstraint("IF [File system] = \"FAT\" THEN [Size] <= 4096");
		mp
				.addConstraint("IF [OS_2] = \"WinXP\" THEN [SKU_2] = \"Professional\"");
		assertEquals("IF [File system] = \"FAT\" THEN [Size] <= 4096", mp
				.getConstraints()[0]);
		assertEquals("IF [OS_2] = \"WinXP\" THEN [SKU_2] = \"Professional\"",
				mp.getConstraints()[1]);

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

	public void testPICTEngine() throws MetaParameterException {
		Engine engine = new PICTEngine();
		IMetaParameterProvider provider = new MockMetaParameterProvider();
		MetaParameter mp = provider.get();
		String[][] testData = null;
		try {
			testData = engine.generateTestData(mp);
			assertNotNull(testData);
		} catch (EngineException e) {
			e.printStackTrace();
			fail("Should not throw EngineException" + e);
		}
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
		int[][] expected2 = { { 1, 1, 1, 1, 1 }, { 1, 1, 2, 2, 2 },
				{ 1, 1, 3, 3, 3 }, { 1, 2, 1, 2, 3 }, { 1, 2, 2, 3, 1 },
				{ 1, 2, 3, 1, 2 }, { 1, 3, 1, 3, 2 }, { 1, 3, 2, 1, 3 },
				{ 1, 3, 3, 2, 1 }, { 2, 1, 1, 1, 1 }, { 2, 1, 2, 2, 2 },
				{ 2, 1, 3, 3, 3 }, { 2, 2, 1, 2, 3 }, { 2, 2, 2, 3, 1 },
				{ 2, 2, 3, 1, 2 }, { 2, 3, 1, 3, 2 }, { 2, 3, 2, 1, 3 },
				{ 2, 3, 3, 2, 1 }, { 3, 1, 1, 1, 1 }, { 3, 1, 2, 2, 2 },
				{ 3, 1, 3, 3, 3 }, { 3, 2, 1, 2, 3 }, { 3, 2, 2, 3, 1 },
				{ 3, 2, 3, 1, 2 }, { 3, 3, 1, 3, 2 }, { 3, 3, 2, 1, 3 },
				{ 3, 3, 3, 2, 1 } };
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
		int[][] expected4 = { { 1, 1, 1, 1 }, { 1, 1, 2, 2 }, { 1, 2, 1, 2 },
				{ 1, 2, 2, 1 }, { 2, 1, 1, 1 }, { 2, 1, 2, 2 }, { 2, 2, 1, 2 },
				{ 2, 2, 2, 1 } };
		// System.out.println(Arrays.deepToString(rawTestData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				rawTestData));
	}

	public void testOAProviderFactory() {
		OAProviderFactory factory = new OAProviderFactory();
		assertTrue("It should create H_2s_OAProvider object", factory.create(2,
				3) instanceof H_2s_OAProvider);
		assertTrue("It should create Rp_OLS_p2_OAProvider object", factory
				.create(3, 4) instanceof Rp_OLS_p2_OAProvider);
		assertTrue("It should create Rp_OLS_pu_OAProvider object", factory
				.create(2, 4) instanceof Rp_OLS_pu_OAProvider);
		assertTrue("It should create Rp_OLS_pu_OAProvider object", factory
				.create(3, 5) instanceof Rp_OLS_pu_OAProvider);
	}

	public void testAMEngine() throws EngineException {
		Engine engine = new AMEngine(new MockOAProviderFactory());
		String[][] testData = engine
				.generateTestData(new MockMetaParameterProvider().get());

		String[][] expected = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "Firefox", "1G", "Oracle" },
				{ "Windows XP", "Opera", "2G", "DB2" },
				{ "Solaris 10", "IE", "1G", "DB2" },
				{ "Solaris 10", "Firefox", "2G", "MySQL" },
				{ "Solaris 10", "Opera", "255M", "Oracle" },
				{ "Red Hat 9", "IE", "2G", "Oracle" },
				{ "Red Hat 9", "Firefox", "255M", "DB2" },
				{ "Red Hat 9", "Opera", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected,
				testData));

		engine = new AMEngine();
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

		testData = engine.generateTestData(mp);
		String[][] expected1 = {
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
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected1,
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
		f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		f3 = new Factor("Memory", new String[] { "255M", "1G", "2G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
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

		//
		// AMEngine with Rp_OLS_pu_OAProvider
		//
		f1 = new Factor("OS");
		f1.addLevel("Windows XP");
		f1.addLevel("Solaris 10");
		f2 = new Factor("Browser", new String[] { "IE", "Firefox" });
		f3 = new Factor("Memory", new String[] { "255M", "1G" });
		f4 = new Factor("DB", new String[] { "MySQL", "Oracle" });
		mp = new MetaParameter(2);
		mp.addFactor(f1);
		mp.addFactor(f2);
		mp.addFactor(f3);
		mp.addFactor(f4);

		testData = engine.generateTestData(mp);
		String[][] expected4 = { { "Windows XP", "IE", "255M", "MySQL" },
				{ "Windows XP", "IE", "1G", "Oracle" },
				{ "Windows XP", "Firefox", "255M", "Oracle" },
				{ "Windows XP", "Firefox", "1G", "MySQL" },
				{ "Solaris 10", "IE", "255M", "MySQL" },
				{ "Solaris 10", "IE", "1G", "Oracle" },
				{ "Solaris 10", "Firefox", "255M", "Oracle" },
				{ "Solaris 10", "Firefox", "1G", "MySQL" } };
		// System.out.println(Arrays.deepToString(testData));
		assertTrue("2D arrays should be equal", Arrays.deepEquals(expected4,
				testData));

	}

	public void testUtil() {
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(1));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(3));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(7));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(63));
		assertTrue("It should be 2^s - 1", MathUtil.is_2sMinusOne(255));

		assertTrue("It should be a prime", MathUtil.isPrime(2));
		assertTrue("It should be a prime", MathUtil.isPrime(7));
		assertTrue("It should be a prime", MathUtil.isPrime(17));
		assertTrue("It should be a prime", MathUtil.isPrime(67));
		assertFalse("It should not be a prime", MathUtil.isPrime(4));
		assertFalse("It should not be a prime", MathUtil.isPrime(6));
		assertFalse("It should not be a prime", MathUtil.isPrime(10));

		String[][] expected = new String[][] { { "1", "2", "3" },
				{ "4", "5", "6" } };
		int[][] testArray = new int[][] { { 1, 2, 3 }, { 4, 5, 6 } };
		assertTrue("They should be equal", Arrays.deepEquals(expected,
				ArrayUtil.int2DArrayToString2DArray(testArray)));
		
		String text = TextFile.read("testdata/data1.txt");
		assertEquals("This is a string.\n", text);
	}

	public void testMetaParameterProvider() throws MetaParameterException {
		String xmlData = "<?xml version='1.0' encoding='UTF-8'?>" +
				"<metaparameter>" +
				"	<strength>2</strength>" +
				"	<factor>" +
				"		<name>OS</name>" +
				"		<level>Windows XP</level>" +
				"		<level>Solaris 10</level>" +
				"		<level>Red Hat 9</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>Browser</name>" +
				"		<level>IE</level>" +
				"		<level>Firefox</level>" +
				"		<level>Opera</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>Memory</name>" +
				"		<level>255M</level>" +
				"		<level>1G</level>" +
				"		<level>2G</level>" +
				"	</factor>" +
				"	<factor>" +
				"		<name>DB</name>" +
				"		<level>MySQL</level>" +
				"		<level>Oracle</level>" +
				"		<level>DB2</level>" +
				"	</factor>" +
				"</metaparameter>";
		String schemaPath = "schema/MetaParameter.xsd";
		IMetaParameterProvider provider = new XMLMetaParameterProvider(xmlData, schemaPath);
		MetaParameter mp = provider.get();
		MetaParameter expected = new MockMetaParameterProvider().get();
		// System.out.println(mp);
		assertEquals(expected, mp);
		
		String dataFile = "testdata/MetaParameter.xml";
		provider = new XMLMetaParameterProvider(TextFile.read(dataFile), schemaPath);
		mp = provider.get();
		// System.out.println(mp);
		assertEquals(expected, mp);
		
		// Invalid XML
//		for (int i = 3; i <= 3; i++) {
//			dataFile = "testdata/InvalidMetaParameter" + i + ".xml";
//			provider = new XMLMetaParameterProvider(TextFile.read(dataFile));
//			try {			
//				mp = provider.get();
//				fail(dataFile + ": Should throw MetaParameterException");
//			} catch (MetaParameterException e) {
//				System.out.println("fdfd");
//			}
//		}
	}
	
	public void testMetaParameterXMLSerializer() throws MetaParameterException {
		MetaParameter mp = new MockMetaParameterProvider().get();
		
		MetaParameterXMLSerializer serializer = new MetaParameterXMLSerializer();
		// System.out.println(serializer.serialize(mp));

		String schemaPath = "schema/MetaParameter.xsd";
		IMetaParameterProvider provider = new XMLMetaParameterProvider(serializer.serialize(mp), schemaPath);
		
		MetaParameter mp2 = provider.get();
		assertEquals(mp, mp2);
		
		// Escape special characters
		mp = new MetaParameter();
		Factor f = new Factor("<&OS&>", new String[] {"Windows", "Linux"});
		Factor f2 = new Factor("DB", new String[] {"<=MySQL=>", ">=DB2<="});
		mp.addFactor(f);
		mp.addFactor(f2);
		mp.addConstraint("IF [File system] = \"FAT\" THEN [Size] <= 4096");
		String xmlData = serializer.serialize(mp);
		String expected = "<?xml version=\"1.0\"?>\n" + 
		"<metaparameter>" +
		"<strength>2</strength>" +
		"<factor><name>&lt;&amp;OS&amp;&gt;</name>" +
		"<level>Windows</level>" +
		"<level>Linux</level>" +
		"</factor>" +
		"<factor><name>DB</name>" +
		"<level>&lt;=MySQL=&gt;</level>" +
		"<level>&gt;=DB2&lt;=</level>" +
		"</factor>" +
		"<constraint>IF [File system] = \"FAT\" THEN [Size] &lt;= 4096</constraint>" + 
		"</metaparameter>\n";
		assertEquals(expected, xmlData);
		provider = new XMLMetaParameterProvider(xmlData, schemaPath);
		mp2 = provider.get();
		assertEquals(mp, mp2);
	}
	
	public void testTestCasesGenerator() throws EngineException {
		ITestCasesGenerator generator = new TXTTestCasesGenerator();
		MetaParameter mp = new MockMetaParameterProvider().get();
		String[][] testData = new MockOAEngine().generateTestData(mp);
		String testCases = generator.generate(mp, testData);
		String expectedTestCases = "OS	Browser	Memory	DB\n"
			+ "Windows XP	IE	255M	MySQL\n"
			+ "Windows XP	Firefox	1G	Oracle\n"
			+ "Windows XP	Opera	2G	DB2\n"
			+ "Solaris 10	IE	1G	DB2\n"
			+ "Solaris 10	Firefox	2G	MySQL\n"
			+ "Solaris 10	Opera	255M	Oracle\n"
			+ "Red Hat 9	IE	2G	Oracle\n"
			+ "Red Hat 9	Firefox	255M	DB2\n"
			+ "Red Hat 9	Opera	1G	MySQL\n";
		// System.out.println(testCases);
		assertEquals(expectedTestCases, testCases);
		
		generator = new XMLTestCasesGenerator();
		testCases = generator.generate(mp, testData);
		
		String expectedTestCases2 = "<?xml version=\"1.0\"?>\n" +
				"<testcases>" +
				"<factor>OS</factor>" +
				"<factor>Browser</factor>" +
				"<factor>Memory</factor>" +
				"<factor>DB</factor>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>IE</level>" +
				"<level>255M</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>Firefox</level>" +
				"<level>1G</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>Windows XP</level>" +
				"<level>Opera</level>" +
				"<level>2G</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>IE</level>" +
				"<level>1G</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>Firefox</level>" +
				"<level>2G</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"<run>" +
				"<level>Solaris 10</level>" +
				"<level>Opera</level>" +
				"<level>255M</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>" + "Red Hat 9</level>" +
				"<level>IE</level>" +
				"<level>2G</level>" +
				"<level>Oracle</level>" +
				"</run>" +
				"<run>" +
				"<level>Red Hat 9</level>" +
				"<level>Firefox</level>" +
				"<level>255M</level>" +
				"<level>DB2</level>" +
				"</run>" +
				"<run>" +
				"<level>Red Hat 9</level>" +
				"<level>Opera</level>" +
				"<level>1G</level>" +
				"<level>MySQL</level>" +
				"</run>" +
				"</testcases>\n";
		// System.out.println(testCases);
		assertEquals(expectedTestCases2, testCases);
	}
	
	public void testClassUtil() {
		assertEquals(TestCase.class, ClassUtil.getClass("junit.framework.TestCase"));
		assertEquals(int.class, ClassUtil.getClass("int"));
		assertEquals(boolean.class, ClassUtil.getClass("boolean"));
		assertEquals(double.class, ClassUtil.getClass("double"));
		assertTrue(ClassUtil.isAbstractClass(TestCase.class));
		assertFalse(ClassUtil.isAbstractClass(Integer.class));
		assertTrue(ClassUtil.isInterface(List.class));
		assertFalse(ClassUtil.isInterface(TestCase.class));
		assertEquals("boolean", ClassUtil.getReturnTypeName(
				"pairwisetesting.test.math.Range", "isBetween", "int", "int",
				"int"));
		assertEquals("boolean", ClassUtil.getReturnTypeName(
				"pairwisetesting.test.math.Range", "isBetween"));
		assertEquals("void", ClassUtil.getReturnTypeName(
				"junit.framework.TestCase", "tearDown"));
		assertEquals("java.lang.String", ClassUtil.getReturnTypeName(
				"junit.framework.TestCase", "toString"));
		assertEquals("Range", ClassUtil.getSimpleClassName("pairwisetesting.test.math.Range"));
		assertEquals("Range", ClassUtil.getSimpleClassName("Range"));
	}
	
	public void testInvokeSequence() {
		InvokeSequence is = new InvokeSequence("src/pairwisetesting/test/bank/AccountService.java");
		
		assertEquals("manager", is.getFieldName("pairwisetesting.test.bank.IAccountManager"));
		assertEquals("manager", is.getFieldName("IAccountManager"));
		
		// is.setScopeByMethod("double", "withdraw", new Parameter("String", "accountId"), new Parameter("double", "amount"));
		is.setScopeByMethod("double", "withdraw");	
		Invoke[] sequences = is.findByFieldType("pairwisetesting.test.bank.IAccountManager");
		Invoke[] expectedSequences = new Invoke[3];
		expectedSequences[0] = new Invoke("manager.beginTransaction()", "void");
		expectedSequences[1] = new Invoke("manager.withdraw(accountId, amount)", "double");
		expectedSequences[2] = new Invoke("manager.commit()", "void");
		// System.out.println(Arrays.toString(sequences));
		assertTrue(Arrays.equals(sequences, expectedSequences));
		
		String[] expectedJMockSequences = new String[4];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).withdraw(accountId, amount)";
		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[3] = "one (manager).commit()";
		String[] jMockSequences = is.generateJMockInvokeSequenceByFieldType("pairwisetesting.test.bank.IAccountManager");
		// System.out.println(Arrays.toString(jMockSequences));
		assertTrue(Arrays.equals(jMockSequences, expectedJMockSequences));
	}
	
	public void testTestCaseTemplate() throws Exception {
		TestCaseTemplateParameter tp = new TestCaseTemplateParameter();
		assertFalse(tp.isSingleton());
		assertFalse(tp.isStaticMethod());
		assertFalse(tp.hasCheckStateMethod());
		assertFalse(tp.hasConstructorArguments());
		assertFalse(tp.hasDelta());
		assertFalse(tp.hasClassesToMock());
		
		tp.setPackageName("math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		tp.setStaticMethod(true);
		tp.setReturnType("boolean");
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.addConstructorArgument("int", "1");
		tp.addConstructorArgument("String", "Tom");
		tp.setSingletonMethod("getInstance");
		tp.setCheckStateMethod("getComputeResult");
		tp.setDelta(0.2);
		tp.addImport("java.io.*");
		tp.addImport("java.net.*");
		tp.addImport("pairwisetesting.test.bank.IAccountManager");
		tp.addClassToMockInstanceName("pairwisetesting.test.bank.IAccountManager", "manager");
		String[] jMockSequences = new String[4];
		jMockSequences[0] = "one (manager).beginTransaction()";
		jMockSequences[1] = "one (manager).withdraw(accountId, amount)";
		jMockSequences[2] = "will(returnValue(<NeedFilled>)";
		jMockSequences[3] = "one (manager).commit()";
		tp.addJMockInvokeSequence("pairwisetesting.test.bank.IAccountManager", jMockSequences);
		// tp.addClassToMockInstanceName("pairwisetesting.test.bank.AbstractAccountRepository", "repository");
		
		assertTrue(tp.isSingleton());
		assertTrue(tp.isStaticMethod());
		assertTrue(tp.hasCheckStateMethod());
		assertTrue(tp.hasConstructorArguments());
		assertEquals(2, tp.getConstructorArguments().length);
		assertEquals(3, tp.getMethodParameters().length);
		assertEquals(3, tp.getImports().length);
		assertTrue(tp.hasClassesToMock());
		assertTrue(tp.hasDelta());
		// System.out.println(tp.toXML());
		assertNotNull(tp.toXML());
		
		TestCaseTemplateParameter tp2 = new TestCaseTemplateParameter(tp.toXML());
		// System.out.println(tp2.toXML());
		assertEquals(tp, tp2);
		
		String pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		TestCaseTemplateEngine te = new TestCaseTemplateEngine();
		te.setTemplateDir("templates");
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertNotNull(te.generateTestNGTestCase());
		
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>n</factor>"
            + "<factor>lower</factor><"
            + "factor>upper</factor>"
            + "<run><level>3</level><level>1</level><level>4</level></run>"
            + "<run><level>3</level><level>3</level><level>4</level></run>"
            + "<run><level>4</level><level>3</level><level>4</level></run>"
            + "</testcases>";
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("pairwisetesting.test.math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		tp.setStaticMethod(true);
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.setSingletonMethod("getInstance");
		tp.setReturnType("boolean");
		
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains("Range.isBetween"));
		tp.setStaticMethod(false);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains("Range.getInstance().isBetween"));
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("pairwisetesting.test.bookstore");
		tp.setClassUnderTest("BookStore");
		tp.addConstructorArgument("String", "PKU");
		tp.addConstructorArgument("int", "40");
		tp.setMethodUnderTest("computeDiscountedPrice");
		tp.addMethodParameter("int", "level");
		tp.addMethodParameter("AccountType", "accountType");
		tp.addMethodParameter("String", "coupon");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		assertTrue(te.generateTestNGTestCase().contains("double testResult = bookStore.computeDiscountedPrice(level, accountType, coupon)"));
		
		tp.setCheckStateMethod("getDiscountedPrice");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		assertTrue(te.generateTestNGTestCase().contains("bookStore.getDiscountedPrice()"));
		
		tp.addImport("pairwisetesting.test.bookstore.Logger");
		tp.addClassToMockInstanceName("pairwisetesting.test.bookstore.Logger", "logger");
		jMockSequences = new String[3];
		jMockSequences[0] = "one (logger).log(level)";
		jMockSequences[1] = "one (logger).log(accountType)";
		jMockSequences[2] = "one (logger).log(coupon)";
		tp.addJMockInvokeSequence("pairwisetesting.test.bookstore.Logger", jMockSequences);
		
		tp.setCheckStateMethod("");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[0]));
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[1]));
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[2]));
		
		tp.setCheckStateMethod("getDiscountedPrice");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[0]));
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[1]));
		assertTrue(te.generateTestNGTestCase().contains(jMockSequences[2]));
		assertTrue(te.generateTestNGTestCase().contains("bookStore.getDiscountedPrice()"));
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("pairwisetesting.test.bank");
		tp.setClassUnderTest("AccountService");
		tp.setMethodUnderTest("withdraw");
		tp.addMethodParameter("String", "accountId");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		tp.addImport("pairwisetesting.test.bank.IAccountManager");
		tp.addClassToMockInstanceName("pairwisetesting.test.bank.IAccountManager", "manager");
		
		InvokeSequence is = new InvokeSequence("src/pairwisetesting/test/bank/AccountService.java");
		is.setScopeByMethod("double", "withdraw");
		jMockSequences = is.generateJMockInvokeSequenceByFieldType("pairwisetesting.test.bank.IAccountManager");
		tp.addJMockInvokeSequence("pairwisetesting.test.bank.IAccountManager", jMockSequences);
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountId</factor>"
            + "<factor>amount</factor>"
            + "<run><level>A001</level><level>1000</level>></run>"
            + "<run><level>A002</level><level>2000</level></run>"
            + "<run><level>A001</level><level>2000</level></run>"
            + "<run><level>A002</level><level>1000</level></run>"
            + "</testcases>";
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		
		String[] expectedJMockSequences = new String[4];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).withdraw(accountId, amount)";
		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[3] = "one (manager).commit()";
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[0]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[1]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[2]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[3]));
		
		tp.addImport("pairwisetesting.test.bank.Logger");
		tp.addClassToMockInstanceName("pairwisetesting.test.bank.Logger", "logger");
		jMockSequences = is.generateJMockInvokeSequenceByFieldType("pairwisetesting.test.bank.Logger");
		tp.addJMockInvokeSequence("pairwisetesting.test.bank.Logger", jMockSequences);

		expectedJMockSequences = new String[6];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).withdraw(accountId, amount)";
		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[3] = "one (manager).commit()";
		expectedJMockSequences[4] = "one (logger).log(accountId)";
		expectedJMockSequences[5] = "one (logger).log(amount)";
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[0]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[1]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[2]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[3]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[4]));
		assertTrue(te.generateTestNGTestCase().contains(expectedJMockSequences[5]));
	}
	
//	public void testPairwiseTestingService() {
//		String xmlData = "<?xml version='1.0' encoding='UTF-8'?>" +
//		"<metaparameter>" +
//		"	<strength>2</strength>" +
//		"	<factor>" +
//		"		<name>OS</name>" +
//		"		<level>Windows XP</level>" +
//		"		<level>Solaris 10</level>" +
//		"	</factor>" +
//		"	<factor>" +
//		"		<name>Browser</name>" +
//		"		<level>IE</level>" +
//		"		<level>Firefox</level>" +
//		"	</factor>" +
//		"	<factor>" +
//		"		<name>Memory</name>" +
//		"		<level>255M</level>" +
//		"		<level>1G</level>" +
//		"	</factor>" +
//		"</metaparameter>";
//		String expectedTestCases = "<?xml version=\"1.0\"?>\n" +
//		"<testcases>" +
//		"<factor>OS</factor>" +
//		"<factor>Browser</factor>" +
//		"<factor>Memory</factor>" +
//		"<run>" +
//		"<level>Windows XP</level>" +
//		"<level>IE</level>" +
//		"<level>255M</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Solaris 10</level>" +
//		"<level>IE</level>" +
//		"<level>1G</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Windows XP</level>" +
//		"<level>Firefox</level>" +
//		"<level>1G</level>" +
//		"</run>" +
//		"<run>" +
//		"<level>Solaris 10</level>" +
//		"<level>Firefox</level>" +
//		"<level>255M</level>" +
//		"</run>" +
//		"</testcases>\n";
//		PairwiseTestingServiceImpl service = new PairwiseTestingServiceImpl();
//		assertEquals(expectedTestCases, service.PariwiseTesting(xmlData, "AMEngine"));
//	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
