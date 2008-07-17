package pairwisetesting.complex;


public class SimpleParameter extends Parameter {

	public SimpleParameter(String type, String name) {
		super(type, name);
	}

	@Override
	public void accept(IParameterVisitor pv) {
		pv.visit(this);
		pv.endVisit(this);
	}
	
	@Override
	public boolean isComplex() {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

}
