package testingngservices.workflow;

import java.util.*;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.net.*;
import java.io.*;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pairwisetesting.util.TestingMetaParameter;
import pairwisetesting.util.TextFile;

public class TestWorkflow {
	private TestingMetaParameter testingMeta;
	private String workPath;
	private Log log = LogFactory.getLog(TestWorkflow.class);

	public TestWorkflow(TestingMetaParameter testingMeta) {
		this.testingMeta = testingMeta;
		this.workPath = testingMeta.getEndPath();
	}

	public String testWorkflow() {
		String result = null;
		testingMeta.addSourceFile("src/pairwisetesting/util/Converter.java");
		testingMeta.addSourceFile("src/pairwisetesting/util/ClassUtil.java");
		
		{
			testingMeta.addSourceFile("src/pairwisetesting/complex/ChildParametersExtractor.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/ComplexParameter.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/DefaultParameterVisitor.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/IMethodUnderTestXMLHelper.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/IParameterVisitor.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/MethodUnderTest.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/Parameter.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/SimpleParameter.java");
			testingMeta.addSourceFile("src/pairwisetesting/complex/XStreamMethodUnderTestXMLHelper.java");
		}
		// testingMeta.addFile("src/test/expect/Expectation.java");
		
		ArrayList<String> libList = new ArrayList<String>();
		{
			libList.add("testng-5.8-jdk15.jar");
			libList.add("xom-1.1.jar");
			libList.add("objenesis-1.0.jar");
			libList.add("jmock-legacy-2.4.0.jar");
			libList.add("jmock-junit4-2.4.0.jar");
			libList.add("jmock-junit3-2.4.0.jar");
			libList.add("jmock-2.4.0.jar");
			libList.add("hamcrest-library-1.1.jar");
			libList.add("hamcrest-core-1.1.jar");
			libList.add("cglib-nodep-2.1_3.jar");
			libList.add("antlr-2.7.7.jar");
			libList.add("junit.jar");
			libList.add("jxl.jar");
			libList.add("xstream-1.3.jar");
			
			
		}

		
		for(String lib:libList){
			testingMeta.addLib(lib);
		}
		testingMeta.writeFiles();
		// testingMeta.writeTestCase();
		log.info("Refactor the source code structor");
		File outputDir = new File(testingMeta.getEndPath() + "bin");
		outputDir.mkdirs();

		// Compile the classes related with test
		String[] files = testingMeta.getFileArray();
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
				.getJavaFileObjects(files);
		String buildPath = testingMeta.getLibString();
		String[] options = new String[] { "-d",
				testingMeta.getEndPath() + "bin", "-classpath", buildPath };

		log.info("Build Path :" + testingMeta.getLibString());

		log.info("Compile source code");
		compiler.getTask(null, fileManager, null, Arrays.asList(options), null,
				compilationUnits1).call();
		try {
			fileManager.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Load the classes related with test

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		
		System.out.println("cl : " + cl.getClass().getFields());
		System.out.println("cl : " + cl.getClass().getName());
		
		
		
		URL[] urls;
		
//		
//		
//		ArrayList<File> fileList = new ArrayList<File>();
//		for(String libName:libList){
//			File file = new File(workPath +"lib/"+ libName);
//			fileList.add(file);
//		}
//		
//		fileList.add(outputDir);
//		urls = new URL[fileList.size()];
//		for(int i=0;i<urls.length;i++){
//			try {
//				urls[i] = fileList.get(i).toURI().toURL();
//				System.out.println(urls[i]);
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		//File lib = new File(workPath +"lib/");
		
		TestNG tng = null;
		try {

			urls = new URL[] { outputDir.toURI().toURL() };

			URLClassLoader ucl = new URLClassLoader(urls, cl);
			
			Thread.currentThread().setContextClassLoader(ucl);
			
			System.out.println("UCL length : " + ucl.getURLs().length);
			System.out.println("UCL : " + Arrays.toString(ucl.getURLs()));

			// Run Test with TestNG
			// testingMeta.gettestCaseClassName()
			Class<?> clazz = ucl.loadClass(testingMeta.gettestCaseClassName());
//			ucl.loadClass("test.bookstore.Logger");
//			ucl.loadClass(testingMeta.gettestCaseClassName());
//			ucl.loadClass(testingMeta.gettestCaseClassName());
//			ucl.loadClass(testingMeta.gettestCaseClassName());
//			ucl.loadClass(testingMeta.gettestCaseClassName());
			
			log.info("ClassName : \n" +clazz);
			
			tng = new TestNG();
			
			tng.setTestClasses(new Class[] { clazz });
			// tng.set
			TestListenerAdapter listener = new TestListenerAdapter();
			tng.setOutputDirectory(workPath + "testNG-Log/");
			tng.addListener(listener);

			log.info("Excute test case");
			tng.run();
			//tng.run();
			// result = "PASSED: " + listener.getPassedTests().size()
			// + "\nFAILED: " + listener.getFailedTests().size();
			result = TextFile.read(workPath + "testNG-Log/"
					+ "emailable-report.html");
			
			log.info("TestNG Finish!");
			//ucl.clearAssertionStatus();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tng = null;
			
		}
		
		//cl.clearAssertionStatus();
		

		return result;

	}

}
