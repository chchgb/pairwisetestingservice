package test.dependency;

import pairwisetesting.util.dependency.DependencyFinder;
import pairwisetesting.util.dependency.DependencyResult;
import junit.framework.TestCase;

public class TestDependency extends TestCase {

	public void testBasicDependency() {
		String fullClassName = "test.dependency.TestDependency";
		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
		DependencyResult dr = df.findDependency();
		
		System.out.println(dr.srcList);
		System.out.println(dr.libList);
		System.out.println(dr.mockList);
		System.out.println(dr.impList);
		
		assertEquals(3, dr.srcList.size());
		assertEquals(1, dr.libList.size());
		assertEquals(0, dr.mockList.size());
		assertEquals(1, dr.impList.size());
		
	}

//	public void testCircularDependency() {
//		String fullClassName = "test.dependency.A";
//		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
//		DependencyResult dr = df.findDependency();
//
//		assertEquals(6 + 3, dr.srcList.size());
//		assertEquals(0, dr.libList.size());
//		assertEquals(3, dr.mockList.size());
//
//		System.out.println(dr.srcList);
//		System.out.println(dr.libList);
//		System.out.println(dr.mockList);
//		System.out.println(dr.impList);
//	}

//	public void testMetaParameter() {
//		String fullClassName = "pairwisetesting.coredomain.MetaParameter";
//		DependencyFinder df = new DependencyFinder(fullClassName, "src", "bin");
//		DependencyResult dr = df.findDependency();
//
//		assertEquals(6 + 3, dr.srcList.size());
//		assertEquals(0, dr.libList.size());
//		//assertEquals(3, dr.mockList.size());
//
//		System.out.println(dr.srcList);
//		System.out.println(dr.libList);
//		//System.out.println(dr.mockList);
//		//System.out.println(dr.impList);
//	}
}
