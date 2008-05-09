package test.dependency;

import java.util.ArrayList;
import pairwisetesting.util.dependency.DependencyFinder;
import pairwisetesting.util.dependency.DependencyResult;
import junit.framework.TestCase;

public class TestDependency extends TestCase {
	/*
	public void testBasicDependency() {
		String fullClassName = "test.dependency.TestDependency";
		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");

		DependencyResult dr = df.findDependency();

		ArrayList<String> expSrcList = new ArrayList<String>();
		expSrcList.add("src/pairwisetesting/util/dependency/DependencyResult.java");
		expSrcList.add("src/pairwisetesting/util/Directory.java");
		expSrcList.add("src/pairwisetesting/util/dependency/DependencyFinder.java");

		//System.out.print(dr.srcList);
		//Arrays.sort(expSrcList.toArray());
		//Arrays.sort(dr.srcList.toArray());
		assertTrue(expSrcList.equals(dr.srcList));

		ArrayList<String> expLibList = new ArrayList<String>();
		expLibList.add("junit");
		assertTrue(expLibList.equals(dr.libList));

		ArrayList<String> expMockList = new ArrayList<String>();
		assertTrue(expMockList.equals(dr.mockList));
	}
	*/
	public void testCircularDependency() {
		String fullClassName = "test.dependency.A";
		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
		DependencyResult dr = df.findDependency();

		assertEquals(6 + 3, dr.srcList.size());
		assertEquals(0, dr.libList.size());
		assertEquals(3, dr.mockList.size());

//		System.out.println(dr.srcList);
//		System.out.println(dr.libList);
//		System.out.println(dr.mockList);
	}

//	public void testMetaParameter() {
//		String fullClassName = "pairwisetesting.coredomain.MetaParameter";
//		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
//		DependencyResult dr = df.findDependency();
////
////		assertEquals(6 + 3, dr.srcList.size());
////		assertEquals(0, dr.libList.size());
////		assertEquals(3, dr.mockList.size());
//
////		System.out.println(dr.srcList);
////		System.out.println(dr.libList);
////		System.out.println(dr.mockList);
//	}
}
