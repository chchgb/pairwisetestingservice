

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.client.PairwiseTestingClient;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import test.mock.MockMetaParameterProvider;


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



	public void testPICTEngine() throws MetaParameterException   {
		
        
        IMetaParameterProvider provider = new MockMetaParameterProvider();
		MetaParameter mp = provider.get();
		
		PairwiseTestingClient ptClient = new PairwiseTestingClient("localhost");
		ptClient.setMetaParameter(mp);
		ptClient.setEngine("PICTEngine");
		
		System.out.println("getTestCase");
		
		
		String pairwiseXML = ptClient.execute();
		
        
        System.out.println("end:\n"+ pairwiseXML);
        
	}
	
		
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
