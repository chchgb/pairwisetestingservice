package pairwisetesting.test.dependency;

import java.util.ArrayList;
import java.util.Arrays;
import pairwisetesting.dependency.DependencyFinder;
import pairwisetesting.dependency.DependencyResult;
import junit.framework.TestCase;

public class TestDependency extends TestCase {
	public void testSelf() {
		String fullClassName = "pairwisetesting.test.dependency.TestDependency";
		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");

		DependencyResult dr = df.findDependency();

		ArrayList<String> expSrcList = new ArrayList<String>();
		expSrcList.add("src/pairwisetesting/dependency/DependencyResult.java");
		expSrcList.add("src/pairwisetesting/util/Directory.java");
		expSrcList.add("src/pairwisetesting/dependency/DependencyFinder.java");

		Arrays.sort(expSrcList.toArray());
		Arrays.sort(dr.srcList.toArray());
		assertTrue(expSrcList.equals(dr.srcList));

		ArrayList<String> expLibList = new ArrayList<String>();
		expLibList.add("junit");
		assertTrue(expLibList.equals(dr.libList));

		ArrayList<String> expMockList = new ArrayList<String>();
		assertTrue(expMockList.equals(dr.mockList));
	}

	public void testRange() {
		String fullClassName = "pairwisetesting.test.math.PairwiseTest";
		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
		DependencyResult dr = df.findDependency();

		ArrayList<String> expSrcList = new ArrayList<String>();
		expSrcList.add("src/pairwisetesting/test/math/Range.java");
		expSrcList.add("src/pairwisetesting/util/Converter.java");
		expSrcList.add("src/pairwisetesting/test/expect/Expectation.java");
		
		assertEquals(expSrcList, dr.srcList);

		ArrayList<String> expLibList = new ArrayList<String>();
		expLibList.add("xom");
		expLibList.add("testng");
		assertEquals(expLibList, dr.libList);

		ArrayList<String> expMockList = new ArrayList<String>();
		assertTrue(expMockList.equals(dr.mockList));
	}
}
