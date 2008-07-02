package pairwisetesting.complex;

public abstract class Parameter {

	private String type;
	private String name;
	private int depth;
	
	public Parameter(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Parameter(String type, String name, int depth) {
		this(type, name);
		this.depth = depth;
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
	
	public String toString() {
		return String.format("[%s %s]", type, name);
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public abstract void accept(IParameterVisitor pv);

}
