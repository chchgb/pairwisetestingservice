package testingngservices.client;

import java.util.ArrayList;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.util.dependency.DependencyFinder;
import pairwisetesting.util.dependency.DependencyResult;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;

public class TestNGClient {
	
	private TestNGCore client;
	private String className;
	private String sourceCode;
	private String classPath;
	private String endPath = "";

	private ArrayList<String> srcList;
	private ArrayList<String> libList;
	public LibManager libManager;
	
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

	public void setClassPath(String path) {
		if (!path.endsWith("/")) {
			path += "/";
		}
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
		
		DependencyFinder depFinder = new DependencyFinder(className, "src", classPath,endPath);
		DependencyResult res = depFinder.findDependency();
		
		if(!endPath.equals("")){
			this.srcList = DependencyResult.transferPath(endPath, res.srcList);
		}else{
			this.libList = res.libList;
		}
		
		
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
		return testResult;

	}

}
