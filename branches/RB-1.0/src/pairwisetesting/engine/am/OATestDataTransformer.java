package pairwisetesting.engine.am;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.ITestDataTransformer;
import pairwisetesting.coredomain.MetaParameter;

public class OATestDataTransformer implements ITestDataTransformer {

	public String[][] transform(MetaParameter mp, String[][] rawTestData) {
		String[][] testData = rawTestData;
		for (int columnIndex = 0; columnIndex < rawTestData[0].length; columnIndex++) {
			
			String factorName = mp.getFactorNames()[columnIndex];
			Factor factor = mp.getFactor(factorName);
			
			// start index of the level to fill the missing value
			int nextIndexForEmpty = 0;
			
			for (int rowIndex = 0; rowIndex < rawTestData.length; rowIndex++) {
				int index = Integer.parseInt(rawTestData[rowIndex][columnIndex]) - 1;
				if (index < factor.getNumOfLevels()) {
					testData[rowIndex][columnIndex] = factor.getLevel(index);
				} else {
					testData[rowIndex][columnIndex] = factor.getLevel(nextIndexForEmpty);
					// next index of the level to fill the missing value
					nextIndexForEmpty = (nextIndexForEmpty + 1) % factor.getNumOfLevels();
				}
					
			}
		}
		return testData;
	}
}
