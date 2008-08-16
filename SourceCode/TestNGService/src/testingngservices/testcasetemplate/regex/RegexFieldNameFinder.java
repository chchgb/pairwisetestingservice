package testingngservices.testcasetemplate.regex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.TextFile;
import testingngservices.testcasetemplate.core.IFieldNameFinder;

import com.google.common.base.Preconditions;

/**
 * The field name finder based on Regular Expressions.
 */
public class RegexFieldNameFinder implements IFieldNameFinder {

	private List<String> sourcefileContent;

	/**
	 * Constructs a field name finder with the specified source file path.
	 * 
	 * @param sourceFilePath
	 *            the specified source file path
	 * @throws NullPointerException
	 *             if {@code sourceFilePath} is null
	 */
	public RegexFieldNameFinder(String sourceFilePath) {
		Preconditions.checkNotNull(sourceFilePath, "source file path");
		this.sourcefileContent = new TextFile(sourceFilePath);
	}

	/**
	 * Constructs a field name finder with the specified source file content.
	 * 
	 * @param sourcefileContent
	 *            the specified source file content
	 * @throws NullPointerException
	 *             if {@code sourcefileContent} is null
	 */
	public RegexFieldNameFinder(List<String> sourcefileContent) {
		Preconditions.checkNotNull(sourcefileContent, "source file content");
		this.sourcefileContent = sourcefileContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * testingngservices.testcasetemplate.core.IFieldNameFinder#getFieldName
	 * (java.lang.String)
	 */
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
