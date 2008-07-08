package testingngservices.testcasetemplate.ast;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.IFieldNameFinder;

/**
 * Finding field name based on AST
 */
public class ASTFieldNameFinder implements IFieldNameFinder {
	
	private CompilationUnit unit;
	
	public ASTFieldNameFinder(CompilationUnit unit) {
		this.unit = unit;
	}

	public ASTFieldNameFinder(String sourceFilePath) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
	}

	public String getFieldName(String fieldClassName) {
		String fieldSimpleClassName = ClassUtil.getSimpleClassName(fieldClassName);
		FieldNameVisitor filedNameVisitor = new FieldNameVisitor(fieldSimpleClassName);
		unit.accept(filedNameVisitor);
		return filedNameVisitor.getFieldName();
	}

}

class FieldNameVisitor extends ASTVisitor {
	private String fieldClassName;
	private String fieldName;

	public FieldNameVisitor(String fieldClassName) {
		this.fieldClassName = fieldClassName;
	}

	public boolean visit(FieldDeclaration node) {
		if (node.getType().toString().equals(fieldClassName)) {
			fieldName = node.fragments().get(0).toString();
		}
		return false;
	}

	public String getFieldName() {
		return this.fieldName;
	}
}
