package testingngservices.testcasetemplate.ast;

import java.util.ArrayList;

import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;

class BufferedInvocationCollector {
	private ArrayList<Invocation> invocationCollector = new ArrayList<Invocation>();
	private ArrayList<Invocation> invocationBuffer = new ArrayList<Invocation>();
 
	public void add(Invocation invocation) {
		invocationBuffer.add(invocation);
	}
	
	public void upgradeInvocationCount() {
		for (Invocation invocation : invocationBuffer) {
			if (invocation.getCount() == InvocationCount.ONCE) {
				invocation.setCount(InvocationCount.ATLEAST_ONCE);
			}
		}
	}
	
	public void flush() {
		invocationCollector.addAll(invocationBuffer);
		invocationBuffer.clear();
	}
	
	public Invocation[] getInvocations() {
		return this.invocationCollector.toArray(new Invocation[0]);
	}
}