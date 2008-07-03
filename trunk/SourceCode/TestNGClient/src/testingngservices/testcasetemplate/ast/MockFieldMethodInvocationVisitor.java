package testingngservices.testcasetemplate.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.MethodSignature;

class MockFieldMethodInvocationVisitor extends ASTVisitor {
	private String fieldName;
	private HashMap<String, MethodDeclaration> methodDeclarationMap;
	private MethodSignature scopeMethodSignature;
	private BufferedInvocationCollector invocationCollector = new BufferedInvocationCollector();
	private boolean startTestMethod = false;

	public MockFieldMethodInvocationVisitor(String fieldName,
			MethodSignature scopeMethodSignature,
			HashMap<String, MethodDeclaration> methodMap) {
		this.fieldName = fieldName;
		this.scopeMethodSignature = scopeMethodSignature;
		this.methodDeclarationMap = methodMap;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		// TODO Currently only check the method name
		if (node.getName().toString().equals(
				scopeMethodSignature.getMethodName())) {
			startTestMethod = true;
		}
		if (startTestMethod) {
			node.accept(new FieldMethodInvocationVisitor(fieldName,
					invocationCollector, methodDeclarationMap, InvocationCount.ONCE, null));
		}
		return true;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
		// TODO Currently only check the method name
		if (node.getName().toString().equals(
				scopeMethodSignature.getMethodName())) {
			startTestMethod = false;
		}
	}

	public Invocation[] getInvocations() {
		return this.invocationCollector.getInvocations();
	}
}

class FieldMethodInvocationVisitor extends ASTVisitor {
	private String fieldName;
	private BufferedInvocationCollector invocationCollector;
	private HashMap<String, MethodDeclaration> methodDeclarationMap;
	private InvocationCount parentInvocationCount;
	private ReturnStatement firstReturnStatement;
	private MethodDeclaration parentMethodDeclaration;
	private MethodDeclaration myMethodDeclaration;

	public FieldMethodInvocationVisitor(String fieldName,
			BufferedInvocationCollector invocationsCollector,
			HashMap<String, MethodDeclaration> methodMap, 
			InvocationCount parentInvocationCount,
			MethodDeclaration parentMethodDeclaration) {
		this.fieldName = fieldName;
		this.invocationCollector = invocationsCollector;
		this.methodDeclarationMap = methodMap;
		this.parentInvocationCount = parentInvocationCount;
		this.parentMethodDeclaration = parentMethodDeclaration;
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		if (null == firstReturnStatement) {
			firstReturnStatement = node;
		}
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		this.myMethodDeclaration = node;
		// Test if this is recursive invocation
		if (myMethodDeclaration == parentMethodDeclaration) {
			invocationCollector.upgradeInvocationCount();
			return false;
		} else {	
			// This method is not recursive.
			// Flush parent invocation buffer.
			invocationCollector.flush();
			return true;
		}
	}
	
	@Override
	public void endVisit(MethodDeclaration node) {
		this.invocationCollector.flush();
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		// Field method invocation
		if (node.getExpression() != null
				&& node.getExpression().toString().equals(fieldName)) {
			// System.out.println("" + node + node.getParent().getNodeType());
			// System.out.println("" + parent + (withinTryBlock(parent) && !withinFinallyBlock(parent)));
			addInvocation(node, getMixedInvocationCount(node));
		// Local method invocation
		} else if (node.getExpression() == null
				|| node.getExpression().toString().equals("this")) {
			InvocationCount invocationCount = getMixedInvocationCount(node); 
			MethodDeclaration childMethodDeclaration
				= methodDeclarationMap.get(node.getName().toString());
			childMethodDeclaration.accept(new FieldMethodInvocationVisitor(
					fieldName, invocationCollector, methodDeclarationMap, 
					invocationCount, myMethodDeclaration));
		}
		return true;
	}
	
	/**
	 * @return the mixed invocation count with parent's
	 */
	private InvocationCount getMixedInvocationCount(MethodInvocation node) {
		
		InvocationCount myIvocationCount = null;
		
		// TODO Invocation Count
		// Rule: Loose -> Strict
		if (withinCatchClause(node)) {
			myIvocationCount = InvocationCount.IGNORING;
		} else if (withinTryBlock(node) && !withinFinallyBlock(node)
				|| withinIFBlock(node)
				|| withinForBlock(node)
				|| withinWhileBlock(node)
				|| hasReturnStatementAlready(node)) {
			myIvocationCount = InvocationCount.ALLOWING;
		} else if (withinDoBlock(node) 
				|| withinForStatement(node)
				|| withinWhileStatement(node) 
				|| withinDoStatement(node)) {
			myIvocationCount = InvocationCount.ATLEAST_ONCE;
		} else if (withinIFStatement(node)) {
			myIvocationCount = InvocationCount.ONCE;
		} else {
			myIvocationCount = InvocationCount.ONCE;
		}
		
		// Return the Looser
		if (myIvocationCount.looserThan(parentInvocationCount)) {
			return myIvocationCount;
		} else {
			return parentInvocationCount;
		}
	}

	private void addInvocation(MethodInvocation node, InvocationCount invocationCount) {
		invocationCollector.add(new Invocation(node.toString(),hasReturnValue(node), invocationCount));
	}

	private boolean hasReturnValue(MethodInvocation node) {
		int parentNodeType = node.getParent().getNodeType();
		if (parentNodeType == ASTNode.ASSIGNMENT
				|| parentNodeType == ASTNode.VARIABLE_DECLARATION_FRAGMENT
				|| parentNodeType == ASTNode.INFIX_EXPRESSION
				|| parentNodeType == ASTNode.IF_STATEMENT
				|| parentNodeType == ASTNode.FOR_STATEMENT
				|| parentNodeType == ASTNode.ENHANCED_FOR_STATEMENT
				|| parentNodeType == ASTNode.WHILE_STATEMENT
				|| parentNodeType == ASTNode.RETURN_STATEMENT
				|| parentNodeType == ASTNode.METHOD_INVOCATION
				|| parentNodeType == ASTNode.DO_STATEMENT
				|| parentNodeType == ASTNode.PREFIX_EXPRESSION
				|| parentNodeType == ASTNode.CONDITIONAL_EXPRESSION
				|| parentNodeType == ASTNode.CLASS_INSTANCE_CREATION
				|| parentNodeType == ASTNode.ARRAY_INITIALIZER
				|| parentNodeType == ASTNode.CAST_EXPRESSION
				|| parentNodeType == ASTNode.INSTANCEOF_EXPRESSION) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean hasReturnStatementAlready(MethodInvocation node) {
		// No return statement found
		if (this.firstReturnStatement == null) {
			return false;
		}
		
		// Check whether node is in this return statement or not
		ASTNode astNode = node;
		while (astNode.getParent().getNodeType() != ASTNode.METHOD_DECLARATION) {
			if (astNode.getParent() == firstReturnStatement) {
				return false;
			}
			astNode = astNode.getParent();
		}
		return true;
	}

	private boolean withinForStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.FOR_STATEMENT)
				|| withControlStatement(node, ASTNode.ENHANCED_FOR_STATEMENT);
	} 
	
	private boolean withinWhileStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.WHILE_STATEMENT);
	} 
	
	private boolean withinDoStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.DO_STATEMENT);
	} 
	
	private boolean withinIFStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.IF_STATEMENT);
	}
	
	private boolean withinForBlock(MethodInvocation node) {
		return !withinForStatement(node)
				&& withinBlock(node, ASTNode.FOR_STATEMENT,
						ASTNode.ENHANCED_FOR_STATEMENT);
	}

	private boolean withinWhileBlock(MethodInvocation node) {
		return !withinWhileStatement(node)
				&& withinBlock(node, ASTNode.WHILE_STATEMENT);
	}

	private boolean withinDoBlock(MethodInvocation node) {
		return !withinDoStatement(node)
				&& withinBlock(node, ASTNode.DO_STATEMENT);
	}
	
	private boolean withinIFBlock(MethodInvocation node) {
		return !withinIFStatement(node) && withinBlock(node, ASTNode.IF_STATEMENT);
	}
	
	private boolean withinTryBlock(MethodInvocation node) {
		return withinBlock(node, ASTNode.TRY_STATEMENT);
	}
	
	private boolean withinCatchClause(MethodInvocation node) {
		return withinBlock(node, ASTNode.CATCH_CLAUSE);
	}
	
	private boolean withinFinallyBlock(MethodInvocation node) {
		ASTNode astNode = node;
		
		// Find the Try AST node
		while (astNode.getParent().getNodeType() != ASTNode.TRY_STATEMENT) {
			astNode = astNode.getParent();
		}
		TryStatement tryAstNode = (TryStatement)astNode.getParent();
		
		return withinFinallyBlock(node, tryAstNode.getFinally());
	}
	
	private boolean withinFinallyBlock(MethodInvocation node, ASTNode finallyBlock) {
		ASTNode astNode = node;
		while (astNode.getParent().getNodeType() != ASTNode.TRY_STATEMENT) {
			if (astNode.getParent() == finallyBlock) {
				return true;
			}
			astNode = astNode.getParent();
		}
		return false;
	}
	
	private boolean withControlStatement(MethodInvocation node, int ASTControlNodeType) {
		int parentNodeType = node.getParent().getNodeType();
		int grandFatherNodeType = node.getParent().getParent().getNodeType();
		boolean exactWithin = parentNodeType == ASTControlNodeType;
		boolean expressionWithin = parentNodeType == ASTNode.INFIX_EXPRESSION
				&& grandFatherNodeType == ASTControlNodeType;
		return exactWithin || expressionWithin;
	}
	
	private boolean withinBlock(MethodInvocation node, int... ASTNodeTypes) {
		ASTNode astNode = node;
		while (astNode.getParent().getNodeType() != ASTNode.METHOD_DECLARATION) {
			for (int ASTNodeType : ASTNodeTypes) {
				if (astNode.getParent().getNodeType() == ASTNodeType) {
					return true;
				}
			}
			astNode = astNode.getParent();
		}
		return false;
	}

}
