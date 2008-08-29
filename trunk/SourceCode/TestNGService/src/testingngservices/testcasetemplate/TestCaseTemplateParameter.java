package testingngservices.testcasetemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import testingngservices.testcasetemplate.core.Argument;

import com.google.common.base.Preconditions;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class encapsulates the test case template parameter related information.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * 
 * @see Argument
 * @see MethodUnderTest
 */
public class TestCaseTemplateParameter {

	private String packageName = "";
	private String classUnderTest = "";
	private ArrayList<Argument> constructorArguments
										= new ArrayList<Argument>();
	private MethodUnderTest methodUnderTest = new MethodUnderTest();
	private boolean isStaticMethod = false;
	private String singletonMethod = "";
	private String checkStateMethod = "";
	private double delta = 0;
	private boolean hasDelta = false;
	private ArrayList<String> imports = new ArrayList<String>();
	private LinkedHashMap<String, String> classToMockInstanceNameMap
			= new LinkedHashMap<String, String>();
	private LinkedHashMap<String, List<String>> jMockInvocationSequenceMap
			= new LinkedHashMap<String, List<String>>();

	/**
	 * Constructs a empty test case template parameter.
	 */
	public TestCaseTemplateParameter() {
	}

	/**
	 * Returns the method under test object.
	 * 
	 * @return the method under test object
	 */
	public MethodUnderTest getMethodUnderTest() {
		return methodUnderTest;
	}

	/**
	 * Returns the XML data of the method under test object.
	 * 
	 * @return he XML data of the method under test object
	 */
	public String getMethodUnderTestXmlData() {
		String xmlData = new XStreamMethodUnderTestXMLHelper()
				.toXML(methodUnderTest);
		return xmlData.replaceAll("\n\\s*", "").replace("\"", "\\\"");
	}

	/**
	 * Sets the method under test object.
	 * 
	 * @param methodUnderTest
	 *            the specified method under test object
	 * @throws NullPointerException
	 *             if {@code methodUnderTest} is null
	 */
	public void setMethodUnderTest(MethodUnderTest methodUnderTest) {
		Preconditions.checkNotNull(methodUnderTest, "method under test");
		this.methodUnderTest = methodUnderTest;
	}

	/**
	 * Sets the package name.
	 * 
	 * @param packageName
	 *            the specified package name
	 * @throws NullPointerException
	 *             if {@code packageName} is null
	 */
	public void setPackageName(String packageName) {
		Preconditions.checkNotNull(packageName, "package name");
		this.packageName = packageName;
	}

	/**
	 * Return the package name.
	 * 
	 * @return the package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Returns the string representation of the class under test.
	 * 
	 * @return the string representation of the class under test
	 */
	public String getClassUnderTest() {
		return classUnderTest;
	}

	/**
	 * Sets the string representation of class under test.
	 * 
	 * @param classUnderTest
	 *            the specified string representation of class under test
	 * @throws NullPointerException
	 *             if {@code classUnderTest} is null
	 */
	public void setClassUnderTest(String classUnderTest) {
		Preconditions.checkNotNull(classUnderTest, "class under test");
		this.classUnderTest = classUnderTest;
	}

	/**
	 * Sets <tt>true</tt> if the method under test is a static method.
	 * 
	 * @param isStaticMethod
	 *            <tt>true</tt> if if the method under test is a static method
	 */
	public void setStaticMethod(boolean isStaticMethod) {
		this.isStaticMethod = isStaticMethod;
	}

	/**
	 * Returns <tt>true</tt> if the method under test is a static method.
	 * 
	 * @return <tt>true</tt> if the method under test is a static method
	 */
	public boolean isStaticMethod() {
		return isStaticMethod;
	}

	/**
	 * Returns an array of the method under test's parameters.
	 * 
	 * @return an array of the method under test's parameters
	 */
	public Parameter[] getMethodParameters() {
		return methodUnderTest.getParameters();
	}

	/**
	 * Adds a new constructor argument.
	 * 
	 * @param type
	 *            the specified constructor argument's type
	 * @param value
	 *            the specified constructor argument's value
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public void addConstructorArgument(String type, String value) {
		Preconditions.checkNotNull(type, "argument's type");
		Preconditions.checkNotNull(value, "argument's value");
		this.constructorArguments.add(new Argument(type, value));
	}

	/**
	 * Returns an array of the constructor arguments.
	 * 
	 * @return an array of the constructor arguments
	 */
	public Argument[] getConstructorArguments() {
		return constructorArguments.toArray(new Argument[0]);
	}

	/**
	 * Returns <tt>true</tt> if the class under test has constructor arguments.
	 * 
	 * @return <tt>true</tt> if the class under test has constructor arguments
	 */
	public boolean hasConstructorArguments() {
		return constructorArguments.isEmpty() == false;
	}

	/**
	 * Returns the return type of the method under test.
	 * 
	 * @return the return type of the method under test
	 */
	public String getReturnType() {
		return methodUnderTest.getReturnType();
	}

	/**
	 * Returns the singleton method name of the class under test.
	 * 
	 * @return the singleton method name of the class under test.
	 */
	public String getSingletonMethod() {
		return singletonMethod;
	}

	/**
	 * Sets the singleton method name of the class under test.
	 * 
	 * @param singletonMethod
	 *            the singleton method name of the class under test
	 * @throws NullPointerException
	 *             if {@code singletonMethod} is null
	 */
	public void setSingletonMethod(String singletonMethod) {
		Preconditions.checkNotNull(singletonMethod, "singleton method");
		this.singletonMethod = singletonMethod;
	}

	/**
	 * Returns <tt>true</tt> if the class under test has singleton method.
	 * 
	 * @return <tt>true</tt> if the class under test has singleton method
	 */
	public boolean isSingleton() {
		return singletonMethod.equals("") == false;
	}

	/**
	 * Sets the check state method of the class under test.
	 * 
	 * @param checkStateMethod
	 *            the specified check state method of the class under test
	 * @throws NullPointerException
	 *             if {@code checkStateMethod} is null
	 */
	public void setCheckStateMethod(String checkStateMethod) {
		Preconditions.checkNotNull(checkStateMethod, "check state method");
		this.checkStateMethod = checkStateMethod;
	}

	/**
	 * Returns <tt>true</tt> if the class under test has check state method.
	 * 
	 * @return <tt>true</tt> if the class under test has check state method
	 */
	public boolean hasCheckStateMethod() {
		return checkStateMethod.equals("") == false;
	}

	/**
	 * Returns the check state method of the class under test.
	 * 
	 * @return the check state method of the class under test
	 */
	public String getCheckStateMethod() {
		return checkStateMethod;
	}

	/**
	 * Sets the delta for the return value of method under test.
	 * 
	 * @param delta
	 *            the specified delta for the return value of method under test
	 */
	public void setDelta(double delta) {
		this.delta = delta;
		this.hasDelta = true;
	}
	
	/**
	 * Returns <tt>true</tt> if the return value of method under test has delta.
	 * 
	 * @return <tt>true</tt> if the return value of method under test has delta
	 */
	public boolean hasDelta() {
		return hasDelta;
	}
	
	/**
	 * Returns the delta of the return value of method under test.
	 * 
	 * @return the delta of the return value of method under test 
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * Adds a new import statement. 
	 * 
	 * @param importName the specified import name
	 * @throws NullPointerException
	 *             if {@code importName} is null
	 */
	public void addImport(String importName) {
		Preconditions.checkNotNull(importName, "import name");
		this.imports.add(importName);
	}

	/**
	 * Returns an array of the import statements.
	 * 
	 * @return an array of the import statements
	 */
	public String[] getImports() {
		return this.imports.toArray(new String[0]);
	}

	/**
	 * Returns <tt>true</tt> if the class under test has import statements.
	 * 
	 * @return <tt>true</tt> if the class under test has import statements
	 */
	public boolean hasImports() {
		return this.imports.size() != 0;
	}

	/**
	 * Adds a new class to mock instance name.
	 * 
	 * @param className
	 *            the specified name of the class to mock
	 * @param instanceName
	 *            the specified instance name of the class to mock
	 * @throws NullPointerException
	 *             if {@code className} or {@code instanceName} is null
	 */
	public void addClassToMockInstanceName(String className, 
			String instanceName) {
		Preconditions.checkNotNull(className, "class name");
		Preconditions.checkNotNull(instanceName, "instance name");
		this.classToMockInstanceNameMap.put(className, instanceName);
	}

	/**
	 * Returns the entry set of the class to mock instance names.
	 * 
	 * @return the entry set of the class to mock instance names 
	 */
	public Set<Map.Entry<String, String>> getClassToMockInstanceNameEntrySet() {
		return this.classToMockInstanceNameMap.entrySet();
	}

	/**
	 * Returns <tt>true</tt> if the class under test has classes to mock.
	 * 
	 * @return <tt>true</tt> if the class under test has classes to mock
	 */
	public boolean hasClassesToMock() {
		return this.classToMockInstanceNameMap.keySet().size() != 0;
	}

	/**
	 * Adds a new JMock invocation sequence with the specified class name and
	 * invocations.
	 * @param className
	 *            the specified class name
	 * @param invocations
	 *            the specified invocations
	 * @throws NullPointerException
	 *             if {@code className} or {@code invocations} is null
	 */
	public void addJMockInvocationSequence(String className, 
			String[] invocations) {
		Preconditions.checkNotNull(className, "class name");
		Preconditions.checkNotNull(invocations, "invocations");
		this.jMockInvocationSequenceMap.put(className, 
				Arrays.asList(invocations));
	}

	/**
	 * Returns the JMock invocation sequence with the specified class name.
	 * 
	 * @param className
	 *            the specified class name.
	 * @return the JMock invocation sequence with the specified class name
	 * @throws NullPointerException
	 *             if {@code className} is null
	 */
	public List<String> getJMockInvocationSequence(String className) {
		Preconditions.checkNotNull(className, "class name");
		return this.jMockInvocationSequenceMap.get(className);
	}

	/**
	 * Constructs and returns a test case template parameter with the specified
	 * XML data.
	 * 
	 * @param xmlData
	 *            the specified XML data
	 * @return a test case template parameter with the specified XML data
	 * @throws NullPointerException
	 *             if {@code xmlData} is null
	 */
	public static TestCaseTemplateParameter fromXML(String xmlData) {
		Preconditions.checkNotNull(xmlData, "XML data");
		XStream xstream = new XStream(new DomDriver());
		return (TestCaseTemplateParameter) xstream.fromXML(xmlData);
	}

	/**
	 * Returns the XML representation of the test case template parameter.
	 * 
	 * @return the XML representation of the test case template parameter
	 */
	public String toXML() {
		XStream xstream = new XStream(new DomDriver());
		return xstream.toXML(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((checkStateMethod == null) ? 0 : checkStateMethod.hashCode());
		result = prime
				* result
				+ ((classToMockInstanceNameMap == null) ? 0
						: classToMockInstanceNameMap.hashCode());
		result = prime * result
				+ ((classUnderTest == null) ? 0 : classUnderTest.hashCode());
		result = prime
				* result
				+ ((constructorArguments == null) ? 0 : constructorArguments
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(delta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (hasDelta ? 1231 : 1237);
		result = prime * result + ((imports == null) ? 0 : imports.hashCode());
		result = prime * result + (isStaticMethod ? 1231 : 1237);
		result = prime
				* result
				+ ((jMockInvocationSequenceMap == null) ? 0
						: jMockInvocationSequenceMap.hashCode());
		result = prime * result
				+ ((methodUnderTest == null) ? 0 : methodUnderTest.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((singletonMethod == null) ? 0 : singletonMethod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCaseTemplateParameter other = (TestCaseTemplateParameter) obj;
		if (checkStateMethod == null) {
			if (other.checkStateMethod != null)
				return false;
		} else if (!checkStateMethod.equals(other.checkStateMethod))
			return false;
		if (classToMockInstanceNameMap == null) {
			if (other.classToMockInstanceNameMap != null)
				return false;
		} else if (!classToMockInstanceNameMap
				.equals(other.classToMockInstanceNameMap))
			return false;
		if (classUnderTest == null) {
			if (other.classUnderTest != null)
				return false;
		} else if (!classUnderTest.equals(other.classUnderTest))
			return false;
		if (constructorArguments == null) {
			if (other.constructorArguments != null)
				return false;
		} else if (!constructorArguments.equals(other.constructorArguments))
			return false;
		if (Double.doubleToLongBits(delta) != Double
				.doubleToLongBits(other.delta))
			return false;
		if (hasDelta != other.hasDelta)
			return false;
		if (imports == null) {
			if (other.imports != null)
				return false;
		} else if (!imports.equals(other.imports))
			return false;
		if (isStaticMethod != other.isStaticMethod)
			return false;
		if (jMockInvocationSequenceMap == null) {
			if (other.jMockInvocationSequenceMap != null)
				return false;
		} else if (!jMockInvocationSequenceMap
				.equals(other.jMockInvocationSequenceMap))
			return false;
		if (methodUnderTest == null) {
			if (other.methodUnderTest != null)
				return false;
		} else if (!methodUnderTest.equals(other.methodUnderTest))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (singletonMethod == null) {
			if (other.singletonMethod != null)
				return false;
		} else if (!singletonMethod.equals(other.singletonMethod))
			return false;
		return true;
	}

}
