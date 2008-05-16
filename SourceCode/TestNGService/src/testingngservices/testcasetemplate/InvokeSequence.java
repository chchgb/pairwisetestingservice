package testingngservices.testcasetemplate;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pairwisetesting.util.TextFile;

@Deprecated
public class InvokeSequence {
	ArrayList<String> fileContent;
	private String methodSignature;

	public InvokeSequence(String srcFilePath) {
		fileContent = new TextFile(srcFilePath);
	}

	public void setScopeByMethod(String returnType, String methodName,
			Parameter... parameters) {
		methodSignature = returnType + " " + methodName;
		boolean firstParameter = true;
		for (Parameter p : parameters) {
			if (firstParameter == true) {
				methodSignature +=  "(" + p.getType() + " " + p.getName();
				firstParameter = false;
			} else {
				methodSignature += ", " + p.getType() + " " + p.getName();
			}
		}
		if (parameters.length != 0)
			methodSignature += ")";
	}

	/**
	 * @param fieldClassName
	 *            the className of the field
	 * @return the invoke sequences of the field with the given className
	 */
	public Invoke[] findByFieldType(String fieldClassName) {

		String fieldName = getFieldName(fieldClassName);

		Class<?> fieldClass = ClassUtil.getClass(fieldClassName);

		ArrayList<Invoke> invokeSequenceList = new ArrayList<Invoke>();

		// Match fieldName.xxxxx(xxxxx)
		String fieldInvokeRegex = "(" + fieldName + "[.](.*?)[(](.*?)[)])[; )]";
		Pattern fieldInvokePattern = Pattern.compile(fieldInvokeRegex);
		
		// Make field.invoke(xxxxx) -> field.invoke[(]xxxxx[)]
		Pattern methodSignaturePattern = Pattern.compile(methodSignature.replaceAll("([(]|[)])", "[$1]"));
		
		// this.localMethod(xxxxx)
		// localMethod(xxxxx)
		// = this.localMethod(xxxxx)
		// (this.localMethod(xxxxx))
		Pattern localInvokePattern = Pattern.compile("((?<=this[.])|[(]|\\s|=)(\\w+)[(].*?[)]");
		
		collectInvokeSequence(methodSignaturePattern, localInvokePattern,
				fieldInvokePattern, fieldClass, invokeSequenceList);

		return invokeSequenceList.toArray(new Invoke[0]);
	}

	/**
	 * @param fieldClassName
	 *            the class name of the field
	 * @return the field's name
	 */
	public String getFieldName(String fieldClassName) {
		String simpleClassName = ClassUtil.getSimpleClassName(fieldClassName);
		// AcountManager manager; | AcountManager manager = null;
		String fieldDeclarationRegex = simpleClassName + "[ ](.*?)[; =]";
		Pattern pattern = Pattern.compile(fieldDeclarationRegex);
		String fieldName = null;
		for (String line : fileContent) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				fieldName = matcher.group(1);
				break;
			}
		}
		return fieldName;
	}

	public String[] generateJMockInvokeSequenceByFieldType(String fieldClassName) {
		Invoke[] invokes = findByFieldType(fieldClassName);
		ArrayList<String> jMockInvokeSequence = new ArrayList<String>();
		for (Invoke invoke : invokes) {
			jMockInvokeSequence.add(invoke.getStatement().replaceFirst("(.*)[.](.*)", "atLeast(1).of ($1).$2"));
			if (! invoke.getReturnTypeName().equals("void")) {
				jMockInvokeSequence.add("will(returnValue(<NeedFilled>))");
			}
		}
		return jMockInvokeSequence.toArray(new String[0]);
	}
	
	private void collectInvokeSequence(Pattern methodSignaturePattern, 
			Pattern localInvokePattern, 
			Pattern fieldInvokePattern, 
			Class<?> fieldClass, 
			ArrayList<Invoke> invokeSequenceList) {
		
		int scopeMark = 0;
		boolean isMethodStart = false;
		
		for (String line : fileContent) {
			
			// Meet the end of the method
			if (scopeMark == 0 && isMethodStart == true)
				break;
			
			if (isMethodStart) {
				// Find the field Invoke
				Matcher macher = fieldInvokePattern.matcher(line);
				if (macher.find()) {
					Invoke invoke = new Invoke();
					invoke.setStatement(macher.group(1));
					String methodName = macher.group(2);
					invoke.setReturnTypeName(ClassUtil.getReturnTypeName(
							fieldClass, methodName));
					invokeSequenceList.add(invoke);
				}
				
				// Find the local invoke
				Matcher localInvokeMatcher = localInvokePattern.matcher(line);
				if (localInvokeMatcher.find()) {
					// group(2): the local method's name
					Pattern localMethodSignaturePattern = Pattern
							.compile("(public|private|protected) (.+?) " + localInvokeMatcher.group(2));
					collectInvokeSequence(localMethodSignaturePattern,
							localInvokePattern, fieldInvokePattern, fieldClass, invokeSequenceList);
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
