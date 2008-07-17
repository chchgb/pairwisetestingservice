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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TestCaseTemplateParameter {
	private String packageName = "";
	private String classUnderTest = "";
	private ArrayList<Argument> constructorArguments = new ArrayList<Argument>();
	private MethodUnderTest methodUnderTest = new MethodUnderTest();
	private boolean isStaticMethod = false;
	private String singletonMethod = "";
	private String checkStateMethod = "";
	private double delta = 0;
	private boolean hasDelta = false;
	private ArrayList<String> imports = new ArrayList<String>();
	private LinkedHashMap<String, String> classToMockInstanceNameMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, List<String>> jMockInvocationSequenceMap = new LinkedHashMap<String, List<String>>();
	
	public TestCaseTemplateParameter() {
	}
	
	public MethodUnderTest getMethodUnderTest() {
		return methodUnderTest;
	}
	
	public String getMethodUnderTestXmlData() {
		String xmlData = new XStreamMethodUnderTestXMLHelper().toXML(methodUnderTest);
		return xmlData.replaceAll("\n\\s*", "").replace("\"", "\\\"");
	}

	public void setMethodUnderTest(MethodUnderTest methodUnderTest) {
		this.methodUnderTest = methodUnderTest;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassUnderTest() {
		return classUnderTest;
	}

	public void setClassUnderTest(String classUnderTest) {
		this.classUnderTest = classUnderTest;
	}

	public void setStaticMethod(boolean isStaticMethod) {
		this.isStaticMethod = isStaticMethod;
	}

	public boolean isStaticMethod() {
		return isStaticMethod;
	}

	public Parameter[] getMethodParameters() {
		return methodUnderTest.getParameters();
	}

	public void addConstructorArgument(String type, String value) {
		this.constructorArguments.add(new Argument(type, value));
	}

	public Argument[] getConstructorArguments() {
		return constructorArguments.toArray(new Argument[0]);
	}
	
	public boolean hasConstructorArguments() {
		return constructorArguments.isEmpty() == false;
	}

	public String getReturnType() {
		return methodUnderTest.getReturnType();
	}

	public String getSingletonMethod() {
		return singletonMethod;
	}

	public void setSingletonMethod(String singletonMethod) {
		this.singletonMethod = singletonMethod;;
	}

	public boolean isSingleton() {
		return singletonMethod.equals("") == false;
	}

	public String getCheckStateMethod() {
		return checkStateMethod;
	}
	
	public boolean hasCheckStateMethod() {
		return checkStateMethod.equals("") == false;
	}

	public void setCheckStateMethod(String checkStateMethod) {
		this.checkStateMethod = checkStateMethod;
	}

	public double getDelta() {
		return delta;
	}
	
	public boolean hasDelta() {
		return hasDelta;
	}
	
	public void setDelta(double delta) {
		this.delta = delta;
		this.hasDelta = true;
	}
	
	public void addImport(String importName) {
		this.imports.add(importName);
	}
	
	public String[] getImports() {
		return this.imports.toArray(new String[0]);
	}
	
	public boolean hasImports() {
		return this.imports.size() != 0;
	}
	
	public void addClassToMockInstanceName(String className, String instanceName) {
		this.classToMockInstanceNameMap.put(className, instanceName);
	}
	
	public Set<Map.Entry<String, String>> getClassToMockInstanceNameEntrySet() {
		return this.classToMockInstanceNameMap.entrySet();
	}
	
	public boolean hasClassesToMock() {
		return this.classToMockInstanceNameMap.keySet().size() != 0;
	}
	
	public void addJMockInvocationSequence(String className, String[] invokes) {
		this.jMockInvocationSequenceMap.put(className, Arrays.asList(invokes));
	}
	
	public List<String> getJMockInvocationSequence(String className) {
		return this.jMockInvocationSequenceMap.get(className);
	}
	
	public static TestCaseTemplateParameter fromXML(String xmlData) {
		XStream xstream = new XStream(new DomDriver());
		return (TestCaseTemplateParameter)xstream.fromXML(xmlData);
	}

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
