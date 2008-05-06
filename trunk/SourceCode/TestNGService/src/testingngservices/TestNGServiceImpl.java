package testingngservices;

import java.io.File;

import pairwisetesting.util.Directory;
import pairwisetesting.util.LibDependence;
import pairwisetesting.util.ObjectSerializ;
import pairwisetesting.util.TestingMetaParameter;
import pairwisetesting.util.Directory.TreeInfo;
import testingngservices.core.TestWorkflow;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngine;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngineException;


public class TestNGServiceImpl implements ITestNGService {
	private String workPath = "D:/MyShare/Tomcat/";
	//private String workPath = "";

	public String testExecute(String testingMeta) {
		String result = null;

		TestingMetaParameter temp = (TestingMetaParameter) ObjectSerializ
				.String2Object(testingMeta);
		
		temp.setEndPath(workPath);
		
		TestWorkflow workflow = new TestWorkflow(temp);
		result = workflow.testWorkflow();
		return result;
	}

	public String getTestCase(String methodMeta,String pairwistXML) {
		String testCase = null;
		
		TestCaseTemplateEngine te = new TestCaseTemplateEngine();
		te.setTemplateDir(workPath + "templates");
		te.setPairwiseTestCasesXmlData(pairwistXML);
		te.setTestCaseTemplateParameterXmlData(methodMeta);
		
		try {
			testCase = te.generateTestNGTestCase();
		} catch (TestCaseTemplateEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testCase;
	}

	public String getLibList() {
		String libListString = "";
		System.out.println("begine getLibList");
		TreeInfo libList = Directory.walk(workPath + "lib/", ".*[.]jar$");
		System.out.println(workPath);
		for(File file:libList){
			libListString += file.getName()+";";
		}
		
		System.out.println(libListString);
		return libListString.substring(0,libListString.length()-1);
	}

	public void uploadLib(String lib) {
		
		LibDependence libDpend = (LibDependence) ObjectSerializ.String2Object(lib);
		libDpend.writeLibList(workPath + "lib/");
		
	}

}