package testingngservices.testcasetemplate.core;

/**
 * This class acts as the finder to find the field name of some class.
 * 
 * @see testingngservices.testcasetemplate.ast.ASTFieldNameFinder
 * @see testingngservices.testcasetemplate.regex.RegexFieldNameFinder
 */
public interface IFieldNameFinder {
	
	/**
	 * Returns the field name with the specified field class name.
	 * 
	 * @param fieldClassName the specified field class name
	 * @return the field name with class name {@code fieldClassName}
 	 * @throws NullPointerException
	 *             if {@code fieldClassName} is null
	 */
	public String getFieldName(String fieldClassName);

}
