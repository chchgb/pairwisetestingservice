package testingngservices.test.math;

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
	public void testIsBetween(final int n, final int lower, final int upper, final boolean expected) {
        Assert.assertEquals(Range.isBetween(n, lower, upper), expected);

	}

	@DataProvider(name = "PairwiseTestingDataProvider")
	public Object[][] rangeData() throws Exception {
        XStreamMethodUnderTestXMLHelper xmlHelper = new XStreamMethodUnderTestXMLHelper();
        String testCases = "<?xml version='1.0'?><testcases><factor>n</factor><factor>lower</factor><factor>upper</factor><run><level>3</level><level>1</level><level>4</level><expected>true</expected></run><run><level>3</level><level>3</level><level>4</level><expected>true</expected></run><run><level>4</level><level>3</level><level>4</level><expected>true</expected></run></testcases>";
        String methodUnderTestXmlData = "<pairwisetesting.complex.MethodUnderTest><returnType>boolean</returnType><name>isBetween</name><paramlist><pairwisetesting.complex.SimpleParameter><type>int</type><name>n</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>int</type><name>lower</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>int</type><name>upper</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter></paramlist></pairwisetesting.complex.MethodUnderTest>";

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
            testDataObjects[i][inputTestObjects.length] = Converter.convertTo(testData[i][testData[i].length - 1], boolean.class);
        }
        return testDataObjects;
	}
}
