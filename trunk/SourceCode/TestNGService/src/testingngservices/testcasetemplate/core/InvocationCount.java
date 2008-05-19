package testingngservices.testcasetemplate.core;

public enum InvocationCount {
	ONCE, ATLEAST_ONCE, ALLOWING, IGNORING;
	
	public boolean looserThan(InvocationCount other) {
		if (this.ordinal() > other.ordinal()) {
			return true;
		} else {
			return false;
		}
	}
	
	public InvocationCount plus(InvocationCount other) {
		if (this == ONCE) {
			throw new UnsupportedOperationException("Can't invoke on InvocationCount.ONCE");
		}
		if (this == ATLEAST_ONCE || other == ATLEAST_ONCE || other == ONCE) {
			return ATLEAST_ONCE;
		} else if (this == ALLOWING || other == ALLOWING) {
			return ALLOWING;
		} else {
			return IGNORING;
		}
	}
}
