package test.testNGService;
import java.util.ArrayList;

import org.junit.*;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import testingngservices.client.LibManager;
import testingngservices.client.PairwiseClient;
import testingngservices.client.TestNGClient;
import testingngservices.client.TestNGCore;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;


public class TestSample {

	@Test
	public void TestNGClient() {
		TestCaseTemplateParameter tp = new TestCaseTemplateParameter();
		tp.setPackageName("test.math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		// tp.setStaticMethod(true);
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.setSingletonMethod("getInstance");
		tp.setReturnType("boolean");

		MetaParameter mp = new MetaParameter();
		Factor f_n = new Factor("n");
		f_n.addLevel(String.valueOf(5));
		f_n.addLevel(String.valueOf(50));
		f_n.addLevel(String.valueOf(500));
		mp.addFactor(f_n);

		Factor f_lower = new Factor("lower");
		f_lower.addLevel(String.valueOf(1));
		f_lower.addLevel(String.valueOf(5000));
		mp.addFactor(f_lower);

		Factor f_upper = new Factor("upper");
		f_upper.addLevel(String.valueOf(5));
		f_upper.addLevel(String.valueOf(10000));
		mp.addFactor(f_upper);
		
		PairwiseClient pairwiseClient = new PairwiseClient("PICTEngine", "localhost");
		
		String pairwiseResult = pairwiseClient.getPairwiseXML(mp);
		
		TestNGClient testNGClient = new TestNGClient();
		
		
		testNGClient.initTestNGClient(tp, "localhost");
		testNGClient.setPairwiseResult(pairwiseResult);
		testNGClient.setClassPath("bin");
		testNGClient.setEndPath("D:/MyShare/Workspace/Eclipse/TestPlugin");
		//testNGClient.setEndPath("");
		

		
		testNGClient.libManager.addFoundedLibFromClasspath(".classpath");
		testNGClient.libManager.getNotFoundLib();
		String pairwiseXML = testNGClient.getPairwiseXML();
		
		System.out.println("Pairwise : "+ pairwiseXML);
		String testCase = testNGClient.getTestCase();
		
		testNGClient.getDependency();
		String testResult = testNGClient.getTestResult();
		
		System.out.println("testResult: "+ testResult);

		
		

	}
	
	
//	public void uploadLib() {
//		ArrayList<String> libList = new ArrayList<String>();
//		TestNGCore client = new TestNGCore();
//		libList.add("Jmock");
//		//client.uploadDependenceLib(libList,"D:/MyShare/.classpath");
//
//	}
	
	
	public void libManager(){
		LibManager lib = new LibManager();
		
		ArrayList<String> libPartName = new ArrayList<String>();
		ArrayList<String> libFullName = new ArrayList<String>();
		
		libPartName.add("jmock");
		//libFullName.add(e)
		lib.addNotFoundLibFromArrayList(libPartName);
		lib.addFoundedLibFromClasspath("D:/MyShare/.classpath");
		
		
		System.out.println(lib.getNotFoundLib());
		System.out.println(lib.getFoundedLibSet());
		
		
	}
	
	
	

}
