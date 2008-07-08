package testingngservices.testcasetemplate;

import java.lang.reflect.Method;

import pairwisetesting.complex.ChildParametersExtractor;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.util.ClassUtil;
import testingngservices.testcasetemplate.core.MethodSignature;

public class MethodUnderTestFinder {

	private String className;
	private MethodSignatureFinder methodSignatureFinder;

	public MethodUnderTestFinder(String sourceFilePath, String className) {
		this.methodSignatureFinder = new MethodSignatureFinder(sourceFilePath);
		this.className = className;
	}

	public MethodUnderTest getMethodUnderTest(String returnTypeName, String methodName) {
		MethodUnderTest methodUnderTest = new MethodUnderTest(returnTypeName, methodName);
		MethodSignature methodSignature
					= methodSignatureFinder.getMethodSignature(returnTypeName, methodName);
		Class<?> clazz = ClassUtil.getClass(className);
		try {
			Method m = ClassUtil.getFirstMethod(clazz, returnTypeName, methodName);
			ChildParametersExtractor extractor = new ChildParametersExtractor();
			for (int i = 0; i < m.getParameterTypes().length; i++) {
				Class<?> parameterType = m.getParameterTypes()[i];
				String parameterName = methodSignature.getParameters()[i].getName();
				// SimpleParameter
				if (ClassUtil.isSimpleType(parameterType)) {
					SimpleParameter p = new SimpleParameter(parameterType.getName(), parameterName);
					methodUnderTest.add(p);
				// ComplexParameter
				} else {
					ComplexParameter p = new ComplexParameter(parameterType.getName(), parameterName);
					Parameter[] children = extractor.getParameters(parameterType.getName());
					for (Parameter child : children) {
						p.add(child);
					}
					methodUnderTest.add(p);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return methodUnderTest;
	}
}
