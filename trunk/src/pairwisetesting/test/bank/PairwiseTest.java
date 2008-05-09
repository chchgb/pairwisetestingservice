
package pairwisetesting.test.bank;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.test.expect.Expectation;
import pairwisetesting.util.Converter;

import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import pairwisetesting.test.bank.IAccountManager;
import pairwisetesting.test.bank.Logger;

public class PairwiseTest extends MockObjectTestCase {
	@Test(dataProvider = "PairwiseTestingDataProvider")
	public void testWithdraw(final String accountId, final double amount) {
		final IAccountManager manager = mock(IAccountManager.class, "MockIAccountManager_" + accountId + "_" + amount);
		final Logger logger = mock(Logger.class, "MockLogger_" + accountId + "_" + amount);
		checking(new Expectations() {{
			atLeast(1).of (manager).beginTransaction();
			atLeast(1).of (manager).withdraw(accountId, amount);
			will(returnValue(10000 - amount));
			atLeast(1).of (manager).commit();
			atLeast(1).of (logger).log(accountId);
			atLeast(1).of (logger).log(amount);
		}});
		AccountService accountService = new AccountService();
		accountService.setIAccountManager(manager);
		accountService.setLogger(logger);
		double testResult = accountService.withdraw(accountId, amount);
		Assert.assertEquals(testResult, Converter.convertTo(Expectation.getExpectedResult("pairwisetesting.test.bank.AccountService.withdraw_" + accountId + "_" + amount), double.class), 0.0010);
	}

	@DataProvider(name = "PairwiseTestingDataProvider")
	public Object[][] rangeData() throws Exception {
                String testCases = "<?xml version='1.0'?><testcases><factor>accountId</factor><factor>amount</factor><run><level>A001</level><level>1000</level>></run><run><level>A002</level><level>2000</level></run><run><level>A001</level><level>2000</level></run><run><level>A002</level><level>1000</level></run></testcases>";
                
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
                        testDataObjects[i][0] = Converter.convertTo(row[0], String.class);
                        testDataObjects[i][1] = Converter.convertTo(row[1], double.class);
                }
                return testDataObjects;
	}
}
