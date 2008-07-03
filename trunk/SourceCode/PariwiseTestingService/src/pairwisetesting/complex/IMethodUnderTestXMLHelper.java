package pairwisetesting.complex;

public interface IMethodUnderTestXMLHelper {

	String toXML(MethodUnderTest m);

	MethodUnderTest fromXML(String xml);

	 Object[] assign(MethodUnderTest m, String[] values);

}
