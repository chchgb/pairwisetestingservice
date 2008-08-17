package testingngservices.testcasetemplate;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.MethodSignature;

/**
 * This class acts as the finder to find the method signature based on 
 * Eclipse AST.
 * 
 * @see MethodSignature
 */
public class MethodSignatureFinder {
	
	private CompilationUnit unit;

	/**
	 * Constructs a method signature finder with the specified source file path.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} is null
	 */
	public MethodSignatureFinder(String sourceFilePath) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(TextFile.read(sourceFilePath).toCharArray());
		this.unit = (CompilationUnit) parser.createAST(null);
	}

	/**
	 * Returns the method signature object with the specified return type and
	 * method name.
	 * 
	 * @param returnTypeName
	 *            the specified method's return type name
	 * @param methodName
	 *            the specified method name
	 * @return the method signature object with the specified return type and
	 * method name
	 * @throws NullPointerException
	 *             if {@code returnTypeName} or {@code methodName} is null
	 */
	public MethodSignature getMethodSignature(String returnTypeName, 
			String methodName) {
		Preconditions.checkNotNull(returnTypeName, "return type name");
		Preconditions.checkNotNull(methodName, "method name");
		String simpleReturnTypeName = 
			ClassUtil.getSimpleClassName(returnTypeName);
		// System.out.println(simpleReturnTypeName);
		MethodSignatureVisitor visitor
			= new MethodSignatureVisitor(simpleReturnTypeName, methodName);
		unit.accept(visitor);
		return visitor.getMethodSignature();
	}
	
}

/**
 * This class acts as the visitor to visit all method declarations and get the 
 * required method signature.
 */
class MethodSignatureVisitor extends ASTVisitor {

	private MethodSignature ms = new MethodSignature();
	private String returnTypeName;
	private String methodName;
	
	/**
	 * Constructs a method signature visitor with the specified source file 
	 * path.
	 * 
	 * @param returnTypeName
	 *            the specified method's return type name
	 * @param methodName
	 *            the specified method name
	 */
	public MethodSignatureVisitor(String returnTypeName, String methodName) {
		this.returnTypeName = returnTypeName;
		this.methodName = methodName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// System.out.println(node + " " + node.getReturnType2());
		if (node.getReturnType2() != null // Make sure it is not Constructor
				&& node.getReturnType2().toString().equals(returnTypeName)
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
	
	/**
	 * Returns the required method signature.
	 * 
	 * @return the required method signature
	 */
	public MethodSignature getMethodSignature() {
		return this.ms;
	}

}
