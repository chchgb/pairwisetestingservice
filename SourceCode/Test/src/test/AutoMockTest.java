package test;

import java.util.Arrays;

import junit.framework.TestCase;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.MethodSignatureFinder;
import testingngservices.testcasetemplate.Parameter;
import testingngservices.testcasetemplate.ast.ASTFieldNameFinder;
import testingngservices.testcasetemplate.ast.ASTInvocationSequenceFinder;
import testingngservices.testcasetemplate.core.IFieldNameFinder;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;
import testingngservices.testcasetemplate.core.MethodSignature;
import testingngservices.testcasetemplate.regex.RegexFieldNameFinder;
import testingngservices.testcasetemplate.regex.RegexInvocationSequenceFinder;

public class AutoMockTest extends TestCase {

	private String sourceFilePath;
	private String fieldClassName1;
	private String fieldSimpleClassName1;
	private String fieldClassName2;
	private String fieldSimpleClassName2;

	protected void setUp() throws Exception {
		sourceFilePath = "src/test/bank/AccountService.java";
		fieldClassName1 = "test.bank.IAccountManager";
		fieldSimpleClassName1 = "IAccountManager";
		fieldClassName2 = "test.bank.Logger";
		fieldSimpleClassName2 = "Logger";
	}

	public void testExtractFieldName() {

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);

		IFieldNameFinder finder = new ASTFieldNameFinder(unit);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new ASTFieldNameFinder(sourceFilePath);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new RegexFieldNameFinder(sourceFilePath);
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

		finder = new RegexFieldNameFinder(new TextFile(sourceFilePath));
		assertEquals("manager", finder.getFieldName(fieldClassName1));
		assertEquals("manager", finder.getFieldName(fieldSimpleClassName1));

	}
	
	public void testRegexInvocationSequenceFinder() {
		InvocationSequenceFinder finder = new RegexInvocationSequenceFinder(
				sourceFilePath);
		assertEquals("logger", finder.getFieldName(fieldClassName2));
		assertEquals("logger", finder.getFieldName(fieldSimpleClassName2));
		
		// Base case
		Invocation[] expectedInvocations = new Invocation[2];
		expectedInvocations[0] = new Invocation("logger.log(accountId)", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation("logger.log(amount)", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodDeclaration("double", "withdraw", new Parameter(
				"String", "accountId"), new Parameter("double", "amount"));
		finder.setScopeByMethodDeclaration("double", "withdraw");
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
		finder.setScopeByMethodDeclaration("double", "transfer");	
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(invocations));
		assertTrue(Arrays.equals(invocations, expectedInvocations));
		
	}

	public void testASTInvocationSequenceFinder() {
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				sourceFilePath);
		assertEquals("logger", finder.getFieldName(fieldClassName2));
		assertEquals("logger", finder.getFieldName(fieldSimpleClassName2));
		
		// Base case
		Invocation[] expectedInvocations = new Invocation[2];
		expectedInvocations[0] = new Invocation("logger.log(accountId)", false,
				InvocationCount.ONCE);
		expectedInvocations[1] = new Invocation("logger.log(amount)", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodDeclaration("double", "withdraw", new Parameter(
				"String", "accountId"), new Parameter("double", "amount"));
		finder.setScopeByMethodDeclaration("double", "withdraw");
		Invocation[] invocations = finder.getInvocations(fieldClassName2);
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
		
		// Contain return value
		expectedInvocations = new Invocation[13];
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
				"manager.withdraw(accountId,amount)", true,
				InvocationCount.ONCE);
		
		finder.setScopeByMethodDeclaration("double", "checkInvocationWithReturnValue");
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
		finder.setScopeByMethodDeclaration("double", "transfer");
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
		finder.setScopeByMethodDeclaration("void", "checkInvocationWithLoop");
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
				"manager.releaseCollection()", false,
				InvocationCount.ONCE);
		finder.setScopeByMethodDeclaration("void", "checkInvocationWithTryCatchFinally");
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
				"manager.releaseCollection()", false,
				InvocationCount.ALLOWING);
		finder.setScopeByMethodDeclaration("void", "checkInvocationWithIfElse");
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
				"manager.releaseCollection()", false,
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
		finder.setScopeByMethodDeclaration("double", "checkInvocationWithAllFeatures");
		invocations = finder.getInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(expectedInvocations));
		// System.out.println(Arrays.toString(invocations));
		assertTrue("Should be equal", Arrays.equals(expectedInvocations,
				invocations));
	}
	
	public void testJMockInvocations() {
		// ASTInvocationSequenceFinder
		String[] expectedJMockInvocations = new String[8];
		expectedJMockInvocations[0] = "once (manager).beginTransaction()";
		expectedJMockInvocations[1] = "allowing (manager).withdraw(accountId,amount)";
		expectedJMockInvocations[2] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[3] = "allowing (manager).commit()";
		expectedJMockInvocations[4] = "ignoring (manager).rollback()";
		expectedJMockInvocations[5] = "once (manager).needClose()";
		expectedJMockInvocations[6] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[7] = "allowing (manager).releaseCollection()";
		InvocationSequenceFinder finder = new ASTInvocationSequenceFinder(
				sourceFilePath);
		finder.setScopeByMethodDeclaration("void", "checkInvocationWithIfElse");
		String[] jMockInvocations = finder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(expectedJMockInvocations));
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue("Should be equal", Arrays.equals(expectedJMockInvocations,
				jMockInvocations));
		
		// RegexInvocationSequenceFinder
		expectedJMockInvocations = new String[10];
		expectedJMockInvocations[0] = "once (manager).beginTransaction()";
		expectedJMockInvocations[1] = "once (manager).beginTransaction()";
		expectedJMockInvocations[2] = "once (manager).withdraw(accountId, amount)";
		expectedJMockInvocations[3] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[4] = "once (manager).commit()";
		expectedJMockInvocations[5] = "once (manager).beginTransaction()";
		expectedJMockInvocations[6] = "once (manager).deposit(accountId, amount)";
		expectedJMockInvocations[7] = "will(returnValue(<NeedFilled>))";
		expectedJMockInvocations[8] = "once (manager).commit()";
		expectedJMockInvocations[9] = "once (manager).commit()";
		InvocationSequenceFinder regexFinder = new RegexInvocationSequenceFinder(sourceFilePath);
		regexFinder.setScopeByMethodDeclaration("double", "transfer");
		jMockInvocations = regexFinder.getJMockInvocations(fieldClassName1);
		// System.out.println(Arrays.toString(jMockInvocations));
		assertTrue(Arrays.equals(jMockInvocations, expectedJMockInvocations));
	}
	
	public void testMethodSignatureFinder() {
		MethodSignatureFinder finder = new MethodSignatureFinder(sourceFilePath);
		MethodSignature ms = finder.getMethodSignature("double", "transfer");
		assertEquals("double", ms.getReturnTypeName());
		assertEquals("transfer", ms.getMethodName());
		Parameter[] expectedPrams = new Parameter[3];
		expectedPrams[0] = new Parameter("String", "accountIdA");
		expectedPrams[1] = new Parameter("String", "accountIdB");
		expectedPrams[2] = new Parameter("double", "amount");
		// System.out.println(ms);
		assertTrue("They should be equal", Arrays.equals(expectedPrams, ms.getParameters()));
	}
}