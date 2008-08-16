package testingngservices.testcasetemplate.ast;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.IFieldNameFinder;

/**
 * The field name finder based on Eclipse AST.
 */
public class ASTFieldNameFinder implements IFieldNameFinder {

	private CompilationUnit unit;

	/**
	 * Constructs a field name finder with the specified compilation unit.
	 * 
	 * @param unit
	 *            the specified compilation unit
	 * @throws NullPointerException
	 *             if {@code unit} is null
	 */
	public ASTFieldNameFinder(CompilationUnit unit) {
		Preconditions.checkNotNull(unit, "compilation unit");
		this.unit = unit;
	}

	/**
	 * Constructs a field name finder with the specified source file path.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} is null
	 */
	public ASTFieldNameFinder(String sourceFilePath) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * testingngservices.testcasetemplate.core.IFieldNameFinder#getFieldName
	 * (java.lang.String)
	 */
	public String getFieldName(String fieldClassName) {
		Preconditions.checkNotNull(fieldClassName, "field class name");
		String fieldSimpleClassName = ClassUtil
				.getSimpleClassName(fieldClassName);
		FieldNameVisitor filedNameVisitor = new FieldNameVisitor(
				fieldSimpleClassName);
		unit.accept(filedNameVisitor);
		return filedNameVisitor.getFieldName();
	}

}

/**
 * This class acts as the visitor to visit the field declaration and then get 
 * the field name.
 */
class FieldNameVisitor extends ASTVisitor {
	private String fieldClassName;
	private String fieldName;

	/**
	 * Constructs a field name visitor with the specified field class name.
	 * 
	 * @param fieldClassName
	 *            the specified field class name
	 * @throws NullPointerException
	 *             if {@code fieldClassName} is null
	 */
	public FieldNameVisitor(String fieldClassName) {
		Preconditions.checkNotNull(fieldClassName, "field class name");
		this.fieldClassName = fieldClassName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * FieldDeclaration)
	 */
	public boolean visit(FieldDeclaration node) {
		if (node.getType().toString().equals(fieldClassName)) {
			fieldName = node.fragments().get(0).toString();
		}
		return false;
	}

	/**
	 * Returns the required field name after visiting all field declaration.
	 * 
	 * @return the required field name
	 */
	public String getFieldName() {
		return this.fieldName;
	}
}
