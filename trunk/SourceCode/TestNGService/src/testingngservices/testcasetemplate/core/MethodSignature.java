package testingngservices.testcasetemplate.core;

import java.util.ArrayList;

public class MethodSignature {
	
	private String returnTypeName = "<UNKNOWN>";
	private String methodName = "<UNKNOWN>";
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
	public MethodSignature() {
	}
	
	public MethodSignature(String returnTypeName,
			String methodName, Parameter... parameters) {
		this.returnTypeName = returnTypeName;
		this.methodName = methodName;
		setParameters(parameters);
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
	
	public void addParameter(String type, String name) {
		this.parameters.add(new Parameter(type, name));
	}
	
	public Parameter[] getParameters() {
		return this.parameters.toArray(new Parameter[0]);
	}
	
	public void setParameters(Parameter[] parameters) {
		for (Parameter p : parameters) {
			this.parameters.add(p);
		}
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
		if (parameters.size() != 0)
			methodSignature += ")";
		return methodSignature;
	}
	
}
