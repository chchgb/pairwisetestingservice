package pairwisetesting.engine.am;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.oaprovider.H_2s_OAProvider;
import pairwisetesting.exception.EngineException;

public class AMEngine extends Engine {
	
	public AMEngine() {
		this.transformer = new OATestDataTransformer();
	}

	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		OAProvider provider = null;
		
		//TODO Currently only two OA providers are available
		int numOfLevels = mp.getMaxNumOfLevels();
		if ( numOfLevels == 2)
			provider = new H_2s_OAProvider();
		else
			provider = new Rp_OLS_p2_OAProvider(numOfLevels);
		
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
