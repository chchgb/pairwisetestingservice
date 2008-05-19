package test.bank;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.expect.Expectation;
import pairwisetesting.util.Converter;

import org.testng.annotations.BeforeMethod;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;
import test.bank.IAccountManager;
import test.bank.Logger;

public class PairwiseTest extends MockObjectTestCase {
	@BeforeMethod
	public void setUp() {
		setImposteriser(ClassImposteriser.INSTANCE);
	}

	@Test(dataProvider = "PairwiseTestingDataProvider")
	public void testTransfer(final String accountIdA, final String accountIdB, final double amount) {
		final IAccountManager manager = mock(IAccountManager.class, "MockIAccountManager_" + accountIdA + "_" + accountIdB + "_" + amount);
		final Logger logger = mock(Logger.class, "MockLogger_" + accountIdA + "_" + accountIdB + "_" + amount);
		checking(new Expectations() {{
			one (manager).beginTransaction();
			one (manager).beginTransaction();
			one (manager).withdraw(accountIdA,amount);
			will(returnValue(10000 - amount));
			one (manager).commit();
			one (manager).beginTransaction();
			one (manager).deposit(accountIdB,amount);
			will(returnValue(10000 + amount));
			one (manager).commit();
			one (manager).commit();
			one (logger).log(accountIdA);
			one (logger).log(amount);
			one (logger).log(accountIdB);
			one (logger).log(amount);
		}});
		AccountService accountService = new AccountService();
		accountService.setIAccountManager(manager);
		accountService.setLogger(logger);
		double testResult = accountService.transfer(accountIdA, accountIdB, amount);
		verify();
		Assert.assertEquals(testResult, Converter.convertTo(Expectation.getExpectedResult("test.bank.AccountService.transfer_" + accountIdA + "_" + accountIdB + "_" + amount), double.class), 0.0010);
	}

	@DataProvider(name = "PairwiseTestingDataProvider")
	public Object[][] rangeData() throws Exception {
                String testCases = "<?xml version='1.0'?><testcases><factor>accountId</factor><factor>amount</factor><run><level>A001</level><level>A003</level><level>1000</level></run><run><level>A001</level><level>A004</level><level>2000</level></run><run><level>A002</level><level>A003</level><level>2000</level></run><run><level>A002</level><level>A004</level><level>1000</level></run></testcases>";
                
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
                        testDataObjects[i][1] = Converter.convertTo(row[1], String.class);
                        testDataObjects[i][2] = Converter.convertTo(row[2], double.class);
                }
                return testDataObjects;
	}
}
