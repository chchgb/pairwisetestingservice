package testingngservices.testcasetemplate;

import pairwisetesting.complex.MethodUnderTest;

/**
 * This class acts as the finder to find the method under test.
 * 
 * @see MethodUnderTestFinder
 * @see MethodUnderTest
 */
public interface IMethodUnderTestFinder {

	/**
	 * Returns the method under test object with the specified return type and
	 * method name.
	 * 
	 * @param returnTypeName
	 *            the specified method's return type name
	 * @param methodName
	 *            the specified method name
	 * @return the method under test object with the specified return type and
	 *         method name
	 * @throws MethodUnderTestException
	 *             if the finder has problems in finding the method under test
	 */
	MethodUnderTest getMethodUnderTest(String returnTypeName, String methodName)
			throws MethodUnderTestException;

}