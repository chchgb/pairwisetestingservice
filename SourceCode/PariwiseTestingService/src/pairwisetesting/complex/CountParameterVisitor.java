package pairwisetesting.complex;

public class CountParameterVisitor extends DefaultParameterVisitor {

	private int branchCount = 0;
	private int leafCount = 0;
	
	public void visit(ComplexParameter p) {
		++branchCount;
	}
	
	public void visit(SimpleParameter p) {
		++leafCount;
	}

	public int getNodeCount() {
		return branchCount + leafCount;
	}

	public int getLeafCount() {
		return leafCount;
	}
	
	
}
