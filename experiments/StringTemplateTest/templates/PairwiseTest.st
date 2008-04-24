package $packageName$;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import expect.Expectation;

import util.Converter;

$imports: {import $it$;}; separator="\n"$

public class PairwiseTest {
	@Test(dataProvider = "$dataProviderName; null="PairwiseTestingDataProvider"$")
	public void test$methodUnderTest; format="capitalize"$($args:argument(); separator=", "$) {
		Assert.assertEquals($invoker()$.$methodUnderTest$($args: {$it.name$}; separator=", "$), Converter.convertTo(Expectation.getExpectedResult("$packageName$.$classUnderTest$.$methodUnderTest$_" + $args: {$it.name$}; separator=" + \"_\" + "$), $returnType$.class)$delta()$);
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
                        $args: {testDataObjects[i][$i0$] = Converter.convertTo(row[$i0$], $it.type$.class);}; separator="\n"$
                }
                return testDataObjects;
	}
}