package testingngservices.testcasetemplate.ast;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.google.common.base.Preconditions;

import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;

/**
 * The invocation sequence finder based on Eclipse AST.
 */
public class ASTInvocationSequenceFinder extends InvocationSequenceFinder {

	private CompilationUnit unit;

	/**
	 * Constructs an invocation sequence finder with the specified source file
	 * path.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} is null
	 */
	public ASTInvocationSequenceFinder(String sourceFilePath) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
		this.fieldNameFinder = new ASTFieldNameFinder(unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seetestingngservices.testcasetemplate.core.InvocationSequenceFinder#
	 * getInvocations(java.lang.String)
	 */
	@Override
	public Invocation[] getInvocations(String fieldClassName) {
		String fieldName = getFieldName(fieldClassName);
		MockFieldMethodInvocationVisitor visitor
			= new MockFieldMethodInvocationVisitor(
				fieldName, this.scopeMethodSignature, getMethodDeclarationMap());
		this.unit.accept(visitor);
		return visitor.getInvocations();
	}

	/**
	 * @see MethodDeclarationMapVisitor#getMethodDeclarationMap
	 */
	private Map<String, MethodDeclaration> getMethodDeclarationMap() {
		MethodDeclarationMapVisitor visitor = new MethodDeclarationMapVisitor();
		unit.accept(visitor);
		return visitor.getMethodDeclarationMap();
	}

}