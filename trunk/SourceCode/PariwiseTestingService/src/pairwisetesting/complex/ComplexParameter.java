package pairwisetesting.complex;

import java.util.ArrayList;

public class ComplexParameter extends Parameter {

	private ArrayList<Parameter> children = new ArrayList<Parameter>();

	public ComplexParameter(String type, String name) {
		super(type, name);
	}

	public void add(Parameter child) {
		this.children.add(child);
		child.setDepth(this.getDepth() + 1);
	}

	public void accept(IParameterVisitor pv) {
		pv.visit(this);
		for (Parameter child : children) {
			child.accept(pv);
		}
	}
	
	public void setDepth(int newDepth) {
		super.setDepth(newDepth);
		for (Parameter child : children) {
			child.setDepth(this.getDepth() + 1);
		}
	}

}
