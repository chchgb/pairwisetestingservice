package pairwisetesting.complex;

public abstract class Parameter {

	private String type;
	private String name = "";
	private String fullNamePrefix = "";
	private int depth;

	public Parameter(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public String getFullName() {
		return this.fullNamePrefix + this.name;
	}
	
	public void addFullNamePrefix(String prefix) {
		this.fullNamePrefix = prefix + "." + this.fullNamePrefix;
	}
	
	public String toString() {
		return String.format("[%s %s]", type, getFullName());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + depth;
		result = prime * result
				+ ((fullNamePrefix == null) ? 0 : fullNamePrefix.hashCode());
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
		Parameter other = (Parameter) obj;
		if (depth != other.depth)
			return false;
		if (fullNamePrefix == null) {
			if (other.fullNamePrefix != null)
				return false;
		} else if (!fullNamePrefix.equals(other.fullNamePrefix))
			return false;
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

	public abstract void accept(IParameterVisitor pv);

}
