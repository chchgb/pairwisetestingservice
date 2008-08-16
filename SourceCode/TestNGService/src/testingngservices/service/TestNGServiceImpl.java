package testingngservices.service;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pairwisetesting.util.Directory;
import pairwisetesting.util.LibDependence;
import pairwisetesting.util.ObjectSerializ;
import pairwisetesting.util.TestingMetaParameter;
import pairwisetesting.util.TextFile;
import pairwisetesting.util.Directory.TreeInfo;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngine;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngineException;
import testingngservices.workflow.TestWorkflow;


public class TestNGServiceImpl implements ITestNGService {
	private String workPath = "D:/MyShare/Tomcat/";
	private Log log = LogFactory.getLog(TestNGServiceImpl.class);
	//private String workPath = "";

	public String testExecute(String testingMeta) {
		String result = null;

//		System.out.println("testingMeta: \n");
//		
//		TextFile.write("D:/text",testingMeta);
		
		TestingMetaParameter temp = (TestingMetaParameter) ObjectSerializ
				.String2Object(testingMeta);
		
		
		
		log.info("TestNGService work Path : "+workPath);
		temp.setEndPath(workPath);
		
//		CopyOfTestWorkflow workflow = new CopyOfTestWorkflow(temp,result);
//		workflow.start();
//		System.out.println(result);
		
		TestWorkflow workflow = new TestWorkflow(temp);
		result = workflow.testWorkflow();
		
		log.info("return result");
		return result;
	}

	public String getTestCase(String methodMeta,String pairwistXML) {
		String testCase = null;
		
		TestCaseTemplateEngine te = new TestCaseTemplateEngine();
		te.setTemplateDir(workPath + "templates");
		te.setPairwiseTestCasesXmlData(pairwistXML);
		te.setTestCaseTemplateParameterXmlData(methodMeta);
		
		try {
			log.info("Generate TestNG TestCase");
			testCase = te.generateTestNGTestCase();
		} catch (TestCaseTemplateEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testCase;
	}

	public String getLibList() {
		String libListString = "";
		log.info("Get Service Lib List");
		TreeInfo libList = Directory.walk(workPath + "lib/", ".*[.]jar$");
		for(File file:libList){
			libListString += file.getName()+";";
		}
		
		return libListString.substring(0,libListString.length()-1);
	}

	public void uploadLib(String lib) {
		log.info("upload lib to Service");
		LibDependence libDpend = (LibDependence) ObjectSerializ.String2Object(lib);
		libDpend.writeLibList(workPath + "lib/");
		
	}

}