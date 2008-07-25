package testingngservices.testcasetemplate;

import java.lang.reflect.Method;

import pairwisetesting.complex.ChildParametersExtractor;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.util.ClassUtil;
import testingngservices.testcasetemplate.core.MethodSignature;

public class MethodUnderTestFinder implements IMethodUnderTestFinder {

	private String className;
	private MethodSignatureFinder methodSignatureFinder;
	private ChildParametersExtractor extractor = new ChildParametersExtractor();

	public MethodUnderTestFinder(String sourceFilePath, String className) {
		this.methodSignatureFinder = new MethodSignatureFinder(sourceFilePath);
		this.className = className;
	}

	public MethodUnderTest getMethodUnderTest(String returnTypeName, String methodName) throws MethodUnderTestException {
		
		MethodUnderTest methodUnderTest = null;
		
		try {
			// MethodUnderTest's Name and Return Value
			if (ClassUtil.isSimpleType(returnTypeName)) {
				methodUnderTest = new MethodUnderTest(returnTypeName, methodName);
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
						= methodSignatureFinder.getMethodSignature(returnTypeName, methodName);
			Class<?> clazz = ClassUtil.getClass(className);
			Method m = ClassUtil.getFirstMethod(clazz, returnTypeName, methodName);
			for (int i = 0; i < m.getParameterTypes().length; i++) {
				Class<?> parameterType = m.getParameterTypes()[i];
				String parameterName = methodSignature.getParameters()[i].getName();
				String shortTypeName = methodSignature.getParameters()[i].getType();
				// SimpleParameter
				if (ClassUtil.isSimpleType(parameterType)) {
					String fullTypeName = getFullTypeName(shortTypeName, parameterType.getCanonicalName());
					SimpleParameter p = new SimpleParameter(fullTypeName, parameterName);
					methodUnderTest.add(p);
				// ComplexParameter
				} else {
					ComplexParameter p = new ComplexParameter(parameterType.getCanonicalName(), parameterName);
					fillChildren(p);
					methodUnderTest.add(p);
				}
			}
		} catch(Exception e) {
			throw new MethodUnderTestException(e);
		}
		return methodUnderTest;
	}

	private String getFullTypeName(String shortTypeName, String canonicalName) {
		if (canonicalName.lastIndexOf('.') != -1) {
			return canonicalName.substring(0, canonicalName.lastIndexOf('.') + 1) + shortTypeName;
		} else {
			return canonicalName;
		}
	}

	/**
	 * Extract ComplexParameter's child parameters and fill them into it
	 */
	private void fillChildren(ComplexParameter p) {
		Parameter[] children = extractor.getParameters(p.getType());
		for (Parameter child : children) {
			p.add(child);
		}
	}
}
