package pairwisetesting.test;

import junit.framework.TestCase;
import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.EngineException;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.engine.am.AMEngine;
import pairwisetesting.engine.jenny.JennyEngine;
import pairwisetesting.engine.pict.PICTEngine;


public class BenchmarkTest extends TestCase {
	public void benchmarkEngine(Engine engine, MetaParameter mp) throws EngineException {
		double before = System.currentTimeMillis();
		String[][] testData = engine.generateTestData(mp);
		double after = System.currentTimeMillis();
		System.out.println("Time spent: " + (after - before));
		System.out.println("Level: " + mp.getMaxNumOfLevels());
		System.out.println("Factor: " + mp.getNumOfFactors());
		System.out.println("Run: " + testData.length);
//		for (String[] row : testData) {
//			System.out.println(Arrays.toString(row));
//		}
	}
	
	public MetaParameter getMetaParameter(int numberOfLevels, int numberOfFactors) {
		MetaParameter mp = new MetaParameter(2);
		for (int i = 0; i < numberOfFactors; i++) {
			Factor factor = new Factor("Factor_" + (i+1));
			for (int j = 0; j < numberOfLevels; j++) {
				factor.addLevel("" + (j+1));
			}
			mp.addFactor(factor);
		}
		return mp;
	}
	
	public void testBenchmark() throws EngineException {
		Engine[] engines = new Engine[] {
				new AMEngine(),
				new JennyEngine(), 
				new PICTEngine(),
		};
		MetaParameter mp = getMetaParameter(16, 17);
		for (Engine engine : engines) {
			System.out.println("--------"
					+ engine.getClass().getSimpleName()
					+ "--------");
			benchmarkEngine(engine, mp);	
		}
		
	}
}
