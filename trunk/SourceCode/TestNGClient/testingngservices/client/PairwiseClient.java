package testingngservices.client;

import pairwisetesting.client.PairwiseTestingClient;
import pairwisetesting.coredomain.MetaParameter;

public class PairwiseClient {
	private String engineName;
	private String url;
	//private String pairwiseXML;
	
	public PairwiseClient(String engineName,String url){
		this.engineName = engineName;
		this.url = url;
	}
	public String getPairwiseXML(MetaParameter mp){
		PairwiseTestingClient ptClient = new PairwiseTestingClient(url);
		ptClient.setMetaParameter(mp);
		ptClient.setEngine(engineName);
		
		//log.info("geting Testcase");
		
		
		String pairwiseXML = ptClient.execute();
		return pairwiseXML;
	}
	
	

}
