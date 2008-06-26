package testingngservices.client;

import java.util.ArrayList;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.util.dependency.DependencyFinder;
import pairwisetesting.util.dependency.DependencyResult;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;

public class TestNGClient {
	
	private TestNGCore testNGCore;
	private String className;
	private String sourceCode;
	private String classPath;
	private String endPath = "";

	private ArrayList<String> srcList;
	private ArrayList<String> libList;

	public LibManager libManager;
	private TestCaseTemplateParameter tp;
	private String pairwiseXML;
	
	public TestNGClient(){
		classPath = "bin";

		this.srcList = new ArrayList<String>();
		this.libList = new ArrayList<String>();
		this.libManager = new LibManager();
		
	}

	public void initTestNGClient(TestCaseTemplateParameter tp,String serviceIP) {
		testNGCore = new TestNGCore(serviceIP);
		this.tp = tp;
		className = tp.getPackageName() + "." + tp.getClassUnderTest();
		sourceCode = "src/" + className.replace(".", "/") + ".java";

	}
	
	public void setTestCaseTemplateParameter(TestCaseTemplateParameter tp){
		this.tp = tp;
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
		this.testNGCore.setEndPath(path);
		this.endPath = path;
	}

	public LibManager getDependency() {
		
		DependencyFinder depFinder = new DependencyFinder(className, "src", classPath,endPath);
		DependencyResult res = depFinder.findDependency();
		
		if(!endPath.equals("")){
			this.srcList = DependencyResult.transferPath(endPath, res.srcList);
		}else{
			this.libList = res.libList;
		}
		
		
		libManager.addNotFoundLibFromArrayList(this.libList);
		libManager.setDependencyLib(res.impList);
		libManager.setMockList(res.mockList);
		
		System.out.println(res.mockList);
		return this.libManager;
		
	}
	
	
	public void setLibManager(LibManager lib){
		this.libManager = lib;
	}
	
	public LibManager getLibManager(){
		return this.libManager;
	}
	
	
	public String getPairwiseXML(){
		return pairwiseXML;
	}
	
	public void setPairwiseResult(String pairwiseXML){
		this.pairwiseXML = pairwiseXML;
	}

	public String getTestCase() {
		return testNGCore.getTestCase(tp,pairwiseXML);

	}
	
	public String getTestCasePath(){
		return testNGCore.getTestCasePath();
	}
	
	public ArrayList<String> getServiceLibList(){
		return new ArrayList<String>(testNGCore.getLibList());
	}

	public String getTestResult() {
		//getDependencyFile();
		this.srcList.add(sourceCode);
		String testResult = "";
		
		
		if (libManager.isFoundAllLib() && !this.srcList.isEmpty()) {

			// 得到测试结果
			testResult = testNGCore.testMethod(this.srcList, new ArrayList<String>(libManager
					.getFoundedLibSet()));

		} else {
			System.err.println("these lib can not found \n"
					+ libManager.getNotFoundLib());
		}
		return testResult;

	}
	


}
