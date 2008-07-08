package testingngservices.test.bank;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.util.Converter;


public class PairwiseTest  {

	@Test(dataProvider = "PairwiseTestingDataProvider")
	public void testTransfer(final testingngservices.test.bank.Account accountA, final testingngservices.test.bank.Account accountB, final double amount, final double expected) {
        AccountService2 accountService2 = new AccountService2();
        double testResult = accountService2.transfer(accountA, accountB, amount);
        Assert.assertEquals(testResult, expected);
	}

	@DataProvider(name = "PairwiseTestingDataProvider")
	public Object[][] rangeData() throws Exception {
        XStreamMethodUnderTestXMLHelper xmlHelper = new XStreamMethodUnderTestXMLHelper();
        String testCases = "<?xml version='1.0'?><testcases><factor>accountA.id</factor><factor>accountA.balance</factor><factor>accountA.name</factor><factor>accountB.id</factor><factor>accountB.balance</factor><factor>accountB.name</factor><factor>amount</factor><run><level>A001</level><level>10000</level><level>Andy</level><level>A002</level><level>10000</level><level>Alex</level><level>1000</level><expected>11000</expected></run><run><level>A001</level><level>20000</level><level>Andy</level><level>A002</level><level>10000</level><level>Alex</level><level>2000</level><expected>12000</expected></run></testcases>";
        String methodUnderTestXmlData = "<pairwisetesting.complex.MethodUnderTest><returnType>double</returnType><name>transfer</name><paramlist><pairwisetesting.complex.ComplexParameter><type>testingngservices.test.bank.Account</type><name>accountA</name><fullNamePrefix></fullNamePrefix><depth>0</depth><children><pairwisetesting.complex.SimpleParameter><type>java.lang.String</type><name>id</name><fullNamePrefix>accountA.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>double</type><name>balance</name><fullNamePrefix>accountA.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>java.lang.String</type><name>name</name><fullNamePrefix>accountA.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter></children></pairwisetesting.complex.ComplexParameter><pairwisetesting.complex.ComplexParameter><type>testingngservices.test.bank.Account</type><name>accountB</name><fullNamePrefix></fullNamePrefix><depth>0</depth><children><pairwisetesting.complex.SimpleParameter><type>java.lang.String</type><name>id</name><fullNamePrefix>accountB.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>double</type><name>balance</name><fullNamePrefix>accountB.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>java.lang.String</type><name>name</name><fullNamePrefix>accountB.</fullNamePrefix><depth>1</depth></pairwisetesting.complex.SimpleParameter></children></pairwisetesting.complex.ComplexParameter><pairwisetesting.complex.SimpleParameter><type>double</type><name>amount</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter></paramlist></pairwisetesting.complex.MethodUnderTest>";

        // Parse XML Data to 2D String Array
        Document doc = new Builder().build(testCases, null);
        Element root = doc.getRootElement();
        Elements runs = root.getChildElements("run");
        String[][] testData = new String[runs.size()][];
        for (int i = 0; i < runs.size(); i++) {
            Element run = runs.get(i);
            Elements levels = run.getChildElements("level");
            testData[i] = new String[levels.size() + 1];
            for (int j = 0; j < levels.size(); j++) {
                testData[i][j] = levels.get(j).getValue();
            }
            Element expected = run.getFirstChildElement("expected");
            testData[i][levels.size()] = expected.getValue();
        }

        // Generate the 2D Object array needed for Data Provider
        // Need proper type conversion
        Object[][] testDataObjects = new Object[testData.length][];
        for (int i = 0; i < testData.length; i++) {
            Object[] inputTestObjects = xmlHelper.assign(methodUnderTestXmlData, testData[i]);
            testDataObjects[i] = new Object[inputTestObjects.length + 1];
            for (int j = 0; j < inputTestObjects.length; j++) {
                testDataObjects[i][j] = inputTestObjects[j];
            }
            testDataObjects[i][inputTestObjects.length] = Converter.convertTo(testData[i][testData[i].length - 1], double.class);
        }
        return testDataObjects;
	}
}
