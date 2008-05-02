package $packageName$;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pairwisetesting.test.expect.Expectation;
import pairwisetesting.util.Converter;

$if(needMock)$
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
$endif$

$imports: {import $it$;}; separator="\n"$

public class PairwiseTest $if(needMock)$extends MockObjectTestCase$endif$ {
	@Test(dataProvider = "$dataProviderName; null="PairwiseTestingDataProvider"$")
	public void test$methodUnderTest; format="capitalize"$($params:parameter(); separator=", "$) {
		$if(isStaticMethod)$
		Assert.assertEquals($classUnderTest$.$invokeMethodUnderTest()$, $expected()$);
		$elseif(isSingleton)$
		Assert.assertEquals($classUnderTest$.$singletonMethod; null="getInstance"$().$invokeMethodUnderTest()$, $expected()$);
		$else$
		$if(needMock)$$createMockObjects()$$endif$
		$instanceClassUnderTest()$
		$if(needMock)$$injectMockObjects()$$endif$
		$if(checkStateMethod)$
		$classUnderTest; format="camelize"$.$invokeMethodUnderTest()$;
		Assert.assertEquals($classUnderTest; format="camelize"$.$checkStateMethod$(), $expected()$);
		$else$
		$returnType$ testResult = $classUnderTest; format="camelize"$.$invokeMethodUnderTest()$;
		Assert.assertEquals(testResult, $expected()$);
		$endif$
		$endif$
	}

	@DataProvider(name = "$dataProviderName; null="PairwiseTestingDataProvider"$")
	public Object[][] rangeData() throws Exception {
                String testCases = "$testCases$";
                
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
                        $params: {testDataObjects[i][$i0$] = Converter.convertTo(row[$i0$], $it.type$.class);}; separator="\n"$
                }
                return testDataObjects;
	}
}