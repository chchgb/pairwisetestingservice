package test.testNGService;
import java.util.ArrayList;

import org.junit.*;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import testingngservices.client.LibManager;
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
		
		System.out.println("111");
		
		
		
		TestNGClient client = new TestNGClient();
		
		
		client.initTestNGClient(tp, mp, "PICTEngine", "localhost");
		//client.setEndPath(filePath);
		client.setClassPath("bin");
		client.setEndPath("D:/MyShare/Workspace/MyEclipse/PairwiseTest/");
//		client.setEndPath("D:/MyShare/Workspace/MyEclipse/TestNGClient/");
		
		

		
		System.out.println("222");
		client.libManager.addFoundedLibFromClasspath(".classpath");
		client.libManager.getNotFoundLib();
		
		System.out.println("22222");
		
		
		System.out.println("333");
		
		String testCase = client.getTestCase();
		
		System.out.println("444");
		String testResult = client.getTestResult();
		
		System.out.println("555");
		System.out.println("testResult: "+ testResult);

		
		

	}
	
	
	public void uploadLib() {
		ArrayList<String> libList = new ArrayList<String>();
		TestNGCore client = new TestNGCore();
		libList.add("Jmock");
		//client.uploadDependenceLib(libList,"D:/MyShare/.classpath");

	}
	
	
	public void libManager(){
		LibManager lib = new LibManager();
		
		ArrayList<String> libPartName = new ArrayList<String>();
		ArrayList<String> libFullName = new ArrayList<String>();
		
		libPartName.add("jmock");
		//libFullName.add(e)
		lib.addNotFoundLibFromArrayList(libPartName);
		lib.addFoundedLibFromClasspath("D:/MyShare/.classpath");
		
		
		System.out.println(lib.getNotFoundLib());
		System.out.println(lib.getNeededLib());
		
		
	}
	
	
	

}
