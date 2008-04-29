package testingngservices.client;

import java.util.ArrayList;

import pairwisetesting.client.PairwiseTestingClient;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.execution.TestCaseTemplateParameter;

import testingngservices.sourcefile.ObjectSerializ;
import testingngservices.sourcefile.TestingMetaParameter;
import testingngservices.sourcefile.TextFile;

public class TestNGClient {
	private String url;
	private String testCaseString;
	private TestingMetaParameter testingMeta;
	private TestCaseTemplateParameter tp;
	private MetaParameter mp;
	private String engineName;
	private String testCasePath;
	
	private String serviceIP;
	
	private String endPath = "";

	TestNGServiceClient client;
	TestNGServicePortType service;

	private TestNGClient() {
	}

	public TestNGClient(TestCaseTemplateParameter tp, MetaParameter mp,
			String engineName,String url) {
		this.serviceIP = url;
		this.url = "http://"+ url +":8080/TestNGServices/services/TestNGService";
		this.tp = tp;
		this.mp = mp;
		this.engineName = engineName;
		testingMeta = new TestingMetaParameter();

		client = new TestNGServiceClient(this.url);

		// create a default service endpoint
		service = client.getTestNGServiceHttpPort();

	}
	
	public void setLibList(ArrayList<String> libList){
		for(String libName:libList){
			testingMeta.addLib(libName);
		}		
	}
	
	
	public void setEndPath(String path){
		this.endPath = path;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getTestCase() {

		PairwiseTestingClient ptClient = new PairwiseTestingClient(serviceIP);
		ptClient.setMetaParameter(mp);
		ptClient.setEngine(engineName);
		String pairwiseXML = ptClient.execute();


		this.testCaseString = service.getTestCase(tp.toXML(), pairwiseXML);
		

		testCasePath = "src/" + tp.getPackageName().replace(".", "/")
				+ "/" + "PairwiseTest.java";
		TextFile.write(endPath + testCasePath, this.testCaseString);
		return testCaseString;
	}

	public String testMethod(ArrayList<String> fileList) {
		String result = null;
		
		this.testingMeta.setEndPath(this.endPath);
		this.testingMeta.addFileList(fileList);
		
		
		testingMeta.setTestCase(testCasePath);


		testingMeta.addFile(testCasePath);

		String input = ObjectSerializ.Object2String(testingMeta);
		result = service.testExecute(input);

		return result;

	}

}
