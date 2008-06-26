package testingngservices.testcasetemplate.core;

import java.util.ArrayList;

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
	
	public void setScopeByMethodSignature(String returnTypeName,
			String methodName, Parameter... parameters) {
		this.scopeMethodSignature = new MethodSignature(returnTypeName,
				methodName, parameters);
	}

	public String[] getJMockInvocations(String fieldClassName) {
		Invocation[] invocations = getInvocations(fieldClassName);
		invocations = cleanRedundancyForJMock(invocations);
		return convertToJMockExpections(invocations);
	}

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
						|| !invocationContent.equals(rawInvocations[innerIndex].getContent()))
					continue;
	
				if (mixedLastInvocation == null) {
					// Find the first invocation that its InvocationCount is not ONCE
					if (rawInvocations[innerIndex].getCount() != InvocationCount.ONCE) {
						mixedLastInvocation = rawInvocations[innerIndex];
					}
				} else {
					// Mix the invocation count
					InvocationCount mixedInvocationCount
						= mixedLastInvocation.getCount().plus(rawInvocations[innerIndex].getCount());
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
