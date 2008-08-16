package testingngservices.testcasetemplate.ast;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * This class acts as the visitor to visit all the method declarations.
 */
class MethodDeclarationMapVisitor extends ASTVisitor {

	private Map<String, MethodDeclaration> methodDeclarationMap
			= new HashMap<String, MethodDeclaration>();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// System.out.println(node.getName() + " " + node.getReturnType2());
		// Make sure it is not Constructor
		if (node.getReturnType2() != null) {
			methodDeclarationMap.put(node.getName().toString(), node);
		}
		return false;
	}

	/**
	 * Returns the method declaration map.
	 * 
	 * @return the method declaration map
	 */
	public Map<String, MethodDeclaration> getMethodDeclarationMap() {
		return this.methodDeclarationMap;
	}
}
