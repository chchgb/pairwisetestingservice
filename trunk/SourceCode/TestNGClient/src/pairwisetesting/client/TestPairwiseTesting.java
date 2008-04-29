package pairwisetesting.client;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.coredomain.PairwiseTestingToolkit;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
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

	public void testPICTEngine() throws MetaParameterException {
		
        
        IMetaParameterProvider provider = new MockMetaParameterProvider();
		MetaParameter mp = provider.get();
		
		
		PairwiseTestingClient ptClient = new PairwiseTestingClient();
		ptClient.setMetaParameter(mp);
		ptClient.setEngine("PICTEngine");
		String result = ptClient.execute();
		
        
        System.out.println("end:\n"+ result);
        
	}
	
		
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
