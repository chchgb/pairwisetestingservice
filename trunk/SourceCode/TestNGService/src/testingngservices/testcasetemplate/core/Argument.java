package testingngservices.testcasetemplate.core;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the actual parameter assigned to method or
 * constructor.
 */
public class Argument {

	private String value;
	private String type;

	/**
	 * Constructs a argument with the specified type and value.
	 * 
	 * @param type
	 *            the specified type
	 * @param value
	 *            the specified value
	 * @throws NullPointerException
	 *             if {@code type} or {@code value} is null
	 */
	public Argument(String type, String value) {
		Preconditions.checkNotNull(type, "type");
		Preconditions.checkNotNull(value, "value");
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the string representation of the value of argument.
	 * 
	 * @return the string representation of the value of argument
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the string representation of the type of argument.
	 * 
	 * @return the string representation of the type of argument
	 */
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		final Argument other = (Argument) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}