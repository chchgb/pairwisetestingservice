package pairwisetesting;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.engine.tvg.TVGEngine;
import pairwisetesting.exception.EngineException;
import pairwisetesting.test.mock.MockTestCasesGenerator;

public class PairwiseTestingServiceImpl implements IPairwiseTestingService {
	
	public String PariwiseTesting(String inputMetaParameter,String engineName) {
		
		
		
		/*
		Engine engine = null;
		
		try {
			pts = (PTSInterface) Class.forName("pts." + engine).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(pts != null){
			pts.initEngine(inputNamesList, dataValues, nWay);
			boolean result = pts.startAlgorithm();
			ArrayList<String>  temp = pts.getOutputList();
			if(result == true){
				return ArrayListSerializ.ArrayList2String(temp);
			}else{
				return null;
			}			
		}else{
			return null;
		}
		*/
		
		Engine engine = null;
		
		MetaParameter mp = (MetaParameter)ObjectSerializ.String2Object(inputMetaParameter);
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			
			System.out.println("Engine error!!");
			return null;
		}
		
		
		
		return testCases;
	}
	
}