package pairwisetesting.engine.tvg;

import java.util.ArrayList;
import java.util.Arrays;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.PTSInterface;
import pairwisetesting.engine.TVG;
import pairwisetesting.exception.EngineException;

public class TVGEngine extends Engine {

	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		// TODO Auto-generated method stub
		ArrayList<String> iNames = new ArrayList<String>(Arrays.asList(mp.getFactorNames()));
		
		ArrayList<ArrayList<String>> iValues = new ArrayList<ArrayList<String>>();
		
		for(Factor factor:mp.getFactors()){
			iValues.add(factor.getLevelList());
		}
		
		int nWay = mp.getStrength();
		
		
		PTSInterface pict = new TVG();
		
		pict.initEngine(iNames, iValues, nWay);
		
		pict.startAlgorithm();
		ArrayList<String> resultArrayList = pict.getOutputList();
		
		String[][] testData = new String[resultArrayList.size()][];
		
		for(int i = 0; i<resultArrayList.size();i++){
			testData[i] = resultArrayList.get(i).trim().split(",");
			
		}
		
		return testData;
	}

}