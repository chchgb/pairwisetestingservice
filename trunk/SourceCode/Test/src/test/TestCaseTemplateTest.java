package test;

import junit.framework.TestCase;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;
import testingngservices.testcasetemplateengine.TestCaseTemplateEngine;


public class TestCaseTemplateTest extends TestCase {
//	public void testInvokeSequence() {
//		InvokeSequence is = new InvokeSequence("src/test/bank/AccountService.java");
//		
//		assertEquals("manager", is.getFieldName("test.bank.IAccountManager"));
//		assertEquals("manager", is.getFieldName("IAccountManager"));
//		
//		//is.setScopeByMethod("double", "withdraw", new Parameter("String", "accountId"), new Parameter("double", "amount"));
//		is.setScopeByMethod("double", "withdraw");	
//		Invoke[] sequences = is.findByFieldType("test.bank.IAccountManager");
//		Invoke[] expectedSequences = new Invoke[3];
//		expectedSequences[0] = new Invoke("manager.beginTransaction()", "void");
//		expectedSequences[1] = new Invoke("manager.withdraw(accountId, amount)", "double");
//		expectedSequences[2] = new Invoke("manager.commit()", "void");
//		// System.out.println(Arrays.toString(sequences));
//		assertTrue(Arrays.equals(sequences, expectedSequences));
//		
//		String[] expectedJMockSequences = new String[4];
//		expectedJMockSequences[0] = "one (manager).beginTransaction()";
//		expectedJMockSequences[1] = "one (manager).withdraw(accountId, amount)";
//		expectedJMockSequences[2] = "will(returnValue(<NeedFilled>))";
//		expectedJMockSequences[3] = "one (manager).commit()";
//		String[] jMockSequences = is.generateJMockInvokeSequenceByFieldType("test.bank.IAccountManager");
//		// System.out.println(Arrays.toString(jMockSequences));
//		assertTrue(Arrays.equals(jMockSequences, expectedJMockSequences));
//		
//		is.setScopeByMethod("double", "transfer");	
//		sequences = is.findByFieldType("test.bank.IAccountManager");
//		expectedSequences = new Invoke[8];
//		expectedSequences[0] = new Invoke("manager.beginTransaction()", "void");
//		expectedSequences[1] = new Invoke("manager.beginTransaction()", "void");
//		expectedSequences[2] = new Invoke("manager.withdraw(accountId, amount)", "double");
//		expectedSequences[3] = new Invoke("manager.commit()", "void");
//		expectedSequences[4] = new Invoke("manager.beginTransaction()", "void");
//		expectedSequences[5] = new Invoke("manager.deposit(accountId, amount)", "double");
//		expectedSequences[6] = new Invoke("manager.commit()", "void");
//		expectedSequences[7] = new Invoke("manager.commit()", "void");
//		// System.out.println(Arrays.toString(sequences));
//		assertTrue(Arrays.equals(sequences, expectedSequences));
//		
//		expectedJMockSequences = new String[10];
//		expectedJMockSequences[0] = "one (manager).beginTransaction()";
//		expectedJMockSequences[1] = "one (manager).beginTransaction()";
//		expectedJMockSequences[2] = "one (manager).withdraw(accountId, amount)";
//		expectedJMockSequences[3] = "will(returnValue(<NeedFilled>))";
//		expectedJMockSequences[4] = "one (manager).commit()";
//		expectedJMockSequences[5] = "one (manager).beginTransaction()";
//		expectedJMockSequences[6] = "one (manager).deposit(accountId, amount)";
//		expectedJMockSequences[7] = "will(returnValue(<NeedFilled>))";
//		expectedJMockSequences[8] = "one (manager).commit()";
//		expectedJMockSequences[9] = "one (manager).commit()";
//		jMockSequences = is.generateJMockInvokeSequenceByFieldType("test.bank.IAccountManager");
//		// System.out.println(Arrays.toString(jMockSequences));
//		assertTrue(Arrays.equals(jMockSequences, expectedJMockSequences));
//	}
	
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
		tp.addImport("test.bank.IAccountManager");
		tp.addClassToMockInstanceName("test.bank.IAccountManager", "manager");
		String[] jMockInvocations = new String[4];
		jMockInvocations[0] = "one (manager).beginTransaction()";
		jMockInvocations[1] = "one (manager).withdraw(accountId,amount)";
		jMockInvocations[2] = "will(returnValue(<NeedFilled>)";
		jMockInvocations[3] = "one (manager).commit()";
		tp.addJMockInvocationSequence("test.bank.IAccountManager", jMockInvocations);
		// tp.addClassToMockInstanceName("test.bank.AbstractAccountRepository", "repository");
		
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
            + "<factor>lower</factor><"
            + "factor>upper</factor>"
            + "<run><level>3</level><level>1</level><level>4</level></run>"
            + "<run><level>3</level><level>3</level><level>4</level></run>"
            + "<run><level>4</level><level>3</level><level>4</level></run>"
            + "</testcases>";
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("test.math");
		tp.setClassUnderTest("Range");
		tp.setMethodUnderTest("isBetween");
		tp.setStaticMethod(true);
		tp.addMethodParameter("int", "n");
		tp.addMethodParameter("int", "lower");
		tp.addMethodParameter("int", "upper");
		tp.setSingletonMethod("getInstance");
		tp.setReturnType("boolean");
		
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
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
		
		tp = new TestCaseTemplateParameter();
		tp.setPackageName("test.bookstore");
		tp.setClassUnderTest("BookStore");
		tp.addConstructorArgument("String", "PKU");
		tp.addConstructorArgument("int", "40");
		tp.setMethodUnderTest("computeDiscountedPrice");
		tp.addMethodParameter("int", "level");
		tp.addMethodParameter("AccountType", "accountType");
		tp.addMethodParameter("String", "coupon");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		assertTrue(te.generateTestNGTestCase().contains("double testResult = bookStore.computeDiscountedPrice(level, accountType, coupon)"));
		
		tp.setCheckStateMethod("getDiscountedPrice");
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
		assertTrue(te.generateTestNGTestCase().contains("bookStore.getDiscountedPrice()"));
		
		tp.addImport("test.bookstore.Logger");
		tp.addClassToMockInstanceName("test.bookstore.Logger", "logger");
		jMockInvocations = new String[3];
		jMockInvocations[0] = "one (logger).log(level)";
		jMockInvocations[1] = "one (logger).log(accountType)";
		jMockInvocations[2] = "one (logger).log(coupon)";
		tp.addJMockInvocationSequence("test.bookstore.Logger", jMockInvocations);
		
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
		tp.setPackageName("test.bank");
		tp.setClassUnderTest("AccountService");
		tp.setMethodUnderTest("withdraw");
		tp.addMethodParameter("String", "accountId");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		tp.addImport("test.bank.IAccountManager");
		tp.addClassToMockInstanceName("test.bank.IAccountManager", "manager");
		
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				"src/test/bank/AccountService.java");
		finder.setScopeByMethodSignature("double", "withdraw");
		jMockInvocations = finder.getJMockInvocations("test.bank.IAccountManager");
		tp.addJMockInvocationSequence("test.bank.IAccountManager", jMockInvocations);
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountId</factor>"
            + "<factor>amount</factor>"
            + "<run><level>A001</level><level>1000</level></run>"
            + "<run><level>A002</level><level>2000</level></run>"
            + "<run><level>A001</level><level>2000</level></run>"
            + "<run><level>A002</level><level>1000</level></run>"
            + "</testcases>";
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
		
		tp.addImport("test.bank.Logger");
		tp.addClassToMockInstanceName("test.bank.Logger", "logger");
		jMockInvocations = finder.getJMockInvocations("test.bank.Logger");
		tp.addJMockInvocationSequence("test.bank.Logger", jMockInvocations);

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
		tp.setPackageName("test.bank");
		tp.setClassUnderTest("AccountService");
		tp.setMethodUnderTest("transfer");
		finder.setScopeByMethodSignature("double", "transfer");
		tp.addMethodParameter("String", "accountIdA");
		tp.addMethodParameter("String", "accountIdB");
		tp.addMethodParameter("double", "amount");
		tp.setReturnType("double");
		tp.setDelta(0.001);
		
		tp.addImport("test.bank.IAccountManager");
		tp.addClassToMockInstanceName("test.bank.IAccountManager", "manager");
		jMockInvocations = finder.getJMockInvocations("test.bank.IAccountManager");
		tp.addJMockInvocationSequence("test.bank.IAccountManager", jMockInvocations);
		
		tp.addImport("test.bank.Logger");
		tp.addClassToMockInstanceName("test.bank.Logger", "logger");
		jMockInvocations = finder.getJMockInvocations("test.bank.Logger");
		tp.addJMockInvocationSequence("test.bank.Logger", jMockInvocations);
		
		pairwiseTestCasesXmlData = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>accountId</factor>"
            + "<factor>amount</factor>"
            + "<run><level>A001</level><level>A003</level><level>1000</level></run>"
            + "<run><level>A001</level><level>A004</level><level>2000</level></run>"
            + "<run><level>A002</level><level>A003</level><level>2000</level></run>"
            + "<run><level>A002</level><level>A004</level><level>1000</level></run>"
            + "</testcases>";
		te.setPairwiseTestCasesXmlData(pairwiseTestCasesXmlData);
		te.setTestCaseTemplateParameterXmlData(tp.toXML());
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
}
