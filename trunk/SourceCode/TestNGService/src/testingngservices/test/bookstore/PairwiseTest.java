package testingngservices.test.bookstore;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.util.Converter;

import org.testng.annotations.BeforeMethod;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;
import testingngservices.test.bookstore.Logger;

public class PairwiseTest extends MockObjectTestCase {
    @BeforeMethod
    public void setUp() {
        setImposteriser(ClassImposteriser.INSTANCE);
    }

    @Test(dataProvider = "PairwiseTestingDataProvider")
    public void testComputeDiscountedPrice(final int level, final testingngservices.test.bookstore.AccountType accountType, final java.lang.String coupon, final double expected) {
        final Logger logger = mock(Logger.class, "MockLogger_" + level + "_" + accountType + "_" + coupon);
        checking(new Expectations() {{
        	one (logger).log(level);
        	one (logger).log(accountType);
        	one (logger).log(coupon);
        }});
        BookStore bookStore = new BookStore(Converter.convertTo("PKU", String.class), Converter.convertTo("40", int.class));
        bookStore.setLogger(logger);
        double testResult = bookStore.computeDiscountedPrice(level, accountType, coupon);
        verify();
        Assert.assertEquals(testResult, expected);
    }

    @DataProvider(name = "PairwiseTestingDataProvider")
    public Object[][] rangeData() throws Exception {
        XStreamMethodUnderTestXMLHelper xmlHelper = new XStreamMethodUnderTestXMLHelper();
        String testCases = "<?xml version=\"1.0\"?><testcases><factor>Level</factor><factor>AccountType</factor><factor>Coupon</factor><run><level>1</level><level>NORMAL</level><level>C001</level><expected>85</expected></run><run><level>1</level><level>STUDENT</level><level>C002</level><expected>60</expected></run><run><level>1</level><level>INTERNAL</level><level>C003</level><expected>40</expected></run><run><level>2</level><level>NORMAL</level><level>C002</level><expected>70</expected></run><run><level>2</level><level>STUDENT</level><level>C003</level><expected>50</expected></run><run><level>2</level><level>INTERNAL</level><level>C001</level><expected>55</expected></run><run><level>3</level><level>NORMAL</level><level>C003</level><expected>50</expected></run><run><level>3</level><level>STUDENT</level><level>C001</level><expected>65</expected></run><run><level>3</level><level>INTERNAL</level><level>C002</level><expected>50</expected></run></testcases>";
        String methodUnderTestXmlData = "<pairwisetesting.complex.MethodUnderTest><paramlist><pairwisetesting.complex.SimpleParameter><type>int</type><name>level</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>testingngservices.test.bookstore.AccountType</type><name>accountType</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter><pairwisetesting.complex.SimpleParameter><type>java.lang.String</type><name>coupon</name><fullNamePrefix></fullNamePrefix><depth>0</depth></pairwisetesting.complex.SimpleParameter></paramlist><returnValueParameter class=\"pairwisetesting.complex.SimpleParameter\"><type>double</type><name>ReturnValue</name><fullNamePrefix></fullNamePrefix><depth>0</depth></returnValueParameter><name>computeDiscountedPrice</name></pairwisetesting.complex.MethodUnderTest>";

        // Parse XML Data to 2D String Array
        Document doc = new Builder().build(testCases, null);
        Element root = doc.getRootElement();
        Elements runs = root.getChildElements("run");
        String[][] testData = new String[runs.size()][];
        for (int i = 0; i < runs.size(); i++) {
            Element run = runs.get(i);
            Elements levels = run.getChildElements("level");
            Elements expects = run.getChildElements("expected");
            testData[i] = new String[levels.size() + expects.size()];
            for (int j = 0; j < levels.size(); j++) {
                testData[i][j] = levels.get(j).getValue();
            }
            for (int j = 0; j < expects.size(); j++) {
                testData[i][levels.size() + j] = expects.get(j).getValue();
            }
        }

        // Generate the 2D Object array needed for Data Provider
        // Need proper type conversion
        Object[][] testDataObjects = new Object[testData.length][];
        for (int i = 0; i < testData.length; i++) {
            testDataObjects[i] = xmlHelper.assign(methodUnderTestXmlData, testData[i]);
        }
        return testDataObjects;
    }
}
