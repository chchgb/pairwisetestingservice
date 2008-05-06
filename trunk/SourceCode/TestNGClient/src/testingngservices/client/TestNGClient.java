package testingngservices.client;

import java.util.ArrayList;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.execution.TestCaseTemplateParameter;
import pairwisetesting.util.DependencyFinder;
import pairwisetesting.util.DependencyResult;

public class TestNGClient {
	private TestNGCore client;
	private String className;
	private String sourceCode;
	private String classPath;
	private String endPath = "";

	private ArrayList<String> srcList;
	private ArrayList<String> libList;
	private LibManager libManager;
	
	public TestNGClient(){
		classPath = "bin";

		this.srcList = new ArrayList<String>();
		this.libList = new ArrayList<String>();
		this.libManager = new LibManager();
		
	}

	public void initTestNGClient(TestCaseTemplateParameter tp, MetaParameter mp,
			String engineName, String serviceIP) {
		client = new TestNGCore(tp, mp, engineName, serviceIP);
		className = tp.getPackageName() + "." + tp.getClassUnderTest();
		sourceCode = "src/" + className.replace(".", "/") + ".java";

		

	}

	public LibManager getLibManager() {
		getDependencyFile();
		return this.libManager;
	}

	public void setClassPath(String path) {
		this.classPath = path;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public void setEndPath(String path) {
		if (!path.endsWith("/")) {
			path += "/";
		}
		this.client.setEndPath(path);
		this.endPath = path;
	}

	private void getDependencyFile() {
		 //DependencyFinder depFinder = new DependencyFinder(className, "src", classPath,endPath);
		DependencyFinder depFinder = new DependencyFinder(className, "src", classPath);
		DependencyResult res = depFinder.findDependency();
		
		this.srcList = DependencyResult.transferPath(endPath, res.srcList);
		this.libList = DependencyResult.transferPath(endPath, res.libList);
		libManager.addNotFoundLibFromArrayList(this.libList);
	}

	public String getTestCase() {
		return client.getTestCase();

	}

	public String getTestResult() {
		getDependencyFile();
		this.srcList.add(sourceCode);
		String testResult = "";
		if (libManager.isFoundAllLib() && !this.srcList.isEmpty()) {

			// 得到测试结果
			testResult = client.testMethod(this.srcList, libManager
					.getNeededLib());

		} else {
			System.err.println("these lib can not found \n"
					+ libManager.getNotFoundLib());
		}

		System.out.println("service.testExcute : \n\n" + testResult);
		return testResult;

	}

	// String className = tp.getPackageName()+"."+tp.getClassUnderTest();
	// String sourceCode = "src/"+className.replace(".", "/")+".java";

	// String classPath = "WebRoot/WEB-INF/classes";

	// System.out.println(className);

	// System.out.println("srcList : " + res.srcList);

	// LibManager libManager = new LibManager();
	// libManager.addNotFoundLibFromArrayList(res.libList);
	// libManager.addFoundedLibFromClasspath("D:/MyShare/.classpath");

}
