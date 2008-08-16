package testingngservices.testcasetemplate.core;

import com.google.common.base.Preconditions;

/**
 * This class encapsulates the formal parameter belongs to method or
 * constructor.
 */
public class Parameter {

	private String name;
	private String type;

	/**
	 * Constructs a parameter with the specified type and value.
	 * 
	 * @param type
	 *            the specified type
	 * @param name
	 *            the specified name
	 * @throws NullPointerException
	 *             if {@code type} or {@code name} is null
	 */
	public Parameter(String type, String name) {
		Preconditions.checkNotNull(type, "type");
		Preconditions.checkNotNull(name, "name");
		this.type = type;
		this.name = name;
	}

	/**
	 * Returns the name of parameter.
	 * 
	 * @return the name of parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the string representation of the type of parameter.
	 * 
	 * @return the string representation of the type of parameter
	 */
	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		final Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}