package testingngservices.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.ast.ASTFieldNameFinder;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.IFieldNameFinder;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;
import testingngservices.testcasetemplate.core.Parameter;
import testingngservices.testcasetemplate.regex.RegexFieldNameFinder;
import testingngservices.testcasetemplate.regex.RegexInvocationSequenceFinder;

public class AutoMockTest extends TestCase {

	private String sourceFilePath1;
	private String fieldClassName1;
	private String fieldSimpleClassName1;
	private String fieldClassName2;
	private String fieldSimpleClassName2;

	protected void setUp() throws Exception {
		sourceFilePath1 = "src/testingngservices/test/bank/AccountService.java";
		fieldClassName1 = "testingngservices.test.bank.IAccountManager";
		fieldSimpleClassName1 = "IAccountManager";
		fieldClassName2 = "testingngservices.test.bank.Logger";
		fieldSimpleClassName2 = "Logger";
	}
	
	public void testClassUtil() {
		assertEquals(TestCase.class, ClassUtil.getClass("junit.framework.TestCase"));
		assertEquals(int.class, ClassUtil.getClass("int"));
		assertEquals(boolean.class, ClassUtil.getClass("boolean"));
		assertEquals(double.class, ClassUtil.getClass("double"));
		assertTrue(ClassUtil.isAbstractClass(TestCase.class));
		assertFalse(ClassUtil.isAbstractClass(Integer.class));
		assertTrue(ClassUtil.isInterface(List.class));
		assertFalse(ClassUtil.isInterface(TestCase.class));
		assertEquals("boolean", ClassUtil.getReturnTypeName(
				"testingngservices.test.math.Range", "isBetween", "int", "int",
				"int"));
		assertEquals("boolean", ClassUtil.getReturnTypeName(
				"testingngservices.test.math.Range", "isBetween"));
		assertEquals("void", ClassUtil.getReturnTypeName(
				"junit.framework.TestCase", "tearDown"));
		assertEquals("java.lang.String", ClassUtil.getReturnTypeName(
				"junit.framework.TestCase", "toString"));
		assertEquals("Range", ClassUtil.getSimpleClassName("testingngservices.test.math.Range"));
		assertEquals("Range", ClassUtil.getSimpleClassName("Range"));
		
		assertTrue(ClassUtil.isSimpleType(int.class));
		assertTrue(ClassUtil.isSimpleType(String.class));
		assertTrue(ClassUtil.isSimpleType(int[].class));
		assertTrue(ClassUtil.isSimpleType(String[].class));
		
		assertTrue(ClassUtil.isSimpleType("double"));
		assertTrue(ClassUtil.isSimpleType("java.lang.String"));
		assertTrue(ClassUtil.isSimpleType("void"));
		assertTrue(ClassUtil.isSimpleType("double[]"));
		assertTrue(ClassUtil.isSimpleType("java.lang.String[]"));
		assertTrue(ClassUtil.isSimpleType("java.util.ArrayList"));
		assertTrue(ClassUtil.isSimpleType("java.util.HashMap"));
		assertTrue(ClassUtil.isSimpleType("java.util.HashSet"));
		
		assertEquals("withdraw", 
				ClassUtil.getFirstMethod(ClassUtil.getClass("testingngservices.test.bank.AccountService"), 
						"double", "withdraw").getName());
	}

	public void testExtractFieldName() {

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath1).toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);

		IFieldNameFinder finder = new ASTFieldNameFinder(unit);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new ASTFieldNameFinder(sourceFilePath1);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new RegexFieldNameFinder(sourceFilePath1);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new RegexFieldNameFinder(new TextFile(sourceFilePath1));
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

	}
	
	public void testRegexInvocationSequenceFinder() {
		InvocationSequenceFinder finder = new RegexInvocationSequenceFinder(
				sourceFilePath1);
		assertEquals("logger", finder.getFieldName(fieldClassName2));
		assertEquals("logger", finder.getFieldName(fieldSimpleClassName2));
		
		try {
			finder.getFieldName(null);
			fail("It should not accept null value");
		} catch (NullPointerException e) {
			
		}
		
		// Base case
		Invocation[] expectedInvocations = new Invocation[2];
		expectedInvocations[0] = new Invocation("logger.log(accountId)", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation("logger.log(amount)", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodSignature("double", "withdraw", new Parameter(
				"String", "accountId"), new Parameter("double", "amount"));
		finder.setScopeByMethodSignature("double", "withdraw");
		Invocation[] invocations = finder.getInvocations(fieldClassName2);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// Cascade invocations
		expectedInvocations = new Invocation[8];
		expectedInvocations[0] = new Invocation("manager.beginTransaction()", false, InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation("manager.beginTransaction()", false, InvocationCount.ONCE);
		expectedInvocations[2] = new Invocation("manager.withdraw(accountId, amount)", true, InvocationCount.ONCE);
		expectedInvocations[3] = new Invocation("manager.commit()", false, InvocationCount.ONCE);
		expectedInvocations[4] = new Invocation("manager.beginTransaction()",  false, InvocationCount.ONCE);
		expectedInvocations[5] = new Invocation("manager.deposit(accountId, amount)", true, InvocationCount.ONCE);
		expectedInvocations[6] = new Invocation("manager.commit()", false, InvocationCount.ONCE);
		expectedInvocations[7] = new Invocation("manager.commit()", false, InvocationCount.ONCE);
		finder.setScopeByMethodSignature("double", "transfer");	
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue(Arrays.equals(invocations, expectedInvocations));
		
	}

	public void testASTInvocationSequenceFinder() {
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				sourceFilePath1);
		assertEquals("logger", finder.getFieldName(fieldClassName2));
		assertEquals("logger", finder.getFieldName(fieldSimpleClassName2));
		
		// Base case
		Invocation[] expectedInvocations = new Invocation[2];
		expectedInvocations[0] = new Invocation("logger.log(accountId)", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation("logger.log(amount)", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodSignature("double", "withdraw", new Parameter(
				"String", "accountId"), new Parameter("double", "amount"));
		finder.setScopeByMethodSignature("double", "withdraw");
		Invocation[] invocations = finder.getInvocations(fieldClassName2);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// Contain return value
		expectedInvocations = new Invocation[20];
		for (int i = 0; i < 8; i++) {
			expectedInvocations[i] = new Invocation(
					"manager.withdraw(accountId,amount)", true,
					InvocationCount.ONCE);
		}
		expectedInvocations[8] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[9] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[10] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[11] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[12] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[13] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[14] = new Invocation(
				"manager.getStatus()", true,
				InvocationCount.ONCE);
		expectedInvocations[15] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[16] = new Invocation(
				"manager.getStatus()", true,
				InvocationCount.ONCE);
		expectedInvocations[17] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ONCE);
		expectedInvocations[18] = new Invocation(
				"manager.getAccounts()", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[19] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ONCE);
		
		finder.setScopeByMethodSignature("double", "checkInvocationWithReturnValue");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(expectedInvocations));
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// Cascade invocations
		expectedInvocations = new Invocation[8];
		expectedInvocations[0] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[2] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ONCE);
		expectedInvocations[3] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ONCE);
		expectedInvocations[4] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[5] = new Invocation(
				"manager.deposit(accountId,amount)", true,
				InvocationCount.ONCE);
		expectedInvocations[6] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ONCE);
		expectedInvocations[7] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodSignature("double", "transfer");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// Within loop invocation
		expectedInvocations = new Invocation[7];
		expectedInvocations[0] = new Invocation(
				"manager.withdraw(accountId,amount)", false,
				InvocationCount.ALLOWING);
		expectedInvocations[1] = new Invocation(
				"manager.withdraw(accountId,amount)", false,
				InvocationCount.ALLOWING);
		expectedInvocations[2] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[3] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[4] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[5] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[6] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ATLEAST_ONCE);
		finder.setScopeByMethodSignature("void", "checkInvocationWithLoop");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// With try-catch-finally invocation
		expectedInvocations = new Invocation[5];
		expectedInvocations[0] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[2] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[3] = new Invocation(
				"manager.rollback()", false,
				InvocationCount.IGNORING);
		expectedInvocations[4] = new Invocation(
				"manager.releaseConnection()", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodSignature("void", "checkInvocationWithTryCatchFinally");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// With if-else invocation
		expectedInvocations = new Invocation[6];
		expectedInvocations[0] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[2] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[3] = new Invocation(
				"manager.rollback()", false,
				InvocationCount.IGNORING);
		expectedInvocations[4] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[5] = new Invocation(
				"manager.releaseConnection()", false,
				InvocationCount.ALLOWING);
		finder.setScopeByMethodSignature("void", "checkInvocationWithIfElse");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// With all features
		expectedInvocations = new Invocation[17];
		expectedInvocations[0] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[2] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[3] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[4] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[5] = new Invocation(
				"manager.deposit(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[6] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[7] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[8] = new Invocation(
				"manager.rollback()", false,
				InvocationCount.IGNORING);
		expectedInvocations[9] = new Invocation(
				"manager.needClose()", true,
				InvocationCount.ONCE);
		expectedInvocations[10] = new Invocation(
				"manager.releaseConnection()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[11] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[12] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		expectedInvocations[13] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ALLOWING);
		expectedInvocations[14] = new Invocation(
				"manager.beginTransaction()", false,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[15] = new Invocation(
				"manager.deposit(accountId,amount)", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[16] = new Invocation(
				"manager.commit()", false,
				InvocationCount.ATLEAST_ONCE);
		finder.setScopeByMethodSignature("double", "checkInvocationWithAllFeatures");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(expectedInvocations));
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// With conditional return
		expectedInvocations = new Invocation[1];
		expectedInvocations[0] = new Invocation(
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ALLOWING);
		finder.setScopeByMethodSignature("double", "checkInvocationWithConditionalReturn1");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		expectedInvocations = new Invocation[1];
		expectedInvocations[0] = new Invocation(
				"manager.withdraw(accountId,amount)", false,
				InvocationCount.ALLOWING);
		finder.setScopeByMethodSignature("void", "checkInvocationWithConditionalReturn2");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// With recursive invocation
		expectedInvocations = new Invocation[4];
		expectedInvocations[0] = new Invocation(
				"manager.withdraw(accountId,amount)", false,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[1] = new Invocation(
				"manager.deposit(accountId,amount)", true,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[2] = new Invocation(
				"manager.withdraw(accountId,amount)", false,
				InvocationCount.ALLOWING);
		expectedInvocations[3] = new Invocation(
				"manager.deposit(accountId,amount)", false,
				InvocationCount.ALLOWING);
		finder.setScopeByMethodSignature("void", "checkInvocationWithRecursive1");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		expectedInvocations = new Invocation[1];
		expectedInvocations[0] = new Invocation(
				"logger.log(n)", false,
				InvocationCount.ATLEAST_ONCE);
		finder.setScopeByMethodSignature("int", "fibonacci");
		invocations = finder.getInvocations(fieldClassName2);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		expectedInvocations = new Invocation[7];
		expectedInvocations[0] = new Invocation(
				"logger.log(accountId)", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation(
				"logger.log(amount)", false,
				InvocationCount.ONCE);
		expectedInvocations[2] = new Invocation(
				"logger.log(n)", false,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[3] = new Invocation(
				"logger.log(accountId)", false,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[4] = new Invocation(
				"logger.log(amount)", false,
				InvocationCount.ATLEAST_ONCE);
		expectedInvocations[5] = new Invocation(
				"logger.log(accountId)", false,
				InvocationCount.ALLOWING);
		expectedInvocations[6] = new Invocation(
				"logger.log(amount)", false,
				InvocationCount.ALLOWING);

		finder.setScopeByMethodSignature("void", "checkInvocationWithRecursive2");
		invocations = finder.getInvocations(fieldClassName2);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
	}
	
	public void testInvocationCount() {
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ATLEAST_ONCE.plus(InvocationCount.ATLEAST_ONCE));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ATLEAST_ONCE.plus(InvocationCount.ALLOWING));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ATLEAST_ONCE.plus(InvocationCount.ONCE));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ATLEAST_ONCE.plus(InvocationCount.IGNORING));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ALLOWING.plus(InvocationCount.ONCE));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.ALLOWING.plus(InvocationCount.ATLEAST_ONCE));
		assertEquals(InvocationCount.ALLOWING, InvocationCount.ALLOWING.plus(InvocationCount.ALLOWING));
		assertEquals(InvocationCount.ALLOWING, InvocationCount.ALLOWING.plus(InvocationCount.IGNORING));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.IGNORING.plus(InvocationCount.ONCE));
		assertEquals(InvocationCount.ATLEAST_ONCE, InvocationCount.IGNORING.plus(InvocationCount.ATLEAST_ONCE));
		assertEquals(InvocationCount.ALLOWING, InvocationCount.IGNORING.plus(InvocationCount.ALLOWING));
		assertEquals(InvocationCount.IGNORING, InvocationCount.IGNORING.plus(InvocationCount.IGNORING));
	}
	
	public void testJMockInvocations() {
		
		// RegexInvocationSequenceFinder
		String[] expectedJMockInvocations = new String[10];
		expectedJMockInvocations[0] = "one (manager).beginTransaction()";
		expectedJMockInvocations[1] = "one (manager).beginTransaction()";
		expectedJMockInvocations[2] = "one (manager).withdraw(accountId, amount)";
		expectedJMockInvocations[3] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[4] = "one (manager).commit()";
		expectedJMockInvocations[5] = "one (manager).beginTransaction()";
		expectedJMockInvocations[6] = "one (manager).deposit(accountId, amount)";
		expectedJMockInvocations[7] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[8] = "one (manager).commit()";
		expectedJMockInvocations[9] = "one (manager).commit()";
		InvocationSequenceFinder regexFinder = new RegexInvocationSequenceFinder(sourceFilePath1);
		regexFinder.setScopeByMethodSignature("double", "transfer");
		String[] jMockInvocations = regexFinder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue(Arrays.equals(jMockInvocations, expectedJMockInvocations));
		
		// ASTInvocationSequenceFinder
		expectedJMockInvocations = new String[8];
		expectedJMockInvocations[0] = "one (manager).beginTransaction()";
		expectedJMockInvocations[1] = "allowing (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[3] = "allowing (manager).commit()";
		expectedJMockInvocations[4] = "ignoring (manager).rollback()";
		expectedJMockInvocations[5] = "one (manager).needClose()";
		expectedJMockInvocations[6] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[7] = "allowing (manager).releaseConnection()";
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				sourceFilePath1);
		finder.setScopeByMethodSignature("void", "checkInvocationWithIfElse");
		jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(expectedJMockInvocations));
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
		
		// Check JMock invoations order
		expectedJMockInvocations = new String[4];
		expectedJMockInvocations[0] = "one (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[1] = "one (manager).deposit(accountId,amount)";
		expectedJMockInvocations[2] = "atLeast(1).of (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[3] = "atLeast(1).of (manager).deposit(accountId,amount)";
		finder.setScopeByMethodSignature("void", "checkJMockInvocation1");
		jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(finder.getInvocations(fieldClassName1)));
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
		
		expectedJMockInvocations = new String[4];
		expectedJMockInvocations[0] = "one (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[1] = "one (manager).deposit(accountId,amount)";
		expectedJMockInvocations[2] = "allowing (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[3] = "atLeast(1).of (manager).deposit(accountId,amount)";
		finder.setScopeByMethodSignature("void", "checkJMockInvocation2");
		jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
		
		expectedJMockInvocations = new String[2];
		expectedJMockInvocations[0] = "allowing (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[1] = "allowing (manager).deposit(accountId,amount)";
		finder.setScopeByMethodSignature("void", "checkJMockInvocation3");
		jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
		
		expectedJMockInvocations = new String[8];
		expectedJMockInvocations[0] = "one (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[1] = "one (manager).deposit(accountId,amount)";
		expectedJMockInvocations[2] = "atLeast(1).of (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[3] = "allowing (manager).deposit(accountId,amount)";
		expectedJMockInvocations[4] = "allowing (manager).commit()";
		expectedJMockInvocations[5] = "ignoring (manager).rollback()";
		expectedJMockInvocations[6] = "one (manager).releaseConnection()";
		expectedJMockInvocations[7] = "one (manager).releaseConnection()";
		finder.setScopeByMethodSignature("void", "checkJMockInvocation4");
		jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(finder.getInvocations(fieldClassName1)));
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
	}

}
