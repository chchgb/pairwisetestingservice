package pairwisetesting.test.expect;

import java.util.HashMap;

public class Expectation {
	
	private static HashMap<String, String> ExpectedResults = new HashMap<String, String>();
	
	static {
		ExpectedResults.put("math.Range.isBetween_5_5000_10000", "false");
		ExpectedResults.put("math.Range.isBetween_5_1_5", "true");
		ExpectedResults.put("math.Range.isBetween_500_5000_5", "false");
		ExpectedResults.put("math.Range.isBetween_50_1_10000", "true");
		ExpectedResults.put("math.Range.isBetween_50_5000_5", "false");
		ExpectedResults.put("math.Range.isBetween_500_1_10000", "true");
		
	}
	
	public static String getExpectedResult(String key) {
		return ExpectedResults.get(key);
	}
}
