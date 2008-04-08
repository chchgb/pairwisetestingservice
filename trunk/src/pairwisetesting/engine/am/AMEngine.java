package pairwisetesting.engine.am;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.oaprovider.OAProviderFactory;
import pairwisetesting.exception.EngineException;
import pairwisetesting.util.ArrayUtil;

public class AMEngine extends Engine {
	
	private IOAProviderFactory factory = new OAProviderFactory();
	
	public AMEngine() {
		this.transformer = new OATestDataTransformer();
	}

	public AMEngine(IOAProviderFactory factory) {
		this();
		this.factory = factory;
	}

	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		
		int numOfLevels = mp.getMaxNumOfLevels();
		OAProvider provider = factory.create(numOfLevels, mp.getNumOfFactors());
		
		int[][] rawTestData = provider.get(mp.getNumOfFactors());
		return ArrayUtil.int2DArrayToString2DArray(rawTestData);
	}

}
