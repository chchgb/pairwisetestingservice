

import junit.framework.TestCase;
import pairwisetesting.client.PairwiseTestingClient;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import test.mock.MockMetaParameterProvider;


public class TestPairwiseTesting extends TestCase {

	public void testPICTEngine() throws MetaParameterException   {
		    
        IMetaParameterProvider provider = new MockMetaParameterProvider();
		MetaParameter mp = provider.get();
		
		PairwiseTestingClient ptClient = new PairwiseTestingClient("localhost");
		ptClient.setMetaParameter(mp);
		ptClient.setEngine("PICTEngine");
		
		System.out.println("getTestCase");
			
		String pairwiseXML = ptClient.execute();
		
        System.out.println("end:\n"+ pairwiseXML);
        
	}

}
