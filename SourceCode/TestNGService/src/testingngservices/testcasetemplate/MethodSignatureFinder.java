package testingngservices.testcasetemplate;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.MethodSignature;

public class MethodSignatureFinder {
	private CompilationUnit unit;
	
	public MethodSignatureFinder(String sourceFilePath) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
	}

	public MethodSignature getMethodSignature(String returnTypeName, String methodName) {
		MethodSignatureVisitor visitor
			= new MethodSignatureVisitor(returnTypeName, methodName);
		unit.accept(visitor);
		return visitor.getMethodSignature();
	}
	
}

class MethodSignatureVisitor extends ASTVisitor {

	private MethodSignature ms = new MethodSignature();
	private String returnTypeName;
	private String methodName;
	
	
	public MethodSignatureVisitor(String returnTypeName, String methodName) {
		this.returnTypeName = returnTypeName;
		this.methodName = methodName;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (node.getReturnType2().toString().equals(returnTypeName)
				&& node.getName().toString().equals(methodName)) {
			ms.setReturnTypeName(returnTypeName);
			ms.setMethodName(methodName);
			for (Object obj : node.parameters()) {
				String[] pair = obj.toString().split(" ");
				ms.addParameter(pair[0], pair[1]);
			}
		}
		return false;
	}
	
	public MethodSignature getMethodSignature() {
		return this.ms;
	}

}
