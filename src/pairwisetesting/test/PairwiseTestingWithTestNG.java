package pairwisetesting.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.exception.EngineException;
import pairwisetesting.util.TextFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

enum AccountType {
	NORMAL, Student, INTERNAL
}

class Book {
	public static double computeDiscountedPrice(double fixedPrice,
			String level, AccountType accountType, String coupon) {

		HashMap<String, Integer> levelMap = Maps.newHashMap();
		levelMap.put("Level1", 90);
		levelMap.put("Level2", 80);
		levelMap.put("Level3", 70);

		HashMap<String, Integer> couponMap = Maps.newHashMap();
		couponMap.put("C001", 5);
		couponMap.put("C002", 10);
		couponMap.put("C003", 20);

		double discountedPrice = 0;

		switch (accountType) {
		case NORMAL:
			discountedPrice = fixedPrice * levelMap.get(level) / 100
					- couponMap.get(coupon);
			break;
		case Student:
			discountedPrice = fixedPrice * 70 / 100 - couponMap.get(coupon);
			break;
		case INTERNAL:
			discountedPrice = fixedPrice * 60 / 100 - couponMap.get(coupon);
			// Double mode fault
			// if (level.equals("Level2")) {
			// discountedPrice = discountedPrice - 1;
			// }
			break;
		}

		return discountedPrice;
	}
}

public class PairwiseTestingWithTestNG {

	private ArrayList<Double> baseLine;
	private int current = 0;

	@BeforeMethod
	public void setUp() {
		if (baseLine == null) {
			TextFile file = new TextFile("testdata/baseline.txt");
			baseLine = Lists.newArrayList();
			for (String data : file) {
				baseLine.add(Double.parseDouble(data));
			}
		} else {
			current++;
		}
	}

	@Test(dataProvider = "pairwisetesting-testdata-provider")
	public void testComputeDiscountedPrice(String level,
			AccountType accountType, String coupon) {
		double price = Book.computeDiscountedPrice(100, level, accountType,
				coupon);
		Assert.assertEquals(price, baseLine.get(current), 0.001);
	}

	@DataProvider(name = "pairwisetesting-testdata-provider")
	public Object[][] testData() throws EngineException, ValidityException,
			ParsingException, IOException {
//		MetaParameter mp = new MetaParameter();
//		Factor f1 = new Factor("Level", new String[] { "Level1", "Level2",
//				"Level3" });
//		Factor f2 = new Factor("AccountType", new String[] { "NORMAL",
//				"Student", "INTERNAL" });
//		Factor f3 = new Factor("Coupon",
//				new String[] { "C001", "C002", "C003" });
//		mp.addFactor(f1);
//		mp.addFactor(f2);
//		mp.addFactor(f3);
//
//		Engine engine = new AMEngine();
//		String testCases = new XMLTestCasesGenerator().generate(mp, engine
//				.generateTestData(mp));
//
//		System.out.println(testCases);
		
		
		// Test cases String received from Web Service
		String testCases = "<?xml version=\"1.0\"?>"
				+ "<testcases>"
				+ "<factor>Level</factor>"
				+ "<factor>AccountType</factor><"
				+ "factor>Coupon</factor>"
				+ "<run><level>Level1</level><level>NORMAL</level><level>C001</level></run>"
				+ "<run><level>Level1</level><level>Student</level><level>C002</level></run>"
				+ "<run><level>Level1</level><level>INTERNAL</level><level>C003</level></run>"
				+ "<run><level>Level2</level><level>NORMAL</level><level>C002</level></run>"
				+ "<run><level>Level2</level><level>Student</level><level>C003</level></run>"
				+ "<run><level>Level2</level><level>INTERNAL</level><level>C001</level></run>"
				+ "<run><level>Level3</level><level>NORMAL</level><level>C003</level></run>"
				+ "<run><level>Level3</level><level>Student</level><level>C001</level></run>"
				+ "<run><level>Level3</level><level>INTERNAL</level><level>C002</level></run>"
				+ "</testcases>";
		
		// Parse XML Data to 2D String Array
		Document doc = new Builder().build(testCases, null);
		Element root = doc.getRootElement();
		Elements runs = root.getChildElements("run");
		String[][] testData = new String[runs.size()][];
		for (int i = 0; i < runs.size(); i++) {
			Element run = runs.get(i);
			Elements levels = run.getChildElements("level");
			testData[i] = new String[levels.size()];
			for (int j = 0; j < levels.size(); j++) {
				testData[i][j] = levels.get(j).getValue();
			}
		}
		
		// Generate the 2D Object array needed for Data Provider
		// Need proper type conversion
		Object[][] testDataObjects = new Object[testData.length][testData[0].length];
		for (int i = 0; i < testData.length; i++) {
			String[] row = testData[i];
			testDataObjects[i][0] = row[0];
			testDataObjects[i][1] = AccountType.valueOf(row[1]);
			testDataObjects[i][2] = row[2];
		}
		return testDataObjects;
	}
}
