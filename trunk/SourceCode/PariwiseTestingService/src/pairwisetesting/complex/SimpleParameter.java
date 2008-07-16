package pairwisetesting.complex;

public class SimpleParameter extends Parameter {

	public SimpleParameter(String type, String name) {
		super(type, name, false);
	}

	public void accept(IParameterVisitor pv) {
		pv.visit(this);
		pv.endVisit(this);
	}

}
