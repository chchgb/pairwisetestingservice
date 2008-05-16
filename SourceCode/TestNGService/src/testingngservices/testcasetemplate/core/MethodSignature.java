package testingngservices.testcasetemplate.core;

import testingngservices.testcasetemplate.Parameter;

public class MethodSignature {
	
	private String returnTypeName = "<UNKNOWN>";
	private String methodName = "<UNKNOWN>";
	private Parameter[] parameters = new Parameter[0];
	
	public MethodSignature() {
	}
	
	public MethodSignature(String returnTypeName,
			String methodName, Parameter... parameters) {
		this.returnTypeName = returnTypeName;
		this.methodName = methodName;
		this.parameters = parameters;
	}
	
	public String getReturnTypeName() {
		return returnTypeName;
	}
	
	public void setReturnTypeName(String returnTypeName) {
		this.returnTypeName = returnTypeName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}
	
	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public String toString() {
		String methodSignature = returnTypeName + " " + methodName;
		boolean firstParameter = true;
		for (Parameter p : parameters) {
			if (firstParameter == true) {
				methodSignature +=  "(" + p.getType() + " " + p.getName();
				firstParameter = false;
			} else {
				methodSignature += ", " + p.getType() + " " + p.getName();
			}
		}
		if (parameters.length != 0)
			methodSignature += ")";
		return methodSignature;
	}
	
}
