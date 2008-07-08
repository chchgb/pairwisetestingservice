package testingngservices.test;

import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import testingngservices.testcasetemplate.MethodSignatureFinder;
import testingngservices.testcasetemplate.MethodUnderTestFinder;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;
import testingngservices.testcasetemplate.core.MethodSignature;
import testingngservices.testcasetemplate.core.Parameter;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngine;


public class TestCaseTemplateTest extends TestCase {
	
	private String sourceFilePath1;
	private String sourceFilePath2;
	private String sourceFilePath3;
	
	protected void setUp() throws Exception {
		sourceFilePath1 = "src/testingngservices/test/bank/AccountService.java";
		sourceFilePath2 = "src/testingngservices/test/math/Range.java";
		sourceFilePath3 = "src/testingngservices/test/bookstore/BookStore.java";
	}
	
	public void testTestCaseTemplate() throws Exception {
		TestCaseTemplateParameter tp = new TestCaseTemplateParameter();
		assertFalse(tp.isSingleton());
		assertFalse(tp.isStaticMethod());
		assertFalse(tp.hasCheckStateMethod());
		assertFalse(tp.hasConstructorArguments());
		assertFalse(tp.hasDelta());
		assertFalse(tp.hasClassesToMock());
		
		tp.setPackageName("math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		tp.setStaticMethod(true);
		tp.setReturnType("boolean");
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.addConstructorArgument("int", "1");
		tp.addConstructorArgument("String", "Tom");
		tp.setSingletonMethod("getInstance");
		tp.setCheckStateMethod("getComputeResult");
		tp.setDelta(0.2);
		tp.addImport("java.io.*");
		tp.addImport("java.net.*");
		tp.addImport("testingngservices.test.bank.IAccountManager");
		tp.addClassToMockInstanceName("testingngservices.test.bank.IAccountManager", "manager");
		String[] jMockInvocations = new String[4];
		jMockInvocations[0] = "one (manager).beginTransaction()";
		jMockInvocations[1] = "one (manager).withdraw(accountId,amount)";
		jMockInvocations[2] = "will(returnValue(<NeedFilled>)";
		jMockInvocations[3] = "one (manager).commit()";
		tp.addJMockInvocationSequence("testingngservices.test.bank.IAccountManager", jMockInvocations);
		// tp.addClassToMockInstanceName("testingngservices.test.bank.AbstractAccountRepository", "repository");
		
		assertTrue(tp.isSingleton());
		assertTrue(tp.isStaticMethod());
		assertTrue(tp.hasCheckStateMethod());
		assertTrue(tp.hasConstructorArguments());
		assertEquals(2, tp.getConstructorArguments().length);
		assertEquals(3, tp.getMethodParameters().length);
		assertEquals(3, tp.getImports().length);
		assertTrue(tp.hasClassesToMock());
		assertTrue(tp.hasDelta());
		// System.out.println(tp.toXML());
		assertNotNull(tp.toXML());
		
		TestCaseTemplateParameter tp2 = new TestCaseTemplateParameter(tp.toXML());
		// System.out.println(tp2.toXML());
		assertEquals(tp, tp2);
		
		String pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		TestCaseTemplateEngine te = new TestCaseTemplateEngine();
		te.setTemplateDir("templates");
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertNotNull(te.generateTestNGTestCase());
		
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>n</factor>"
            + "<factor>lower</factor>"
            + "<factor>upper</factor>"
            + "<run><level>3</level><level>1</level><level>4</level><expected>true</expected></run>"
            + "<run><level>3</level><level>3</level><level>4</level><expected>true</expected></run>"
            + "<run><level>4</level><level>3</level><level>4</level><expected>true</expected></run>"
            + "</testcases>";
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("testingngservices.test.math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		tp.setStaticMethod(true);
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.setSingletonMethod("getInstance");
		tp.setReturnType("boolean");
		
		String sourceFilePath = "src/testingngservices/test/math/Range.java";
		String className = "testingngservices.test.math.Range";
		MethodUnderTestFinder mutFinder = new MethodUnderTestFinder(sourceFilePath, className);
		MethodUnderTest mut = mutFinder.getMethodUnderTest("boolean", "isBetween");
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		te.setMethodUnderTestXmlData(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains("Range.isBetween"));
		tp.setStaticMethod(false);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		assertTrue(te.generateTestNGTestCase().contains("Range.getInstance().isBetween"));
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level><expected>85</expected></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level><expected>60</expected></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level><expected>40</expected></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level><expected>70</expected></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level><expected>50</expected></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level><expected>55</expected></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level><expected>50</expected></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level><expected>65</expected></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level><expected>50</expected></run>"
            + "</testcases>";
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("testingngservices.test.bookstore");
		tp.setClassUnderTest("BookStore");
		tp.addConstructorArgument("String", "PKU");
		tp.addConstructorArgument("int", "40");
		tp.setMethodUnderTest("computeDiscountedPrice");
		tp.addMethodParameter("int", "level");
		tp.addMethodParameter("AccountType", "accountType");
		tp.addMethodParameter("String", "coupon");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		sourceFilePath = "src/testingngservices/test/bookstore/BookStore.java";
		className = "testingngservices.test.bookstore.BookStore";
		mutFinder = new MethodUnderTestFinder(sourceFilePath, className);
		mut = mutFinder.getMethodUnderTest("double", "computeDiscountedPrice");
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		te.setMethodUnderTestXmlData(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		assertTrue(te.generateTestNGTestCase().contains("double testResult = bookStore.computeDiscountedPrice(level, accountType, coupon)"));
		
		tp.setCheckStateMethod("getDiscountedPrice");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		assertTrue(te.generateTestNGTestCase().contains("bookStore.getDiscountedPrice()"));
		
		tp.addImport("testingngservices.test.bookstore.Logger");
		tp.addClassToMockInstanceName("testingngservices.test.bookstore.Logger", "logger");
		jMockInvocations = new String[3];
		jMockInvocations[0] = "one (logger).log(level)";
		jMockInvocations[1] = "one (logger).log(accountType)";
		jMockInvocations[2] = "one (logger).log(coupon)";
		tp.addJMockInvocationSequence("testingngservices.test.bookstore.Logger", jMockInvocations);
		
		tp.setCheckStateMethod("");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		String testNGTestCase = te.generateTestNGTestCase();
		assertTrue(testNGTestCase.contains(jMockInvocations[0]));
		assertTrue(testNGTestCase.contains(jMockInvocations[1]));
		assertTrue(testNGTestCase.contains(jMockInvocations[2]));
		
		tp.setCheckStateMethod("getDiscountedPrice");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		testNGTestCase = te.generateTestNGTestCase();
		assertTrue(testNGTestCase.contains(jMockInvocations[0]));
		assertTrue(testNGTestCase.contains(jMockInvocations[1]));
		assertTrue(testNGTestCase.contains(jMockInvocations[2]));
		assertTrue(testNGTestCase.contains("bookStore.getDiscountedPrice()"));
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("testingngservices.test.bank");
		tp.setClassUnderTest("AccountService");
		tp.setMethodUnderTest("withdraw");
		tp.addMethodParameter("String", "accountId");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		tp.addImport("testingngservices.test.bank.IAccountManager");
		tp.addClassToMockInstanceName("testingngservices.test.bank.IAccountManager", "manager");
		
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				"src/testingngservices/test/bank/AccountService.java");
		finder.setScopeByMethodSignature("double", "withdraw");
		jMockInvocations = finder.getJMockInvocations("testingngservices.test.bank.IAccountManager");
		tp.addJMockInvocationSequence("testingngservices.test.bank.IAccountManager", jMockInvocations);
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountId</factor>"
            + "<factor>amount</factor>"
            + "<run><level>A001</level><level>1000</level><expected>9000</expected></run>"
            + "<run><level>A002</level><level>2000</level><expected>8000</expected></run>"
            + "<run><level>A001</level><level>2000</level><expected>8000</expected></run>"
            + "<run><level>A002</level><level>1000</level><expected>9000</expected></run>"
            + "</testcases>";
		
		sourceFilePath = "src/testingngservices/test/bank/AccountService.java";
		className = "testingngservices.test.bank.AccountService";
		mutFinder = new MethodUnderTestFinder(sourceFilePath, className);
		mut = mutFinder.getMethodUnderTest("double", "withdraw");
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setMethodUnderTestXmlData(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		
		String[] expectedJMockSequences = new String[4];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).withdraw(accountId,amount)";
		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[3] = "one (manager).commit()";
		testNGTestCase = te.generateTestNGTestCase();
		assertTrue(testNGTestCase.contains(expectedJMockSequences[0]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[1]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[2]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[3]));
		
		tp.addImport("testingngservices.test.bank.Logger");
		tp.addClassToMockInstanceName("testingngservices.test.bank.Logger", "logger");
		jMockInvocations = finder.getJMockInvocations("testingngservices.test.bank.Logger");
		tp.addJMockInvocationSequence("testingngservices.test.bank.Logger", jMockInvocations);

		expectedJMockSequences = new String[6];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).withdraw(accountId,amount)";
		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[3] = "one (manager).commit()";
		expectedJMockSequences[4] = "one (logger).log(accountId)";
		expectedJMockSequences[5] = "one (logger).log(amount)";
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(te.generateTestNGTestCase());
		testNGTestCase = te.generateTestNGTestCase();
		assertTrue(testNGTestCase.contains(expectedJMockSequences[0]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[1]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[2]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[3]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[4]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[5]));
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("testingngservices.test.bank");
		tp.setClassUnderTest("AccountService");
		tp.setMethodUnderTest("transfer");
		finder.setScopeByMethodSignature("double", "transfer");
		tp.addMethodParameter("String", "accountIdA");
		tp.addMethodParameter("String", "accountIdB");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		tp.addImport("testingngservices.test.bank.IAccountManager");
		tp.addClassToMockInstanceName("testingngservices.test.bank.IAccountManager", "manager");
		jMockInvocations = finder.getJMockInvocations("testingngservices.test.bank.IAccountManager");
		tp.addJMockInvocationSequence("testingngservices.test.bank.IAccountManager", jMockInvocations);
		
		tp.addImport("testingngservices.test.bank.Logger");
		tp.addClassToMockInstanceName("testingngservices.test.bank.Logger", "logger");
		jMockInvocations = finder.getJMockInvocations("testingngservices.test.bank.Logger");
		tp.addJMockInvocationSequence("testingngservices.test.bank.Logger", jMockInvocations);
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountId</factor>"
            + "<factor>amount</factor>"
            + "<run><level>A001</level><level>A003</level><level>1000</level><expected>11000</expected></run>"
            + "<run><level>A001</level><level>A004</level><level>2000</level><expected>12000</expected></run>"
            + "<run><level>A002</level><level>A003</level><level>2000</level><expected>12000</expected></run>"
            + "<run><level>A002</level><level>A004</level><level>1000</level><expected>11000</expected></run>"
            + "</testcases>";
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		mut = mutFinder.getMethodUnderTest("double", "transfer");
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setMethodUnderTestXmlData(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		// System.out.println(te.generateTestNGTestCase());
		
		expectedJMockSequences = new String[14];
		expectedJMockSequences[0] = "one (manager).beginTransaction()";
		expectedJMockSequences[1] = "one (manager).beginTransaction()";
		expectedJMockSequences[2] = "one (manager).withdraw(accountId,amount)";
		expectedJMockSequences[3] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[4] = "one (manager).commit()";
		expectedJMockSequences[5] = "one (manager).beginTransaction()";
		expectedJMockSequences[6] = "one (manager).deposit(accountId,amount)";
		expectedJMockSequences[7] = "will(returnValue(<NeedFilled>))";
		expectedJMockSequences[8] = "one (manager).commit()";
		expectedJMockSequences[9] = "one (manager).commit()";
		expectedJMockSequences[10] = "one (logger).log(accountId)";
		expectedJMockSequences[11] = "one (logger).log(amount)";
		expectedJMockSequences[12] = "one (logger).log(accountId)";
		expectedJMockSequences[13] = "one (logger).log(amount)";
		testNGTestCase = te.generateTestNGTestCase();
		assertTrue(testNGTestCase.contains(expectedJMockSequences[0]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[1]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[2]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[3]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[4]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[5]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[6]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[7]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[8]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[9]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[10]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[11]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[12]));
		assertTrue(testNGTestCase.contains(expectedJMockSequences[13]));
	}
	
	public void testMethodSignatureFinder() {
		MethodSignatureFinder finder = new MethodSignatureFinder(sourceFilePath1);
		MethodSignature ms = finder.getMethodSignature("double", "transfer");
		assertEquals("double", ms.getReturnTypeName());
		assertEquals("transfer", ms.getMethodName());
		Parameter[] expectedPrams = new Parameter[3];
		expectedPrams[0] = new Parameter("String", "accountIdA");
		expectedPrams[1] = new Parameter("String", "accountIdB");
		expectedPrams[2] = new Parameter("double", "amount");
		// System.out.println(ms);
		assertTrue("They should be equal", Arrays.equals(expectedPrams, ms.getParameters()));
		
		finder = new MethodSignatureFinder(sourceFilePath2);
		ms = finder.getMethodSignature("boolean", "isBetween");
		assertEquals("boolean", ms.getReturnTypeName());
		assertEquals("isBetween", ms.getMethodName());
		expectedPrams = new Parameter[3];
		expectedPrams[0] = new Parameter("int", "n");
		expectedPrams[1] = new Parameter("int", "lower");
		expectedPrams[2] = new Parameter("int", "upper");
		// System.out.println(ms);
		assertTrue("They should be equal", Arrays.equals(expectedPrams, ms.getParameters()));
		
		finder = new MethodSignatureFinder(sourceFilePath3);
		ms = finder.getMethodSignature("double", "computeDiscountedPrice");
		assertEquals("double", ms.getReturnTypeName());
		assertEquals("computeDiscountedPrice", ms.getMethodName());
		expectedPrams = new Parameter[3];
		expectedPrams[0] = new Parameter("int", "level");
		expectedPrams[1] = new Parameter("AccountType", "accountType");
		expectedPrams[2] = new Parameter("String", "coupon");
		// System.out.println(ms);
		assertTrue("They should be equal", Arrays.equals(expectedPrams, ms.getParameters()));
	}
	
	public void testMethodUnderTestFinder() {
		String sourceFilePath = "src/testingngservices/test/bank/AccountService.java";
		String className = "testingngservices.test.bank.AccountService";
		MethodUnderTestFinder finder = new MethodUnderTestFinder(sourceFilePath, className);
		MethodUnderTest mut = finder.getMethodUnderTest("double", "transfer");
		assertEquals("transfer", mut.getName());
		assertEquals("double", mut.getReturnType());
		assertEquals(3, mut.getParameters().length);
		assertEquals("java.lang.String", mut.getParameters()[0].getType());
		assertEquals("java.lang.String", mut.getParameters()[1].getType());
		assertEquals("double", mut.getParameters()[2].getType());
		assertEquals("accountIdA", mut.getParameters()[0].getName());
		assertEquals("accountIdB", mut.getParameters()[1].getName());
		assertEquals("amount", mut.getParameters()[2].getName());
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		
		mut = finder.getMethodUnderTest("void", "transfer2");
		assertEquals("transfer2", mut.getName());
		assertEquals("void", mut.getReturnType());
		assertEquals(3, mut.getParameters().length);
		assertEquals("testingngservices.test.bank.Account", mut.getParameters()[0].getType());
		assertEquals("testingngservices.test.bank.Account", mut.getParameters()[1].getType());
		assertEquals("double", mut.getParameters()[2].getType());
		assertEquals("accountA", mut.getParameters()[0].getFullName());
		assertEquals("accountB", mut.getParameters()[1].getFullName());
		assertEquals("amount", mut.getParameters()[2].getFullName());
		ComplexParameter cp1 = (ComplexParameter)mut.getParameters()[0];
		assertEquals(3, cp1.getChildren().length);
		assertEquals("java.lang.String",cp1.getChildren()[0].getType());
		assertEquals("double", cp1.getChildren()[1].getType());
		assertEquals("java.lang.String", cp1.getChildren()[2].getType());
		assertEquals("accountA.id", cp1.getChildren()[0].getFullName());
		assertEquals("accountA.balance", cp1.getChildren()[1].getFullName());
		assertEquals("accountA.name", cp1.getChildren()[2].getFullName());
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
	}
	
	public void testTestCaseTemplateWithComplexParameter() throws Exception {
		String pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountA.id</factor>"
            + "<factor>accountA.balance</factor>"
            + "<factor>accountA.name</factor>"
            + "<factor>accountB.id</factor>"
            + "<factor>accountB.balance</factor>"
            + "<factor>accountB.name</factor>"
            + "<factor>amount</factor>"
            + "<run>" +
            		"<level>A001</level>" +
            		"<level>10000</level>" +
            		"<level>Andy</level>" +
            		"<level>A002</level>" +
            		"<level>10000</level>" +
            		"<level>Alex</level>" +
            		"<level>1000</level>" +
            		"<expected>11000</expected>"
            + "</run>"
            + "<run>" +
		    		"<level>A001</level>" +
		    		"<level>20000</level>" +
		    		"<level>Andy</level>" +
		    		"<level>A002</level>" +
		    		"<level>10000</level>" +
		    		"<level>Alex</level>" +
		    		"<level>2000</level>" +
            		"<expected>12000</expected>"
		    + "</run>"           
            + "</testcases>";
		TestCaseTemplateParameter tp = new TestCaseTemplateParameter();
		tp.setPackageName("testingngservices.test.bank");
		tp.setClassUnderTest("AccountService2");
		tp.setMethodUnderTest("transfer");
		tp.addMethodParameter("testingngservices.test.bank.Account", "accountA");
		tp.addMethodParameter("testingngservices.test.bank.Account", "accountB");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		
		String sourceFilePath = "src/testingngservices/test/bank/AccountService2.java";
		String className = "testingngservices.test.bank.AccountService2";
		MethodUnderTestFinder mutFinder = new MethodUnderTestFinder(sourceFilePath, className);
		MethodUnderTest mut = mutFinder.getMethodUnderTest("double", "transfer");
//		IParameterVisitor ppv = new PrintParameterVisitor();
//		mut.accept(ppv);
		TestCaseTemplateEngine te = new TestCaseTemplateEngine();
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		// System.out.println(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		te.setMethodUnderTestXmlData(new XStreamMethodUnderTestXMLHelper().toXML(mut));
		System.out.println(te.generateTestNGTestCase());
		assertTrue("It should contain this", te.generateTestNGTestCase().contains(
				"testTransfer(" +
					"final testingngservices.test.bank.Account accountA, " +
					"final testingngservices.test.bank.Account accountB, " +
					"final double amount, final double expected)"));
	}
}
