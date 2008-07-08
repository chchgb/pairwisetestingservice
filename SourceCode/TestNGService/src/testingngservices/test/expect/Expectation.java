package testingngservices.test.expect;

import java.util.HashMap;

public class Expectation {
	
	private static HashMap<String, String> ExpectedResults = new HashMap<String, String>();
	
	static {
		ExpectedResults.put("testingngservices.test.math.Range.isBetween_3_1_4", "true");
		ExpectedResults.put("testingngservices.test.math.Range.isBetween_3_3_4", "true");
		ExpectedResults.put("testingngservices.test.math.Range.isBetween_4_3_4", "true");
		ExpectedResults.put("testingngservices.test.math.Range.isBetween_4_3_4", "true");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_1_NORMAL_C001", "85");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_1_STUDENT_C002", "60");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_1_INTERNAL_C003", "40");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_2_NORMAL_C002", "70");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_2_STUDENT_C003", "50");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_2_INTERNAL_C001", "55");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_3_NORMAL_C003", "50");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_3_STUDENT_C001", "65");
		ExpectedResults.put("testingngservices.test.bookstore.BookStore.computeDiscountedPrice_3_INTERNAL_C002", "50");
		ExpectedResults.put("testingngservices.test.bank.AccountService.withdraw_A001_1000.0", "9000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.withdraw_A002_2000.0", "8000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.withdraw_A001_2000.0", "8000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.withdraw_A002_1000.0", "9000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.transfer_A001_A003_1000.0", "11000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.transfer_A001_A004_2000.0", "12000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.transfer_A002_A003_2000.0", "12000");
		ExpectedResults.put("testingngservices.test.bank.AccountService.transfer_A002_A004_1000.0", "11000");

		ExpectedResults.put("testingngservices.test.bank.AccountService2.transfer_Account_A001_9000.0_Andy_Account_A002_11000.0_Alex_1000.0", "11000");
		ExpectedResults.put("testingngservices.test.bank.AccountService2.transfer_Account_A001_18000.0_Andy_Account_A002_12000.0_Alex_2000.0", "11000");
	}
	
	public static String getExpectedResult(String key) {
		return ExpectedResults.get(key);
	}
}
