package pairwisetesting;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

public class PairwiseTestingToolkit {
	private IMetaParameterProvider provider;
	private Engine engine;
	private ITestCasesGenerator generator;

	public void setMetaParameterProvider(IMetaParameterProvider provider) {
		this.provider = provider;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void setTestCasesGenerator(ITestCasesGenerator generator) {
		this.generator = generator;
	}
	
	public String[][] generateTestData() throws MetaParameterException, EngineException {
		MetaParameter mp = provider.get();
		return engine.generateTestData(mp);
	}

	public String generateTestCases() throws MetaParameterException, EngineException {
		MetaParameter mp = provider.get();
		String[][] testData = engine.generateTestData(mp);
		return generator.generate(mp, testData);
	}

}
