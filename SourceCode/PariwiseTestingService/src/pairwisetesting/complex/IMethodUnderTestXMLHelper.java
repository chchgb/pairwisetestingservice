package pairwisetesting.complex;

public interface IMethodUnderTestXMLHelper {

	String toXML(MethodUnderTest m);

	MethodUnderTest fromXML(String xml);

	void assign(MethodUnderTest m, String[] values);

}
