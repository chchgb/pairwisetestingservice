package test.expect;

import java.util.HashMap;

public class Expectation {
	
	private static HashMap<String, String> ExpectedResults = new HashMap<String, String>();
	
	static {
		ExpectedResults.put("test.math.Range.isBetween_3_1_4", "true");
		ExpectedResults.put("test.math.Range.isBetween_3_3_4", "true");
		ExpectedResults.put("test.math.Range.isBetween_4_3_4", "true");
		ExpectedResults.put("test.math.Range.isBetween_4_3_4", "true");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_1_NORMAL_C001", "85");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_1_STUDENT_C002", "60");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_1_INTERNAL_C003", "40");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_2_NORMAL_C002", "70");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_2_STUDENT_C003", "50");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_2_INTERNAL_C001", "55");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_3_NORMAL_C003", "50");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_3_STUDENT_C001", "65");
		ExpectedResults.put("test.bookstore.BookStore.computeDiscountedPrice_3_INTERNAL_C002", "50");
		ExpectedResults.put("test.bank.AccountService.withdraw_A001_1000.0", "9000");
		ExpectedResults.put("test.bank.AccountService.withdraw_A002_2000.0", "8000");
		ExpectedResults.put("test.bank.AccountService.withdraw_A001_2000.0", "8000");
		ExpectedResults.put("test.bank.AccountService.withdraw_A002_1000.0", "9000");
		
	}
	
	public static String getExpectedResult(String key) {
		return ExpectedResults.get(key);
	}
}
