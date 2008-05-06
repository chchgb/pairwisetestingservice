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
	private String url;
	private String testCaseString;
	private TestingMetaParameter testingMeta;
	private TestCaseTemplateParameter tp;
	private MetaParameter mp;
	private String engineName;
	private String testCasePath;
	
	private Set<String> libSet;
	
	private String serviceIP;
	
	private String endPath = "";

	TestNGServiceClient client;
	TestNGServicePortType service;

	public TestNGCore() {
		libSet = new HashSet<String>();
		this.url = "http://localhost:8080/TestNGServices/services/TestNGService";
		client = new TestNGServiceClient(this.url);

		// create a default service endpoint
		service = client.getTestNGServiceHttpPort();
	}

	public TestNGCore(TestCaseTemplateParameter tp, MetaParameter mp,
			String engineName,String url) {
		this.serviceIP = url;
		this.url = "http://"+ url +":8080/TestNGServices/services/TestNGService";
		this.tp = tp;
		this.mp = mp;
		this.engineName = engineName;
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

	public String getTestCase() {

		PairwiseTestingClient ptClient = new PairwiseTestingClient(serviceIP);
		ptClient.setMetaParameter(mp);
		ptClient.setEngine(engineName);
		
		System.out.println("getTestCase");
		
		
		String pairwiseXML = ptClient.execute();


		this.testCaseString = service.getTestCase(tp.toXML(), pairwiseXML);
		
		System.out.println("this.testCaseString" + this.testCaseString);

		testCasePath = "src/" + tp.getPackageName().replace(".", "/")
				+ "/" + "PairwiseTest.java";
		TextFile.write(endPath + testCasePath, this.testCaseString);
		return testCaseString;
	}

	public String testMethod(ArrayList<String> fileList,ArrayList<String> libList) {
		
		uploadLib(libList);
		String result = null;
		
		
		this.testingMeta.setEndPath(this.endPath);
		this.testingMeta.addFileList(fileList);
		
		
		testingMeta.setTestCase(testCasePath);


		testingMeta.addFile(testCasePath);

		String input = ObjectSerializ.Object2String(testingMeta);
		result = service.testExecute(input);

		return result;

	}
	
	private void uploadLib(ArrayList<String> libList){
		this.libSet = getLibList();
		String libPath = "";
		String libName = "";
		////设置编译时的依赖库
		setLibList(libList);
		LibDependence libNeededUpload = new LibDependence();
		for(String lib:libList){
			libPath = lib.substring(0,lib.lastIndexOf("/")+1);
			libName = lib.substring(lib.lastIndexOf("/")+1,lib.length());
			if(!libSet.contains(libName)){
				libNeededUpload.addJavaLib(libName, libPath);
			}
		}
		if(!libNeededUpload.isEmpty()){
			String libString = ObjectSerializ.Object2String(libNeededUpload);
			service.uploadLib(libString);
		}
	}
	/*
	public void uploadAllProjectLib(String classpathFullName){
		uploadLib(Dependence.getAllLibList(classpathFullName));
	}
	
	//库文件的全路径名列表
	public void uploadUserLib(ArrayList<String> userLib){
		uploadLib(userLib);
	}
	
	
	//待测类所依赖的库文件的关键包名ArrayList（如：[junit, stringtemplate, testng]）
	public void uploadDependenceLib(ArrayList<String> depLibName,String classpathFullName){
		ArrayList<String> allLibList = Dependence.getAllLibList(classpathFullName);
		ArrayList<String> depLib = new ArrayList<String>();
		Set<String> tempSet = new HashSet<String>();
		tempSet.addAll(depLibName);
		String libName = "";
		for(String fileFullName:allLibList){
			libName = fileFullName.substring(fileFullName.lastIndexOf("/")+1, fileFullName.length()-3);
			for(String depLibNameString:tempSet){
				if(libName.toLowerCase().contains(depLibNameString.toLowerCase())){
					depLib.add(fileFullName);
					System.out.println(fileFullName);
				}
			}
		}
		uploadLib(depLib);
		
	}
	*/
	public Set<String> getLibList(){
		Set<String> resultSet = new HashSet<String>();
		String[] temp = service.getLibList().split(";");
		
		System.out.println("getLibList" +temp);
		for(int index = 0;index<temp.length;index++){
			resultSet.add(temp[index]);
		}
		
		return resultSet;
		
	}

}
