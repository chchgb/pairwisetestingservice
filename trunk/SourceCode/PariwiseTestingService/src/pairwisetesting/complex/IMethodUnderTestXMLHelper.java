package pairwisetesting.complex;

public interface IMethodUnderTestXMLHelper {

	String toXML(MethodUnderTest m);

	MethodUnderTest fromXML(String xml);

	/**
	 * @param methodUnderTestXmlData
	 *            the given XML that represents MethodUnderTest
	 * @param values
	 *            the values assigned to simple type
	 * @return the instances of the MethodUnderTest's parameters
	 */
	Object[] assign(String methodUnderTestXmlData, String[] values);

}
