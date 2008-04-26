package pairwisetesting;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.engine.tvg.TVGEngine;
import pairwisetesting.exception.EngineException;
import pairwisetesting.exception.MetaParameterException;
import pairwisetesting.metaparameterprovider.XMLMetaParameterProvider;
import pairwisetesting.test.mock.MockTestCasesGenerator;

public class PairwiseTestingServiceImpl implements IPairwiseTestingService {
	
	public String PariwiseTesting(String inputMetaParameter,String engineName) {

		
		Engine engine = null;
		
		//MetaParameter mp = (MetaParameter)ObjectSerializ.String2Object(inputMetaParameter);
		
		
		
		IMetaParameterProvider provider = new XMLMetaParameterProvider(inputMetaParameter);
		
		MetaParameter mp = null;
		try {
			mp = provider.get();
		} catch (MetaParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		System.out.print("Strength : "+ mp.getStrength());
		System.out.println();
		
		if("TVGEngine".equals(engineName)){
			engine = new TVGEngine();
		}else if("PICTEngine".equals(engineName)){
			engine = new PICTEngine();
		}else{
			
			System.out.println("Engine none select!!");
			return null;
		}
		
		
		ITestCasesGenerator generator = new MockTestCasesGenerator();
		String testCases = null;
		if(engine != null){
			try {
				String[][]  result = engine.generateTestData(mp);
				
				testCases = generator.generate(mp, result);
				
			} catch (EngineException e) {
				e.printStackTrace();
			}
			
		}else{
			
			System.out.println("Engine error!!");
			return null;
		}
		
		
		
		return testCases;	
		
	}
	
}