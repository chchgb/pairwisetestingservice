package pairwisetesting.dependency;

import java.util.ArrayList;
import junit.framework.TestCase;

public class TestDependency extends TestCase {

	private DependencyFinder depFinder;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		String className = "pairwisetesting.dependency.TestDependency";
		depFinder = new DependencyFinder(className);
	}

	public void testFindDependency() {
		DependencyResult res = depFinder.findDependency();
		
		ArrayList<String> srcList = new ArrayList<String>();
		srcList.add("src/pairwisetesting/dependency/DependencyFinder.java");
		srcList.add("src/pairwisetesting/dependency/DependencyResult.java");
		
		ArrayList<String> libList = new ArrayList<String>();
		libList.add("junit");
		
		assertTrue(srcList.equals(res.srcList));
		assertTrue(libList.equals(res.libList));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
