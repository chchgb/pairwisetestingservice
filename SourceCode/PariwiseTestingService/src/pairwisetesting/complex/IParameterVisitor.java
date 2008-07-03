package pairwisetesting.complex;

public interface IParameterVisitor {

	public void visit(SimpleParameter p);

	public void visit(ComplexParameter p);
	
	public void endVisit(SimpleParameter p);
	
	public void endVisit(ComplexParameter p);

}