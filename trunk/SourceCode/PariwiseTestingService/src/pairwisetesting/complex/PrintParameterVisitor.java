package pairwisetesting.complex;

public class PrintParameterVisitor extends DefaultParameterVisitor {

	public void visit(SimpleParameter p) {
		for (int i = 0; i < p.getDepth(); i++) {
			System.out.print("\t");
		}
		System.out.println(p);
	}
	
	public void visit(ComplexParameter p) {
		for (int i = 0; i < p.getDepth(); i++) {
			System.out.print("\t");
		}
		System.out.println(p);
	}

}
