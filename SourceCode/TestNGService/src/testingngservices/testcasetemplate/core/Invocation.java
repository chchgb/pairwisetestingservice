package testingngservices.testcasetemplate.core;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the method invocation related information.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 * 
 * @see InvocationCount
 */
public class Invocation {
	
	private String content;
	private boolean hasReturnValue;
	private InvocationCount count;

	/**
	 * Constructs a method invocation with the specified invocation related
	 * information.
	 * 
	 * @param content
	 *            the string representation of the invocation content
	 * @param hasReturnValue
	 *            if <tt>true</tt> then it has return value
	 * @param count
	 *            the invocation count
	 * @throws NullPointerException
	 *             if {@code content} or {@code count} is null
	 */
	public Invocation(String content, boolean hasReturnValue, 
			InvocationCount count) {
		Preconditions.checkNotNull(content, "content");
		Preconditions.checkNotNull(count, "the invocation count");
		this.content = content;
		this.hasReturnValue = hasReturnValue;
		this.count = count;
	}
	
	/**
	 * Constructs a empty method invocation.
	 */
	public Invocation() {
	}
	
	/**
	 * Returns the string representation of the invocation content.
	 * 
	 * @return the string representation of the invocation content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Returns <tt>true</tt> if the method invocation has return value.
	 * 
	 * @return <tt>true</tt> if the method invocation has return value
	 */
	public boolean hasReturnValue() {
		return hasReturnValue;
	}

	/**
	 * Sets <tt>true</tt> if the method invocation has return value, otherwise
	 * <tt>false</tt>.
	 * 
	 * @param hasReturnValue
	 *            <tt>true</tt> if the method invocation has return value,
	 *            otherwise <tt>false</tt>
	 */
	public void hasReturnValue(boolean hasReturnValue) {
		this.hasReturnValue = hasReturnValue;
	}

	/**
	 * Sets the content of the method invocation.
	 * 
	 * @param content the specified content of the method invocation
	 * @throws NullPointerException
	 *             if {@code content} is null
	 */
	public void setContent(String content) {
		Preconditions.checkNotNull(content, "content");
		this.content = content;
	}

	/**
	 * Returns the invocation count of the method invocation.
	 * 
	 * @return the invocation count of the method invocation
	 */
	public InvocationCount getCount() {
		return count;
	}

	/**
	 * Sets the invocation count of the method invocation
	 * 
	 * @param count the invocation count
	 * @throws NullPointerException
	 *             if {@code count} is null
	 */
	public void setCount(InvocationCount count) {
		Preconditions.checkNotNull(count, "the invocation count");
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + (hasReturnValue ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Invocation other = (Invocation) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (hasReturnValue != other.hasReturnValue)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.hasReturnValue + " " + this.content + " " + this.count;
	}

}
