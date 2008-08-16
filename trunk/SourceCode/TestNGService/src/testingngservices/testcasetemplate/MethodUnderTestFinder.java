package testingngservices.testcasetemplate;

import java.lang.reflect.Method;

import com.google.common.base.Preconditions;

import pairwisetesting.complex.ChildParametersExtractor;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.util.ClassUtil;
import testingngservices.testcasetemplate.core.MethodSignature;

/**
 * The default implementation to find the method under test.
 */
public class MethodUnderTestFinder implements IMethodUnderTestFinder {

	private String className;
	private MethodSignatureFinder methodSignatureFinder;
	private ChildParametersExtractor extractor = new ChildParametersExtractor();

	/**
	 * Constructs a method under test finder with the specified source file path
	 * and class name.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @param className
	 *            the specified class name
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} or {@code className} is null
	 */
	public MethodUnderTestFinder(String sourceFilePath, String className) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		Preconditions.checkNotNull(className, "class name");
		this.methodSignatureFinder = new MethodSignatureFinder(sourceFilePath);
		this.className = className;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * testingngservices.testcasetemplate.IMethodUnderTestFinder#getMethodUnderTest
	 * (java.lang.String, java.lang.String)
	 */
	public MethodUnderTest getMethodUnderTest(String returnTypeName, 
			String methodName) throws MethodUnderTestException {
		Preconditions.checkNotNull(returnTypeName, "return type name");
		Preconditions.checkNotNull(returnTypeName, "method name");
		
		MethodUnderTest methodUnderTest = null;
		
		try {
			// MethodUnderTest's Name and Return Value
			if (ClassUtil.isSimpleType(returnTypeName)) {
				methodUnderTest
					= new MethodUnderTest(returnTypeName, methodName);
			} else {
				methodUnderTest = new MethodUnderTest();
				methodUnderTest.setName(methodName);
				ComplexParameter returnValueParameter
					= new ComplexParameter(returnTypeName, "ReturnValue");
				fillChildren(returnValueParameter);
				methodUnderTest.setReturnValueRarameter(returnValueParameter);
			}
			
			// MethodUnderTest's Input Parameters
			MethodSignature methodSignature
				= methodSignatureFinder.getMethodSignature(returnTypeName, 
						methodName);
			Class<?> clazz = ClassUtil.getClass(className);
			Method m = ClassUtil.getFirstMethod(clazz, returnTypeName, 
					methodName);
			for (int i = 0; i < m.getParameterTypes().length; i++) {
				Class<?> parameterType = m.getParameterTypes()[i];
				String parameterName = methodSignature.getParameters()[i].getName();
				String shortTypeName = methodSignature.getParameters()[i].getType();
				// SimpleParameter
				if (ClassUtil.isSimpleType(parameterType)) {
					String fullTypeName = getFullTypeName(shortTypeName, 
											parameterType.getCanonicalName());
					SimpleParameter p 
						= new SimpleParameter(fullTypeName, parameterName);
					methodUnderTest.add(p);
				// ComplexParameter
				} else {
					ComplexParameter p = new ComplexParameter(
							parameterType.getCanonicalName(), parameterName);
					fillChildren(p);
					methodUnderTest.add(p);
				}
			}
		} catch(Exception e) {
			throw new MethodUnderTestException(e);
		}
		return methodUnderTest;
	}

	/**
	 * Returns the full type name with the specified short type name and
	 * canonical name.
	 * 
	 * @param shortTypeName
	 *            the specified short type name
	 * @param canonicalName
	 *            the specified canonical name
	 * @return the full type name with the specified short type name and
	 *         canonical name
	 */
	private String getFullTypeName(String shortTypeName, 
			String canonicalName) {
		if (canonicalName.lastIndexOf('.') != -1) {
			return canonicalName.substring(0, 
					canonicalName.lastIndexOf('.') + 1) + shortTypeName;
		} else {
			return canonicalName;
		}
	}

	/**
	 * Extracts the specified complex arameter's child parameters and fill them
	 * into it.
	 * 
	 * @param p
	 *            the specified complex parameter
	 */
	private void fillChildren(ComplexParameter p) {
		Parameter[] children = extractor.getParameters(p.getType());
		for (Parameter child : children) {
			p.add(child);
		}
	}
}
