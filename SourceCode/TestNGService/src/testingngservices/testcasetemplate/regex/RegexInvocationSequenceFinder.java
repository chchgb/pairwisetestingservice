package testingngservices.testcasetemplate.regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.Invocation;
import testingngservices.testcasetemplate.core.InvocationCount;
import testingngservices.testcasetemplate.core.InvocationSequenceFinder;

public class RegexInvocationSequenceFinder extends InvocationSequenceFinder {
	
	private ArrayList<String> sourceFileContent;
	
	public RegexInvocationSequenceFinder(String sourceFilePath) {
		sourceFileContent = new TextFile(sourceFilePath);
		this.fieldNameFinder = new RegexFieldNameFinder(sourceFileContent);
	}
	
	@Override
	public Invocation[] getInvocations(String fieldClassName) {
		String fieldName = getFieldName(fieldClassName);
		
		Class<?> fieldClass = ClassUtil.getClass(fieldClassName);

		ArrayList<Invocation> invocationCollector = new ArrayList<Invocation>();

		// Match fieldName.xxxxx(xxxxx)
		String fieldInvokeRegex = "(" + fieldName + "[.](.*?)[(](.*?)[)])[; )]";
		Pattern fieldInvokePattern = Pattern.compile(fieldInvokeRegex);
		
		// Make field.invoke(xxxxx) -> field.invocation[(]xxxxx[)]
		Pattern methodSignaturePattern
			= Pattern.compile(scopeMethodSignature.toString().replaceAll("([(]|[)])", "[$1]"));
		
		// this.localMethod(xxxxx)
		// localMethod(xxxxx)
		// = this.localMethod(xxxxx)
		// (this.localMethod(xxxxx))
		Pattern localInvokePattern = Pattern.compile("((?<=this[.])|[(]|\\s|=)(\\w+)[(].*?[)]");
		
		collectInvokeSequence(methodSignaturePattern, localInvokePattern,
				fieldInvokePattern, fieldClass, invocationCollector);

		return invocationCollector.toArray(new Invocation[0]);
	}
	
	private void collectInvokeSequence(Pattern methodSignaturePattern, 
			Pattern localInvokePattern, 
			Pattern fieldInvokePattern, 
			Class<?> fieldClass, 
			ArrayList<Invocation> invocationCollector) {
		
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
					if (ClassUtil.getReturnTypeName(fieldClass, methodName).equals("void")) {
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
							.compile("(public|private|protected) (.+?) " + localInvokeMatcher.group(2));
					collectInvokeSequence(localMethodSignaturePattern,
							localInvokePattern, fieldInvokePattern, fieldClass, invocationCollector);
				}
			}
			
			// Check whether the current line is the start of the method
			Matcher methodSignatureMacher = methodSignaturePattern.matcher(line);
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
