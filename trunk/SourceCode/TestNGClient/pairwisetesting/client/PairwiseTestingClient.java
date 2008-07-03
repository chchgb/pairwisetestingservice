package pairwisetesting.client;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.metaparameterparser.MetaParameterXMLSerializer;

public class PairwiseTestingClient {
	
	private MetaParameter mp;
	private String engineName;
	private String testCases;
	private String url;
	
	public PairwiseTestingClient(){
		this.url = "http://localhost:8080/PariwiseTesting/services/PairwiseTestingService";
		           
	}
	
	public PairwiseTestingClient(String url){
		this.url = "http://" + url +":8080/PariwiseTesting/services/PairwiseTestingService";
	}
	
	public void setMetaParameter(MetaParameter mp) {
		this.mp = mp;
	}
	
	public void setEngine(String engineName){
		this.engineName = engineName;
	}
	
	public void setServicesURL(String url){
		this.url = url;
	}

	public String execute() {

		PairwiseTestingServiceClient client = new PairwiseTestingServiceClient(url);
		PairwiseTestingServicePortType service = client.getPairwiseTestingServiceHttpPort();

		//String input = ObjectSerializ.Object2String(mp);
		MetaParameterXMLSerializer metaSeri = new MetaParameterXMLSerializer();
		 
		String input = metaSeri.serialize(mp);
		testCases = service.pariwiseTesting(input, engineName);
		return testCases;

	}

}
