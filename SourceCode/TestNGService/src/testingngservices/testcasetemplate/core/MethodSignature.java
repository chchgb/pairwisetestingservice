package testingngservices.testcasetemplate.core;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the method signature related information.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class MethodSignature {

	private String returnTypeName = "<UNKNOWN>";
	private String methodName = "<UNKNOWN>";
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * Construct a empty method signature.
	 */
	public MethodSignature() {
	}

	/**
	 * Construct a method signature with specified signature related
	 * information.
	 * 
	 * @param returnTypeName
	 *            the return type name
	 * @param methodName
	 *            the method name
	 * @param parameters
	 *            the method's parameters
	 * @throws NullPointerException
	 *             if {@code returnTypeName} or {@code methodName} is null
	 */
	public MethodSignature(String returnTypeName, String methodName,
			Parameter... parameters) {
		Preconditions.checkNotNull(returnTypeName, "return type name");
		Preconditions.checkNotNull(methodName, "method name");
		this.returnTypeName = returnTypeName;
		this.methodName = methodName;
		setParameters(parameters);
	}

	/**
	 * Returns the return type name.
	 * 
	 * @return the return type name
	 */
	public String getReturnTypeName() {
		return returnTypeName;
	}

	/**
	 * Sets the return type name.
	 * 
	 * @param returnTypeName
	 *            the specified return type name
	 * @throws NullPointerException
	 *             if {@code returnTypeName} is null
	 */
	public void setReturnTypeName(String returnTypeName) {
		Preconditions.checkNotNull(returnTypeName, "return type name");
		this.returnTypeName = returnTypeName;
	}

	/**
	 * Returns the method name.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets the method name.
	 * 
	 * @param methodName
	 *            the specified method name
	 * @throws NullPointerException
	 *             if {@code methodName} is null
	 */
	public void setMethodName(String methodName) {
		Preconditions.checkNotNull(methodName, "method name");
		this.methodName = methodName;
	}

	/**
	 * Adds a new parameter to the method signature.
	 * 
	 * @param type
	 *            the specified parameter's type
	 * @param name
	 *            the specified parameter's name
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public void addParameter(String type, String name) {
		Preconditions.checkNotNull(type, "type");
		Preconditions.checkNotNull(name, "name");
		this.parameters.add(new Parameter(type, name));
	}

	/**
	 * Returns an array containing all the parameters of the method signature.
	 * 
	 * @return an array containing all the parameters of the method signature.
	 */
	public Parameter[] getParameters() {
		return this.parameters.toArray(new Parameter[0]);
	}

	/**
	 * Sets the parameters of the method signature.
	 * 
	 * @param parameters
	 *            the parameters of the method signature
	 * @throws NullPointerException
	 *             if {@code parameters} is null
	 */
	public void setParameters(Parameter[] parameters) {
		Preconditions.checkNotNull(parameters, "parameters");
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
				methodSignature += "(" + p.getType() + " " + p.getName();
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
