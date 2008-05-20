package testingngservices.testcasetemplate.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

class MethodDeclarationMapVisitor extends ASTVisitor {

	private HashMap<String, MethodDeclaration> methodDeclarationMap = new HashMap<String, MethodDeclaration>();

	@Override
	public boolean visit(MethodDeclaration node) {
		// System.out.println(node.getName() + " " + node.getReturnType2());
		// Make sure it is not Constructor
		if (node.getReturnType2() != null) {
			methodDeclarationMap.put(node.getName().toString(), node);
		}
		return false;
	}

	public HashMap<String, MethodDeclaration> getMethodDeclarationMap() {
		return this.methodDeclarationMap;
	}
}
