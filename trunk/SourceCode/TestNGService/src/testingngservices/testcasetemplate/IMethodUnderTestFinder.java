package testingngservices.testcasetemplate;

import pairwisetesting.complex.MethodUnderTest;

public interface IMethodUnderTestFinder {

	MethodUnderTest getMethodUnderTest(String returnTypeName, 
			String methodName) throws MethodUnderTestException;

}