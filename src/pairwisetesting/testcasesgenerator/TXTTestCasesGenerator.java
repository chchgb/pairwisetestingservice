package pairwisetesting.testcasesgenerator;

import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

import com.google.common.base.Join;

public class TXTTestCasesGenerator implements ITestCasesGenerator {

	public String generate(MetaParameter mp, String[][] testData) {
		StringBuilder res = new StringBuilder();
		res.append(Join.join("\t", mp.getFactorNames()));
		res.append("\n");
		for (String[] row : testData) {
			res.append(Join.join("\t", row));
			res.append("\n");
		}
		return res.toString();
	}


}
