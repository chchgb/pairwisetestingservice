package testingngservices.testcasetemplate.core;

import java.util.ArrayList;

/**
 * This class acts as the finder to find the invocation sequence of some method.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public abstract class InvocationSequenceFinder {

	protected MethodSignature scopeMethodSignature = new MethodSignature();
	protected IFieldNameFinder fieldNameFinder;

	/**
	 * Returns an array of the method invocations with the specified field class
	 * name.
	 * 
	 * @param fieldClassName
	 *            the specified field class name
	 * @return an array of the method invocations with the specified field class
	 *         name
	 * @throws NullPointerException
	 *             if {@code fieldClassName} is null
	 */
	public abstract Invocation[] getInvocations(String fieldClassName);

	/**
	 * @see IFieldNameFinder#getFieldName(String)
	 */
	public String getFieldName(String fieldClassName) {
		return this.fieldNameFinder.getFieldName(fieldClassName);
	}

	/**
	 * Sets the scope to find method invocations by specified method signature.
	 * 
	 * @param returnTypeName
	 *            the return type name of the method signature
	 * @param methodName
	 *            the method name of the method signature
	 * @param parameters
	 *            the parameters of the method signature
	 * @throws NullPointerException
	 *             if {@code returnTypeName} or {@code methodName} is null
	 */
	public void setScopeByMethodSignature(String returnTypeName,
			String methodName, Parameter... parameters) {
		this.scopeMethodSignature = new MethodSignature(returnTypeName,
				methodName, parameters);
	}

	/**
	 * Returns the JMock method invocations with the specified field class name.
	 * 
	 * @param fieldClassName
	 *            the specified field class name
	 * @return the JMock method invocations with the specified field class name
	 * @throws NullPointerException
	 *             if {@code fieldClassName} is null
	 */
	public String[] getJMockInvocations(String fieldClassName) {
		Invocation[] invocations = getInvocations(fieldClassName);
		invocations = cleanRedundancyForJMock(invocations);
		return convertToJMockExpections(invocations);
	}

	/**
	 * Cleans redundancy for specified raw JMock method invocations.
	 * 
	 * @param rawInvocations
	 *            the specified raw JMock method invocations
	 * @return the JMock method invocations without redundancy
	 */
	private Invocation[] cleanRedundancyForJMock(Invocation[] rawInvocations) {
		for (int outterIndex = 0; outterIndex < rawInvocations.length; outterIndex++) {
			// Find the next invocation content to process
			if (rawInvocations[outterIndex] == null) {
				continue;
			}
			String invocationContent = rawInvocations[outterIndex].getContent();

			Invocation mixedLastInvocation = null;
			for (int innerIndex = outterIndex; innerIndex < rawInvocations.length; innerIndex++) {

				// Same invocation content is required
				if (rawInvocations[innerIndex] == null
						|| !invocationContent.equals(rawInvocations[innerIndex]
								.getContent()))
					continue;

				if (mixedLastInvocation == null) {
					// Find the first invocation that its InvocationCount is not
					// ONCE
					if (rawInvocations[innerIndex].getCount() != InvocationCount.ONCE) {
						mixedLastInvocation = rawInvocations[innerIndex];
					}
				} else {
					// Mix the invocation count
					InvocationCount mixedInvocationCount = mixedLastInvocation
							.getCount().plus(
									rawInvocations[innerIndex].getCount());
					mixedLastInvocation.setCount(mixedInvocationCount);
					// Remove the invocation has just been mixed
					rawInvocations[innerIndex] = null;
				}
			}
		}

		// Filter the invocations have just been mixed
		ArrayList<Invocation> cleanedInvocations = new ArrayList<Invocation>();
		for (Invocation invocation : rawInvocations) {
			if (invocation != null) {
				cleanedInvocations.add(invocation);
			}
		}
		return cleanedInvocations.toArray(new Invocation[0]);
	}

	/**
	 * Converts method invocations to JMock expection invocations
	 * 
	 * @param invocations
	 *            the specified method invocations
	 * @return the JMock expection invocations
	 */
	private String[] convertToJMockExpections(Invocation[] invocations) {
		// Convert to JMock Expections
		ArrayList<String> jMockInvocationContents = new ArrayList<String>();
		for (Invocation invocation : invocations) {
			String invocationCount = null;
			switch (invocation.getCount()) {
			case ONCE:
				invocationCount = "one";
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
			jMockInvocationContents.add(invocation.getContent().replaceFirst(
					"(.*)[.](.*)", invocationCount + " ($1).$2"));
			if (invocation.hasReturnValue()) {
				jMockInvocationContents.add("will(returnValue(<NeedFilled>))");
			}
		}
		return jMockInvocationContents.toArray(new String[0]);
	}

}
