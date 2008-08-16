package testingngservices.testcasetemplate.ast;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import com.google.common.base.Preconditions;

import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.MethodSignature;

/**
 * This class acts as the visitor to extract all the method invocations
 * specified to some field needed to mock within the specified method scope.
 */
class MockFieldMethodInvocationVisitor extends ASTVisitor {
	private String fieldName;
	private Map<String, MethodDeclaration> methodDeclarationMap;
	private MethodSignature scopeMethodSignature;
	private BufferedInvocationCollector invocationCollector
								= new BufferedInvocationCollector();
	private boolean startTestMethod = false;

	/**
	 * Constructs a method invocation visitor for mock field with the specified
	 * field name, method signature that determines the scope, and the method
	 * declaration map.
	 * 
	 * @param fieldName
	 *            the specified field name
	 * @param scopeMethodSignature
	 *            the specified method signature that determines the scope
	 * @param methodDeclarationMap
	 *            the specified method declaration map
	 * @throws NullPointerException
	 *             if {@code fieldName} or {@code scopeMethodSignature} or
	 *             {@code methodDeclarationMap} is null
	 */
	public MockFieldMethodInvocationVisitor(String fieldName,
			MethodSignature scopeMethodSignature,
			Map<String, MethodDeclaration> methodDeclarationMap) {
		Preconditions.checkNotNull(fieldName, "field name");
		Preconditions.checkNotNull(scopeMethodSignature,
				"method signature that determines the scope");
		Preconditions.checkNotNull(methodDeclarationMap,
				"method declaration map");
		this.fieldName = fieldName;
		this.scopeMethodSignature = scopeMethodSignature;
		this.methodDeclarationMap = methodDeclarationMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration node) {
		// TODO Currently only check the method name
		if (node.getName().toString().equals(
				scopeMethodSignature.getMethodName())) {
			startTestMethod = true;
		}
		if (startTestMethod) {
			node.accept(new FieldMethodInvocationVisitor(fieldName,
					invocationCollector, methodDeclarationMap,
					InvocationCount.ONCE, null));
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom
	 * .MethodDeclaration)
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		// TODO Currently only check the method name
		if (node.getName().toString().equals(
				scopeMethodSignature.getMethodName())) {
			startTestMethod = false;
		}
	}

	/**
	 * @see BufferedInvocationCollector#getInvocations()
	 */
	public Invocation[] getInvocations() {
		return this.invocationCollector.getInvocations();
	}
}


/**
 * This class acts as the visitor to extract all the method invocations
 * specified to some field within the specified method scope.
 */
class FieldMethodInvocationVisitor extends ASTVisitor {
	private String fieldName;
	private BufferedInvocationCollector invocationCollector;
	private Map<String, MethodDeclaration> methodDeclarationMap;
	private InvocationCount parentInvocationCount;
	private ReturnStatement firstReturnStatement;
	private MethodDeclaration parentMethodDeclaration;
	private MethodDeclaration myMethodDeclaration;

	/**
	 * Constructs a method invocation visitor for some field.
	 * 
	 * @param fieldName
	 *            the specified field name
	 * @param invocationsCollector
	 *            the specified method invocation collector
	 * @param methodDeclarationMap
	 *            the specified method declaration map
	 * @param parentInvocationCount
	 *            the method invocation node's parent invocation count
	 * @param parentMethodDeclaration
	 *            the method invocation node's parent method declaration
	 * @throws NullPointerException
	 *             if {@code fieldName} or {@code invocationsCollector} or
	 *             {@code methodDeclarationMap} or {@code parentInvocationCount}
	 *             is null
	 */
	public FieldMethodInvocationVisitor(String fieldName,
			BufferedInvocationCollector invocationsCollector,
			Map<String, MethodDeclaration> methodDeclarationMap,
			InvocationCount parentInvocationCount,
			MethodDeclaration parentMethodDeclaration) {
		Preconditions.checkNotNull(fieldName, "field name");
		Preconditions.checkNotNull(invocationsCollector, 
				"method invocation collector");
		Preconditions.checkNotNull(methodDeclarationMap, 
				"method declaration map");
		Preconditions.checkNotNull(parentInvocationCount, 
				"parent invocation count");
		this.fieldName = fieldName;
		this.invocationCollector = invocationsCollector;
		this.methodDeclarationMap = methodDeclarationMap;
		this.parentInvocationCount = parentInvocationCount;
		this.parentMethodDeclaration = parentMethodDeclaration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * ReturnStatement)
	 */
	@Override
	public boolean visit(ReturnStatement node) {
		if (null == firstReturnStatement) {
			firstReturnStatement = node;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * MethodDeclaration)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.core.dom.ASTVisitor#endVisit(org.eclipse.jdt.core.dom
	 * .MethodDeclaration)
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		this.invocationCollector.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.
	 * MethodInvocation)
	 */
	@Override
	public boolean visit(MethodInvocation node) {
		// Field method invocation
		if (node.getExpression() != null
				&& node.getExpression().toString().equals(fieldName)) {
			// System.out.println("" + node + node.getParent().getNodeType());
			// System.out.println("" + parent + (withinTryBlock(parent)
			// && !withinFinallyBlock(parent)));
			addInvocation(node, getMixedInvocationCount(node));
			// Local method invocation
		} else if (node.getExpression() == null
				|| node.getExpression().toString().equals("this")) {
			InvocationCount invocationCount = getMixedInvocationCount(node);
			MethodDeclaration childMethodDeclaration = methodDeclarationMap
					.get(node.getName().toString());
			childMethodDeclaration.accept(new FieldMethodInvocationVisitor(
					fieldName, invocationCollector, methodDeclarationMap,
					invocationCount, myMethodDeclaration));
		}
		return true;
	}

	/**
	 * Returns the mixed invocation count with parent's.
	 * 
	 * @param node
	 *            the specified method invocation
	 * @return the mixed invocation count with parent's
	 */
	private InvocationCount getMixedInvocationCount(MethodInvocation node) {

		InvocationCount myIvocationCount = null;

		// Invocation Level Rule: Loose -> Strict
		if (withinCatchClause(node)) {
			myIvocationCount = InvocationCount.IGNORING;
		} else if (withinTryBlock(node) && !withinFinallyBlock(node)
				|| withinIFBlock(node) || withinForBlock(node)
				|| withinWhileBlock(node) || hasReturnStatementAlready(node)) {
			myIvocationCount = InvocationCount.ALLOWING;
		} else if (withinDoBlock(node) || withinForStatement(node)
				|| withinWhileStatement(node) || withinDoStatement(node)) {
			myIvocationCount = InvocationCount.ATLEAST_ONCE;
		} else if (withinIFStatement(node)) {
			myIvocationCount = InvocationCount.ONCE;
		} else {
			myIvocationCount = InvocationCount.ONCE;
		}

		// Return the looser
		if (myIvocationCount.looserThan(parentInvocationCount)) {
			return myIvocationCount;
		} else {
			return parentInvocationCount;
		}
	}

	/**
	 * Adds a method invocation to invocation collector.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @param invocationCount
	 *            the specified invocation count
	 */
	private void addInvocation(MethodInvocation node,
			InvocationCount invocationCount) {
		invocationCollector.add(new Invocation(node.toString(),
				hasReturnValue(node), invocationCount));
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node has return
	 * value.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} has return value
	 */
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

	/**
	 * return <tt>true</tt> if there are return statements before the specified
	 * method invocation node.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if there are return statements before {@code node}
	 */
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

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * For Statement.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within For Statement
	 */
	private boolean withinForStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.FOR_STATEMENT)
				|| withControlStatement(node, ASTNode.ENHANCED_FOR_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * While Statement.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within While Statement
	 */
	private boolean withinWhileStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.WHILE_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * Do Statement.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within Do Statement
	 */
	private boolean withinDoStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.DO_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * If Statement.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within If Statement
	 */
	private boolean withinIFStatement(MethodInvocation node) {
		return withControlStatement(node, ASTNode.IF_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * For Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within For Block
	 */
	private boolean withinForBlock(MethodInvocation node) {
		return !withinForStatement(node)
				&& withinBlock(node, ASTNode.FOR_STATEMENT,
						ASTNode.ENHANCED_FOR_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * While Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within While Block
	 */
	private boolean withinWhileBlock(MethodInvocation node) {
		return !withinWhileStatement(node)
				&& withinBlock(node, ASTNode.WHILE_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * Do Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within Do Block
	 */
	private boolean withinDoBlock(MethodInvocation node) {
		return !withinDoStatement(node)
				&& withinBlock(node, ASTNode.DO_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * If Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within If Block
	 */
	private boolean withinIFBlock(MethodInvocation node) {
		return !withinIFStatement(node)
				&& withinBlock(node, ASTNode.IF_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * Try Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within Try Block
	 */
	private boolean withinTryBlock(MethodInvocation node) {
		return withinBlock(node, ASTNode.TRY_STATEMENT);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * Catch Clause.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within Catch Clause
	 */
	private boolean withinCatchClause(MethodInvocation node) {
		return withinBlock(node, ASTNode.CATCH_CLAUSE);
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * Finally Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @return <tt>true</tt> if {@code node} is within Finally Block
	 */
	private boolean withinFinallyBlock(MethodInvocation node) {
		ASTNode astNode = node;

		// Find the Try AST node
		while (astNode.getParent().getNodeType() != ASTNode.TRY_STATEMENT) {
			astNode = astNode.getParent();
		}
		TryStatement tryAstNode = (TryStatement) astNode.getParent();

		return withinFinallyBlock(node, tryAstNode.getFinally());
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * the specified Finally Block.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @param finallyBlock
	 *            the specified Finally Block
	 * @return <tt>true</tt> if {@code node} is within {@code finallyBlock}
	 */
	private boolean withinFinallyBlock(MethodInvocation node,
			ASTNode finallyBlock) {
		ASTNode astNode = node;
		while (astNode.getParent().getNodeType() != ASTNode.TRY_STATEMENT) {
			if (astNode.getParent() == finallyBlock) {
				return true;
			}
			astNode = astNode.getParent();
		}
		return false;
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * the node with the specified AST control node type.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @param ASTControlNodeType
	 *            the specified AST control node type
	 * @return Returns <tt>true</tt> if the specified method invocation node is
	 *         within the node with node type {@code ASTControlNodeType}
	 */
	private boolean withControlStatement(MethodInvocation node,
			int ASTControlNodeType) {
		int parentNodeType = node.getParent().getNodeType();
		int grandFatherNodeType = node.getParent().getParent().getNodeType();
		boolean exactWithin = parentNodeType == ASTControlNodeType;
		boolean expressionWithin = parentNodeType == ASTNode.INFIX_EXPRESSION
				&& grandFatherNodeType == ASTControlNodeType;
		return exactWithin || expressionWithin;
	}

	/**
	 * Returns <tt>true</tt> if the specified method invocation node is within
	 * the nodes with the specified AST node types.
	 * 
	 * @param node
	 *            the specified method invocation node
	 * @param ASTNodeTypes
	 *            the specified AST node types
	 * @return <tt>true</tt> if the specified method invocation node is within
	 *         the nodes with node types {@code ASTNodeTypes}
	 */
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
