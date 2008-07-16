package pairwisetesting.complex;

import java.util.ArrayList;

public class ComplexParameter extends Parameter {

	private ArrayList<Parameter> children = new ArrayList<Parameter>();

	public ComplexParameter(String type, String name) {
		super(type, name, true);
		// Parameter[] parameters = new
		// ChildParametersExtractor().getParameters(type);
		// children.addAll(Arrays.asList(parameters));
	}

	public void add(Parameter child) {
		this.children.add(child);
		child.setDepth(this.getDepth() + 1);
		child.addFullNamePrefix(this.getFullName());
	}

	public void accept(IParameterVisitor pv) {
		pv.visit(this);
		for (Parameter child : children) {
			child.accept(pv);
		}
		pv.endVisit(this);
	}

	public void setDepth(int newDepth) {
		super.setDepth(newDepth);
		for (Parameter child : children) {
			child.setDepth(this.getDepth() + 1);
		}
	}

	public void addFullNamePrefix(String prefix) {
		super.addFullNamePrefix(prefix);
		for (Parameter child : children) {
			child.addFullNamePrefix(prefix);
		}
	}

	public Parameter[] getChildren() {
		return this.children.toArray(new Parameter[0]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexParameter other = (ComplexParameter) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		return true;
	}

}
