package test.bookstore;
 
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
 
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
 
import pairwisetesting.util.Converter;
 
import org.testng.annotations.BeforeMethod;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.lib.legacy.ClassImposteriser;
import test.bookstore.Logger;
 
public class PairwiseTest extends MockObjectTestCase {
         @BeforeMethod
         public void setUp() {
                  setImposteriser(ClassImposteriser.INSTANCE);
         }
 
         @Test(dataProvider = "PairwiseTestingDataProvider")
         public void testComputeDiscountedPrice(final int level, final AccountType accountType, final String coupon, final double expected) {
                  final Logger logger = mock(Logger.class, "MockLogger_" + level + "_" + accountType + "_" + coupon);
                  checking(new Expectations() {{
                          one (logger).log(level);
                          one (logger).log(accountType);
                          one (logger).log(coupon);
                  }});
                  BookStore bookStore = new BookStore();
                  bookStore.setLogger(logger);
                  double testResult = bookStore.computeDiscountedPrice(level, accountType, coupon);
                  verify();
                  Assert.assertEquals(testResult, expected, 0.0010);
         }
 
         @DataProvider(name = "PairwiseTestingDataProvider")
         public Object[][] rangeData() throws Exception {
                String testCases = "<?xml version='1.0'?><testcases><factor>level</factor><factor>accountType</factor><factor>coupon</factor><run><level>1</level><level>NORMAL</level><level>C001</level><expected>85</expected></run><run><level>1</level><level>STUDENT</level><level>C002</level><expected>0</expected></run><run><level>1</level><level>INTERNAL</level><level>C003</level><expected>0</expected></run><run><level>2</level><level>NORMAL</level><level>C002</level><expected>0</expected></run><run><level>2</level><level>STUDENT</level><level>C003</level><expected>0</expected></run><run><level>2</level><level>INTERNAL</level><level>C001</level><expected>0</expected></run><run><level>3</level><level>NORMAL</level><level>C003</level><expected>0</expected></run><run><level>3</level><level>STUDENT</level><level>C001</level><expected>0</expected></run><run><level>3</level><level>INTERNAL</level><level>C002</level><expected>0</expected></run></testcases>";
                
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
                        Element expectedElement = run.getFirstChildElement("expected");
                        testData[i][levels.size()] = expectedElement.getValue();
                }
                
                // Generate the 2D Object array needed for Data Provider
                // Need proper type conversion
                Object[][] testDataObjects = new Object[testData.length][testData[0].length];
                for (int i = 0; i < testData.length; i++) {
                        String[] row = new String[testData[i].length - 1];
                        System.arraycopy( testData[i], 0, row, 0, row.length);
                        testDataObjects[i][0] = Converter.convertTo(row[0], int.class);
                        testDataObjects[i][1] = Converter.convertTo(row[1], AccountType.class);
                        testDataObjects[i][2] = Converter.convertTo(row[2], String.class);
                        testDataObjects[i][row.length] = Converter.convertTo(testData[i][row.length], double.class);
                }
                return testDataObjects;
         }
}
