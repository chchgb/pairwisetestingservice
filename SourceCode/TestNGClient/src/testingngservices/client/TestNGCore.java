package testingngservices.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import pairwisetesting.client.PairwiseTestingClient;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.util.LibDependence;
import pairwisetesting.util.ObjectSerializ;
import pairwisetesting.util.TestingMetaParameter;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;


public class TestNGCore {
	
	//private Log log = LogFactory.getLog(TestNGCore.class);
	private String url;
	private String testCaseString;
	private TestingMetaParameter testingMeta;
	//private TestCaseTemplateParameter tp;
	//private MetaParameter mp;
	//private String engineName;
	private String testCasePath;
	//private String pairwiseXML;
	
	private Set<String> libSet;
	
	private String serviceIP;
	
	private String endPath = "";

	TestNGServiceClient client;
	TestNGServicePortType service;

	private TestNGCore() {
		libSet = new HashSet<String>();
		this.url = "http://localhost:8080/TestNGServices/services/TestNGService";
		client = new TestNGServiceClient(this.url);

		// create a default service endpoint
		service = client.getTestNGServiceHttpPort();
	}

	public TestNGCore(String url) {
		this.serviceIP = url;
		this.url = "http://"+ url +":8080/TestNGServices/services/TestNGService";
		//this.engineName = engineName;
		testingMeta = new TestingMetaParameter();
		libSet = new HashSet<String>();

		client = new TestNGServiceClient(this.url);

		// create a default service endpoint
		service = client.getTestNGServiceHttpPort();

	}
	
	private void setLibList(ArrayList<String> libList){
		for(String libName:libList){
			testingMeta.addLib(libName.substring(libName.lastIndexOf("/")+1,libName.length()));
		}		
	}
	
	
	public void setEndPath(String path){
		this.endPath = path;
	}

	public void setURL(String url) {
		this.url = url;
	}

	
	public String getTestCase(TestCaseTemplateParameter tp,String pairwiseXML) {

		//getPairwiseResult();

		this.testCaseString = service.getTestCase(tp.toXML(), pairwiseXML);
		

		testCasePath = "src/" + tp.getPackageName().replace(".", "/")
				+ "/" + "PairwiseTest.java";
		TextFile.write(endPath + testCasePath, this.testCaseString);
		return testCaseString;
	}

	public String testMethod(ArrayList<String> fileList,ArrayList<String> libList) {
		
		uploadLib(libList);
		String result = null;
		
		this.testingMeta.setEndPath(this.endPath);
		
		//log.info("File List: "+ fileList);
		
		//System.out.println("File List: " + fileList);
		this.testingMeta.addFileList(fileList);
		
		testingMeta.setTestCase(testCasePath);

		testingMeta.addFile(testCasePath);

		String input = ObjectSerializ.Object2String(testingMeta);
		result = service.testExecute(input);

		return result;

	}
	
	public void uploadLib(ArrayList<String> libList){
		this.libSet = getLibList();
		String libPath = "";
		String libName = "";
		////…Ë÷√±‡“Î ±µƒ“¿¿µø‚
		setLibList(libList);
		LibDependence libNeededUpload = new LibDependence();
		for(String lib:libList){
			
			System.out.println("lib : " + lib);
			
			libPath = lib.substring(0,lib.lastIndexOf("/")+1);
			libName = lib.substring(lib.lastIndexOf("/")+1,lib.length());
			
			
			System.out.println("libPath " +libPath);
			System.out.println("libName : " + libName);
			
			if(!libSet.contains(libName)){
				libNeededUpload.addJavaLib(libName, libPath);
			}
		}
		if(!libNeededUpload.isEmpty()){
			String libString = ObjectSerializ.Object2String(libNeededUpload);
			service.uploadLib(libString);
		}
	}

	public Set<String> getLibList(){
		Set<String> resultSet = new HashSet<String>();
		
		
		String[] temp = service.getLibList().split(";");

		
		for(int index = 0;index<temp.length;index++){
			resultSet.add(temp[index]);
		}
		
		return resultSet;
		
	}

}
