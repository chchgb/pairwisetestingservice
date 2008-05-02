package pairwisetesting;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.engine.pict.PICTEngine;
import pairwisetesting.engine.tvg.TVGEngine;
import pairwisetesting.metaparameterparser.XMLMetaParameterProvider;
import pairwisetesting.testcasesgenerator.XMLTestCasesGenerator;

public class PairwiseTestingServiceImpl implements IPairwiseTestingService {

	private String schemaPath = "D:/MyShare/Tomcat/schema/MetaParameter.xsd";

	public String PariwiseTesting(String inputMetaParameter, String engineName) {

		Engine engine = null;
		
		// MetaParameter mp =
		// (MetaParameter)ObjectSerializ.String2Object(inputMetaParameter);

		XMLMetaParameterProvider provider = new XMLMetaParameterProvider(
				inputMetaParameter, schemaPath);

		MetaParameter mp = null;
		try {
			mp = provider.get();
		} catch (MetaParameterException e1) {
			e1.printStackTrace();
		}

		System.out.print("Strength : " + mp.getStrength());
		System.out.println();

		if ("TVGEngine".equals(engineName)) {
			engine = new TVGEngine();
		} else if ("PICTEngine".equals(engineName)) {
			engine = new PICTEngine();
		} else if ("JennyEngine".equals(engineName)) {
			engine = new JennyEngine();
		} else if ("AMEngine".equals(engineName)) {
			engine = new AMEngine();
		} else {
			System.out.println("Engine none select!!");
			return null;
		}

		ITestCasesGenerator generator = new XMLTestCasesGenerator();
		String testCases = null;
		if (engine != null) {
			try {
				String[][] result = engine.generateTestData(mp);
				testCases = generator.generate(mp, result);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Engine error!!");
			return null;
		}
		return testCases;
	}
}