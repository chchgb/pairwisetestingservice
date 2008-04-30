package pairwisetesting.dependency.test;

import java.util.ArrayList;
import java.util.Collections;

import org.antlr.stringtemplate.StringTemplate;

import junit.framework.TestCase;
import pairwisetesting.dependency.DependencyFinder;
import pairwisetesting.dependency.DependencyResult;

public class TestDependency extends TestCase {

	private DependencyFinder depFinder;
	private IAccountManager manager;
	private AbstractAccountManager absMan;
	private StringTemplate t = new StringTemplate();
	
	protected void setUp() throws Exception {
		super.setUp();
		
		String className = "pairwisetesting.dependency.test.TestDependency";
		depFinder = new DependencyFinder(className,"src","WebRoot/WEB-INF/classes");
		
		manager = new AccountManager();
		manager.store("Andy");
	}

	public void testFindDependency() {
		DependencyResult res = depFinder.findDependency();
		
		ArrayList<String> expectedSrcList = new ArrayList<String>();
		expectedSrcList.add("src/pairwisetesting/dependency/test/AbstractAccountManager.java");
		expectedSrcList.add("src/pairwisetesting/dependency/DependencyFinder.java");
		expectedSrcList.add("src/pairwisetesting/dependency/DependencyResult.java");
		expectedSrcList.add("src/pairwisetesting/dependency/test/AccountManager.java");
		expectedSrcList.add("src/pairwisetesting/dependency/test/IAccountManager.java");
		
		ArrayList<String> expectedLibList = new ArrayList<String>();
		expectedLibList.add("junit");
		expectedLibList.add("antlr");
		
		ArrayList<String> expectedMockList = new ArrayList<String>();
		expectedMockList.add("pairwisetesting.dependency.test.AbstractAccountManager");
		
		
		// Sort the ArrayLists
		Collections.sort(res.srcList);
		Collections.sort(res.libList);
		Collections.sort(res.mockList);
		
		Collections.sort(expectedSrcList);
		Collections.sort(expectedLibList);
		Collections.sort(expectedMockList);
		
		assertTrue(expectedSrcList.equals(res.srcList));
		assertTrue(expectedLibList.equals(res.libList));
		assertEquals(expectedMockList, res.mockList);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
