package testingngservices.testcasetemplate.core;

import java.util.ArrayList;

import testingngservices.testcasetemplate.Parameter;

public abstract class InvocationSequenceFinder {

	protected MethodSignature scopeMethodSignature = new MethodSignature();
	protected IFieldNameFinder fieldNameFinder;
	
	/**
	 * @param fieldClassName
	 *            the className of the field
	 * @return the invocation sequences of the field with the given className
	 */
	public abstract Invocation[] getInvocations(String fieldClassName);

	/**
	 * @param fieldClassName
	 *            the className of the field
	 * @return return field's name
	 */
	public String getFieldName(String fieldClassName) {
		return this.fieldNameFinder.getFieldName(fieldClassName);
	}
	
	public void setScopeByMethodDeclaration(String returnTypeName,
			String methodName, Parameter... parameters) {
		this.scopeMethodSignature = new MethodSignature(returnTypeName,
				methodName, parameters);
	}

	public String[] getJMockInvocations(String fieldClassName) {
		Invocation[] invocations = getInvocations(fieldClassName);
		ArrayList<String> jMockInvocations = new ArrayList<String>();
		String invocationCount = null;
		for (Invocation invocation : invocations) {
			switch (invocation.getCount()) {
			case ONCE:
				invocationCount = "once";
				break;
			case ATLEAST_ONCE:
				invocationCount = "atLeast(1).of";
				break;
			case ALLOWING:
				invocationCount = "allowing";
				break;
			case IGNORING: 
				invocationCount = "ignoring";
				break;
			}
			jMockInvocations.add(invocation.getContent().replaceFirst(
					"(.*)[.](.*)", invocationCount + " ($1).$2"));
			if (invocation.hasReturnValue()) {
				jMockInvocations.add("will(returnValue(<NeedFilled>))");
			}
		}
		return jMockInvocations.toArray(new String[0]);
	}
	
}
