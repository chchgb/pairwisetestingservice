package pairwisetesting.engine.pict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import pairwisetesting.PICT;
import pairwisetesting.PTSInterface;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.exception.EngineException;

public class PICTEngine extends Engine {

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
		
		
		PTSInterface pict = new PICT();
		
		pict.initEngine(iNames, iValues, nWay);
		
		boolean result = pict.startAlgorithm();
		ArrayList<String> resultArrayList = pict.getOutputList();
		
		String[][] testData = new String[resultArrayList.size()][];
		
		for(int i = 0; i<resultArrayList.size();i++){
			testData[i] = resultArrayList.get(i).trim().split(",");
			
		}
		
		return testData;
	}
}