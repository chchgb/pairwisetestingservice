package pairwisetesting.coredomain;

import pairwisetesting.exception.EngineException;

public abstract class Engine {
	protected ITestDataTransformer transformer = ITestDataTransformer.NULL;

	protected abstract String[][] generateRawTestData(MetaParameter mp) throws EngineException;

	public String[][] generateTestData(MetaParameter mp) throws EngineException {
		String[][] rawTestData = generateRawTestData(mp);
		return transformer.transform(mp, rawTestData);
	}

}
