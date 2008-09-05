package testingngservices.testcasetemplate.core;

import com.google.common.base.Preconditions;

/**
 * This enum class provides the different method invocation count.
 */
public enum InvocationCount {

	/**
	 * 1 time.
	 */
	ONCE(1),
	/**
	 * 1..* times.
	 */
	ATLEAST_ONCE(2),
	/**
	 * 0..* times and hope that the method invocation should run.
	 */
	ALLOWING(3),
	/**
	 * 0..* times and hope that the method invocation should not run.
	 */
	IGNORING(4);

	private int level;

	/**
	 * Constructs an invocation count with the specified level. The level
	 * indicates the strictness of invocation. The smaller the level is, the
	 * stricter the invocation is.
	 * 
	 * @param level
	 *            the specified level.
	 */
	private InvocationCount(int level) {
		this.level = level;
	}

	/**
	 * Returns <tt>true</tt> if the invocation level is looser than another.
	 * 
	 * @param other
	 *            another invocation count
	 * @return <tt>true</tt> if the invocation level is looser than {@code
	 *         other}
	 * @throws NullPointerException
	 *             if {@code other} is null
	 */
	public boolean looserThan(InvocationCount other) {
		Preconditions.checkNotNull(other, "another invocation count");
		if (this.level > other.level) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a mixed invocation count with another invocation count.
	 * 
	 * @param other
	 *            another invocation count
	 * @return a mixed invocation count with {@code other}
	 * @throws NullPointerException
	 *             if {@code other} is nulls
	 */
	public InvocationCount plus(InvocationCount other) {
		Preconditions.checkNotNull(other, "another invocation count");
		if (this == ONCE) {
			throw new UnsupportedOperationException(
					"Can't invoke on InvocationCount.ONCE");
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
