package pairwisetesting;

import java.util.ArrayList;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.MetaParameter;
//Generated by MyEclipse
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.engine.tvg.TVGEngine;
import pairwisetesting.exception.EngineException;

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
		
		MetaParameter mp = MetaParameterSerializ.String2Object(inputMetaParameter);
		
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
		
		if(engine != null){
			try {
				String[][]  result = engine.generateTestData(mp);
				for(String[] resulta:result){
					for(String resultb:resulta){
						System.out.println(resultb+" ");
					}
					System.out.println();
				}
			} catch (EngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			
			System.out.println("Engine error!!");
			return null;
		}
		
		
		
		return "true";
	}
	
}