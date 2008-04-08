package pairwisetesting.engine.am;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.oaprovider.OAProviderFactory;
import pairwisetesting.exception.EngineException;

public class AMEngine extends Engine {
	
	private OAProviderFactory factory;
	
	public AMEngine() {
		this.transformer = new OATestDataTransformer();
		this.factory = new OAProviderFactory();
	}

	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		
		int numOfLevels = mp.getMaxNumOfLevels();
		OAProvider provider = factory.create(numOfLevels, mp.getNumOfFactors());
		
		int[][] rawTestData = provider.get(mp.getNumOfFactors());
		return int2DArrayToString2DArray(rawTestData);
	}
	
	private String[][] int2DArrayToString2DArray(int[][] int2DArray) {
		String[][] res = new String[int2DArray.length][int2DArray[0].length];
		for (int column = 0; column < int2DArray[0].length; column++) {
			for (int row = 0; row < int2DArray.length; row++) {
				res[row][column] = "" + int2DArray[row][column];
			}
		}
		return res;
	}

}
