package pairwisetesting.test.dependency;

import pairwisetesting.util.dependency.DependencyFinder;
import pairwisetesting.util.dependency.DependencyResult;
import junit.framework.TestCase;

public class TestFinder2 extends TestCase{

	public void testFindDependency(){
		String className = "pairwisetesting.dependency.test.TestFinder2";
		DependencyFinder df = new DependencyFinder(className, "src", "bin");
		DependencyResult result = df.findDependency();
		
		// Íµ¸öÀÁ :P
		System.out.print(result.srcList);
		System.out.println("");
		System.out.print(result.libList);
		System.out.println("");
		System.out.print(result.mockList);
	}
}
