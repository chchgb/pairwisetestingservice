package testingngservices.testcasetemplate.ast;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;

/**
 * This class acts as the method invocations collector to buffer the method
 * invocations in order to update the properties of the method invocations in
 * the buffer area when necessary.
 */
class BufferedInvocationCollector {
	private ArrayList<Invocation> invocationCollector = new ArrayList<Invocation>();
	private ArrayList<Invocation> invocationBuffer = new ArrayList<Invocation>();

	/**
	 * Adds a new method invocation to buffer.
	 * 
	 * @param invocation
	 *            the specified method invocation
	 * @throws NullPointerException
	 *             if {@code invocation} is null
	 */
	public void add(Invocation invocation) {
		Preconditions.checkNotNull(invocation, "invocation");
		invocationBuffer.add(invocation);
	}

	/**
	 * Updates the invocation count property of the method invocations in the 
	 * buffer area.
	 */
	public void upgradeInvocationCount() {
		for (Invocation invocation : invocationBuffer) {
			if (invocation.getCount() == InvocationCount.ONCE) {
				invocation.setCount(InvocationCount.ATLEAST_ONCE);
			}
		}
	}

	/**
	 * Flushes the method invocations in the buffer area to reachable area. 
	 */
	public void flush() {
		invocationCollector.addAll(invocationBuffer);
		invocationBuffer.clear();
	}

	/**
	 * Returns an array of the method invocations in the reachable area.
	 * 
	 * @return an array of the method invocations in the reachable area
	 */
	public Invocation[] getInvocations() {
		return this.invocationCollector.toArray(new Invocation[0]);
	}
}