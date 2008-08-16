package testingngservices.testcasetemplate.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;

/**
 * The invocation sequence finder based on Regular Expressions.
 */
public class RegexInvocationSequenceFinder extends InvocationSequenceFinder {

	private List<String> sourceFileContent;

	/**
	 * Constructs an invocation sequence finder with the specified source file
	 * path.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} is null
	 */
	public RegexInvocationSequenceFinder(String sourceFilePath) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		sourceFileContent = new TextFile(sourceFilePath);
		this.fieldNameFinder = new RegexFieldNameFinder(sourceFileContent);
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

		Class<?> fieldClass = ClassUtil.getClass(fieldClassName);

		List<Invocation> invocationCollector = new ArrayList<Invocation>();

		// Match fieldName.xxxxx(xxxxx)
		String fieldInvokeRegex = "(" + fieldName + "[.](.*?)[(](.*?)[)])[; )]";
		Pattern fieldInvokePattern = Pattern.compile(fieldInvokeRegex);

		// Make field.invoke(xxxxx) -> field.invocation[(]xxxxx[)]
		Pattern methodSignaturePattern = Pattern.compile(scopeMethodSignature
				.toString().replaceAll("([(]|[)])", "[$1]"));

		// this.localMethod(xxxxx)
		// localMethod(xxxxx)
		// = this.localMethod(xxxxx)
		// (this.localMethod(xxxxx))
		Pattern localInvokePattern = Pattern
				.compile("((?<=this[.])|[(]|\\s|=)(\\w+)[(].*?[)]");

		collectInvocationSequence(methodSignaturePattern, localInvokePattern,
				fieldInvokePattern, fieldClass, invocationCollector);

		return invocationCollector.toArray(new Invocation[0]);
	}

	/**
	 * Collects method invocations based on Regular Expressions.
	 * 
	 * @param methodSignaturePattern
	 *            the method signature pattern
	 * @param localInvokePattern
	 *            the local method invocation pattern
	 * @param fieldInvokePattern
	 *            the method invocation pattern on field
	 * @param fieldClass
	 *            the field class object
	 * @param invocationCollector
	 *            the collector to collect the method invocations
	 */
	private void collectInvocationSequence(Pattern methodSignaturePattern,
			Pattern localInvokePattern, Pattern fieldInvokePattern,
			Class<?> fieldClass, List<Invocation> invocationCollector) {

		int scopeMark = 0;
		boolean isMethodStart = false;

		for (String line : sourceFileContent) {

			// Meet the end of the method
			if (scopeMark == 0 && isMethodStart == true)
				break;

			if (isMethodStart) {
				// Find the field Invoke
				Matcher macher = fieldInvokePattern.matcher(line);
				if (macher.find()) {
					Invocation invocation = new Invocation();
					invocation.setContent(macher.group(1));
					String methodName = macher.group(2);
					if (ClassUtil.getReturnTypeName(fieldClass, methodName)
							.equals("void")) {
						invocation.hasReturnValue(false);
					} else {
						invocation.hasReturnValue(true);
					}
					invocation.setCount(InvocationCount.ONCE);
					invocationCollector.add(invocation);
				}

				// Find the local invocation
				Matcher localInvokeMatcher = localInvokePattern.matcher(line);
				if (localInvokeMatcher.find()) {
					// group(2): the local method's name
					Pattern localMethodSignaturePattern = Pattern
							.compile("(public|private|protected) (.+?) "
									+ localInvokeMatcher.group(2));
					collectInvocationSequence(localMethodSignaturePattern,
							localInvokePattern, fieldInvokePattern, fieldClass,
							invocationCollector);
				}
			}

			// Check whether the current line is the start of the method
			Matcher methodSignatureMacher = methodSignaturePattern
					.matcher(line);
			if (methodSignatureMacher.find()) {
				isMethodStart = true;
			}

			// Mark of start of the block with the method scope
			if (isMethodStart && line.contains("{")) {
				scopeMark++;
			}
			// Mark of end of the block with the method scope
			if (isMethodStart && line.contains("}")) {
				scopeMark--;
			}
		}
	}

}
