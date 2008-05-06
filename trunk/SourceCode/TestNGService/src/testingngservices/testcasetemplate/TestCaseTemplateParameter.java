package testingngservices.testcasetemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class TestCaseTemplateParameter {
	private String packageName = "";
	private String classUnderTest = "";
	private ArrayList<Argument> constructorArguments = new ArrayList<Argument>();
	private String methodUnderTest = "";
	private ArrayList<Parameter> methodParameters = new ArrayList<Parameter>();
	private boolean isStaticMethod = false;
	private String singletonMethod = "";
	private String checkStateMethod = "";
	private String returnType = "";
	private double delta = 0;
	private boolean hasDelta = false;
	private ArrayList<String> imports = new ArrayList<String>();
	private LinkedHashMap<String, String> classToMockInstanceNameMap = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, List<String>> jMockInvokeSequenceMap = new LinkedHashMap<String, List<String>>();
	
	public TestCaseTemplateParameter() {
	}

	public TestCaseTemplateParameter(String xmlData) throws Exception {
		
		Document doc = new Builder().build(xmlData, null);
		Element root = doc.getRootElement();
		
		Elements importElements = root.getChildElements("import");
		for (int i = 0; i < importElements.size(); i++) {
			Element importElement = importElements.get(i);
			this.addImport(importElement.getValue());
		}
		
		Elements classToMockElements = root.getChildElements("classToMock");
		for (int i = 0; i < classToMockElements.size(); i++) {
			Element classToMockElement = classToMockElements.get(i);
			Element classNameElement = classToMockElement.getFirstChildElement("className");
			Element instanceNameElement = classToMockElement.getFirstChildElement("instanceName");
			this.addClassToMockInstanceName(classNameElement.getValue(), instanceNameElement.getValue());
			Elements invokeElements = classToMockElement.getChildElements("invoke");
			ArrayList<String> invokeList = new ArrayList<String>();
			for (int j = 0; j < invokeElements.size(); j++) {
				Element invokeElement = invokeElements.get(j);
				invokeList.add(invokeElement.getValue());
			}
			this.addJMockInvokeSequence(classNameElement.getValue(), invokeList.toArray(new String[0]));
		}
		
		this.setPackageName(root.getFirstChildElement("packageName").getValue());
		this.setClassUnderTest(root.getFirstChildElement("classUnderTest").getValue());
		
		Elements constructorArguments = root.getChildElements("constructorArgument");
		for (int i = 0; i < constructorArguments.size(); i++) {
			Element constructorArgumentElement = constructorArguments.get(i);
			String type = constructorArgumentElement.getFirstChildElement("type").getValue();
			String value = constructorArgumentElement.getFirstChildElement("value").getValue();
			this.addConstructorArgument(type, value);
		}
		
		this.setMethodUnderTest(root.getFirstChildElement("methodUnderTest").getValue());
		
		Elements methodParameters = root.getChildElements("methodParameter");
		for (int i = 0; i < methodParameters.size(); i++) {
			Element methodParameterElement = methodParameters.get(i);
			String type = methodParameterElement.getFirstChildElement("type").getValue();
			String name = methodParameterElement.getFirstChildElement("name").getValue();
			this.addMethodParameter(type, name);
		}
		
		this.setStaticMethod(Boolean.parseBoolean(root.getFirstChildElement("isStaticMethod").getValue()));
		
		if (root.getFirstChildElement("singletonMethod") != null) {
			this.setSingletonMethod(root.getFirstChildElement("singletonMethod").getValue());
		}
		
		if (root.getFirstChildElement("checkStateMethod") != null) {
			this.setCheckStateMethod(root.getFirstChildElement("checkStateMethod").getValue());
		}
		
		this.setReturnType(root.getFirstChildElement("returnType").getValue());
		
		if (root.getFirstChildElement("delta") != null) {
			this.setDelta(Double.parseDouble(root.getFirstChildElement("delta").getValue()));
		}
	}

	public String toXML() {
		Element root = new Element("testCaseTemplateParameter");
		
		for (String importName : imports) {
			Element importElement = new Element("import");
			importElement.appendChild(importName);
			root.appendChild(importElement);
		}
		
		for (Map.Entry<String, String> entry : classToMockInstanceNameMap.entrySet()) {
			Element classToMockElement = new Element("classToMock");
			
			// ClassName
			Element classNameElement = new Element("className");
			classNameElement.appendChild(entry.getKey());
			classToMockElement.appendChild(classNameElement);
			
			// InstanceName
			Element instanceNameElement = new Element("instanceName");
			instanceNameElement.appendChild(entry.getValue());
			classToMockElement.appendChild(instanceNameElement);
			
			// Invokes
			for (String invoke : jMockInvokeSequenceMap.get(entry.getKey())) {
				Element invokeElement = new Element("invoke");
				invokeElement.appendChild(invoke);
				classToMockElement.appendChild(invokeElement);
			}
			root.appendChild(classToMockElement);
		}

		Element packageNameElement = new Element("packageName");
		packageNameElement.appendChild(this.getPackageName());
		root.appendChild(packageNameElement);
		
		Element classUnderTestElement = new Element("classUnderTest");
		classUnderTestElement.appendChild(this.getClassUnderTest());
		root.appendChild(classUnderTestElement);
		
		for (Argument constructorArgument : constructorArguments) {
			Element constructorArgumentElement = new Element("constructorArgument");
			Element typeElement = new Element("type");
			typeElement.appendChild(constructorArgument.getType());
			Element valueElement = new Element("value");
			valueElement.appendChild(constructorArgument.getValue());
			constructorArgumentElement.appendChild(typeElement);
			constructorArgumentElement.appendChild(valueElement);
			root.appendChild(constructorArgumentElement);
		}
		
		Element methodUnderTestElement = new Element("methodUnderTest");
		methodUnderTestElement.appendChild(this.getMethodUnderTest());
		root.appendChild(methodUnderTestElement);
		
		for (Parameter methodParameter : methodParameters) {
			Element methodParameterElement = new Element("methodParameter");
			Element typeElement = new Element("type");
			typeElement.appendChild(methodParameter.getType());
			Element nameElement = new Element("name");
			nameElement.appendChild(methodParameter.getName());
			methodParameterElement.appendChild(typeElement);
			methodParameterElement.appendChild(nameElement);
			root.appendChild(methodParameterElement);
		}
		
		Element isStaticMethodElement = new Element("isStaticMethod");
		isStaticMethodElement.appendChild("" + this.isStaticMethod());
		root.appendChild(isStaticMethodElement);
		
		if (this.isSingleton()) {
			Element singletonMethodElement = new Element("singletonMethod");
			singletonMethodElement.appendChild(this.getSingletonMethod());
			root.appendChild(singletonMethodElement);
		}
		
		if (this.hasCheckStateMethod()) {
			Element checkStateMethodElement = new Element("checkStateMethod");
			checkStateMethodElement.appendChild(this.getCheckStateMethod());
			root.appendChild(checkStateMethodElement);
		}
		
		Element returnTypeElement = new Element("returnType");
		returnTypeElement.appendChild(this.getReturnType());
		root.appendChild(returnTypeElement);
		
		if (this.hasDelta()) {
			Element deltaElement = new Element("delta");
			deltaElement.appendChild("" + this.getDelta());
			root.appendChild(deltaElement);
		}
		
		Document doc = new Document(root);
		return doc.toXML();
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getMethodUnderTest() {
		return methodUnderTest;
	}

	public void setMethodUnderTest(String methodUnderTest) {
		this.methodUnderTest = methodUnderTest;
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

	public void addMethodParameter(String type, String name) {
		this.methodParameters.add(new Parameter(type, name));
	}

	public Parameter[] getMethodParameters() {
		return methodParameters.toArray(new Parameter[0]);
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
		return returnType;
	}

	/**
	 * @param returnType
	 *            the return type of the method under test if not void or the
	 *            return type of the checkstate method
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
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
	
	public void addJMockInvokeSequence(String className, String[] invokes) {
		this.jMockInvokeSequenceMap.put(className, Arrays.asList(invokes));
	}
	
	public List<String> getJMockInvokeSequence(String className) {
		return this.jMockInvokeSequenceMap.get(className);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((checkStateMethod == null) ? 0 : checkStateMethod.hashCode());
		result = prime * result
				+ ((classUnderTest == null) ? 0 : classUnderTest.hashCode());
		long temp;
		temp = Double.doubleToLongBits(delta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (hasDelta ? 1231 : 1237);
		result = prime * result + (isStaticMethod ? 1231 : 1237);
		result = prime * result
				+ ((methodUnderTest == null) ? 0 : methodUnderTest.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((returnType == null) ? 0 : returnType.hashCode());
		result = prime * result
				+ ((singletonMethod == null) ? 0 : singletonMethod.hashCode());
		result = prime * result
				+ ((constructorArguments == null) ? 0 : constructorArguments.hashCode());
		result = prime * result
				+ ((methodParameters == null) ? 0 : methodParameters.hashCode());
		result = prime * result
				+ ((imports == null) ? 0 : imports.hashCode());
		result = prime * result
				+ ((classToMockInstanceNameMap == null) ? 0 : classToMockInstanceNameMap.hashCode());
		result = prime * result
				+ ((jMockInvokeSequenceMap == null) ? 0 : jMockInvokeSequenceMap.hashCode());
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
		final TestCaseTemplateParameter other = (TestCaseTemplateParameter) obj;
		if (checkStateMethod == null) {
			if (other.checkStateMethod != null)
				return false;
		} else if (!checkStateMethod.equals(other.checkStateMethod))
			return false;
		if (classUnderTest == null) {
			if (other.classUnderTest != null)
				return false;
		} else if (!classUnderTest.equals(other.classUnderTest))
			return false;
		if (Double.doubleToLongBits(delta) != Double
				.doubleToLongBits(other.delta))
			return false;
		if (hasDelta != other.hasDelta)
			return false;
		if (isStaticMethod != other.isStaticMethod)
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
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		if (singletonMethod == null) {
			if (other.singletonMethod != null)
				return false;
		} else if (!singletonMethod.equals(other.singletonMethod))
			return false;
		if (this.constructorArguments == null) {
			if (other.constructorArguments != null)
				return false;
		} else if (!constructorArguments.equals(other.constructorArguments))
			return false;
		if (this.methodParameters == null) {
			if (other.methodParameters != null)
				return false;
		} else if (!methodParameters.equals(other.methodParameters))
			return false;
		if (this.imports == null) {
			if (other.imports != null)
				return false;
		} else if (!imports.equals(other.imports))
			return false;
		if (this.classToMockInstanceNameMap == null) {
			if (other.classToMockInstanceNameMap != null)
				return false;
		} else if (!classToMockInstanceNameMap.equals(other.classToMockInstanceNameMap))
			return false;
		if (this.jMockInvokeSequenceMap == null) {
			if (other.jMockInvokeSequenceMap != null)
				return false;
		} else if (!jMockInvokeSequenceMap.equals(other.jMockInvokeSequenceMap))
			return false;
		return true;
	}
}
