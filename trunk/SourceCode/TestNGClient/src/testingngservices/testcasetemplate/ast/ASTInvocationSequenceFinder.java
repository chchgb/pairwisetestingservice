package testingngservices.testcasetemplate.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;

public class ASTInvocationSequenceFinder extends InvocationSequenceFinder {

	private CompilationUnit unit;
	
	public ASTInvocationSequenceFinder(String sourceFilePath) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
		this.fieldNameFinder = new ASTFieldNameFinder(unit);
	}

	@Override
	public Invocation[] getInvocations(String fieldClassName) {
		String fieldName = getFieldName(fieldClassName);
		MockFieldMethodInvocationVisitor visitor = new MockFieldMethodInvocationVisitor(
				fieldName, this.scopeMethodSignature, getMethodDeclarationMap());
		this.unit.accept(visitor);
		return visitor.getInvocations();
	}
	
	private HashMap<String, MethodDeclaration> getMethodDeclarationMap() {
		MethodDeclarationMapVisitor visitor = new MethodDeclarationMapVisitor();
		unit.accept(visitor);
		return visitor.getMethodDeclarationMap();
	}

}