package testingngservices.testcasetemplate.regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.IFieldNameFinder;
import testingngservices.testcasetemplate.util.ClassUtil;

/**
 * Finding field name based on Regular Expression
 */
public class RegexFieldNameFinder implements IFieldNameFinder {
	
	private ArrayList<String> sourcefileContent;
	
	public RegexFieldNameFinder(String sourceFilePath) {
		this.sourcefileContent = new TextFile(sourceFilePath);
	}
	
	public RegexFieldNameFinder(ArrayList<String> sourcefileContent) {
		this.sourcefileContent = sourcefileContent;
	}
	
	public String getFieldName(String fieldClassName) {
		String simpleClassName = ClassUtil.getSimpleClassName(fieldClassName);
		// e.g. 
		// AcountManager manager;
		// AcountManager manager = null; 
		String fieldDeclarationRegex = simpleClassName + "[ ](.*?)[; =]";
		Pattern pattern = Pattern.compile(fieldDeclarationRegex);
		String fieldName = null;
		for (String line : sourcefileContent) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				fieldName = matcher.group(1);
				break;
			}
		}
		return fieldName;
	}

}
