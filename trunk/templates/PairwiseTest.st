package $packageName$;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.util.Converter;

$if(needMock)$
import org.testng.annotations.BeforeMethod;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;
$endif$

$imports: {import $it$;}; separator="\n"$

public class PairwiseTest $if(needMock)$extends MockObjectTestCase$endif$ {
    $if(needMock)$
    @BeforeMethod
    public void setUp() {
        setImposteriser(ClassImposteriser.INSTANCE);
    }
    $endif$

	@Test(dataProvider = "$dataProviderName; null="PairwiseTestingDataProvider"$")
	public void test$methodUnderTest; format="capitalize"$($params:parameter(); separator=", "$, final $returnType$ expected) {
        $if(isStaticMethod)$
        Assert.assertEquals($classUnderTest$.$invokeMethodUnderTest()$, expected);
        $elseif(isSingleton)$
        Assert.assertEquals($classUnderTest$.$singletonMethod; null="getInstance"$().$invokeMethodUnderTest()$, expected);
        $else$
        $if(needMock)$$createMockObjects()$$endif$
        $instanceClassUnderTest()$
        $if(needMock)$$injectMockObjects()$$endif$
        $if(checkStateMethod)$
        $classUnderTest; format="camelize"$.$invokeMethodUnderTest()$;
        $if(needMock)$
        verify();
        $endif$
        Assert.assertEquals($classUnderTest; format="camelize"$.$checkStateMethod$(), expected);
        $else$
        $returnType$ testResult = $classUnderTest; format="camelize"$.$invokeMethodUnderTest()$;
        $if(needMock)$
        verify();
        $endif$
        Assert.assertEquals(testResult, expected);
        $endif$
        $endif$
	}

	@DataProvider(name = "$dataProviderName; null="PairwiseTestingDataProvider"$")
	public Object[][] rangeData() throws Exception {
        XStreamMethodUnderTestXMLHelper xmlHelper = new XStreamMethodUnderTestXMLHelper();
        String testCases = "$testCases$";
        String methodUnderTestXmlData = "$methodUnderTestXmlData$";

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
            testDataObjects[i][inputTestObjects.length] = Converter.convertTo(testData[i][testData[i].length - 1], $returnType$.class);
        }
        return testDataObjects;
	}
}