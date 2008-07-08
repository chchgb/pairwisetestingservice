package pairwisetesting.complex;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamMethodUnderTestXMLHelper implements
		IMethodUnderTestXMLHelper {

	private XStream xstream = new XStream(new DomDriver());

	public MethodUnderTest fromXML(String xml) {
		return (MethodUnderTest) xstream.fromXML(xml);
	}

	public String toXML(MethodUnderTest m) {
		return xstream.toXML(m);
	}

	public Object[] assign(String methodUnderTestXmlData, String[] values) {
		MethodUnderTest m = fromXML(methodUnderTestXmlData);
		ParameterAssignmentVisitor pv = new ParameterAssignmentVisitor(values);
		m.accept(pv);
		ArrayList<Object> objects = new ArrayList<Object>();
		for (String xmlParameter : pv.getXMLParameters()) {
			objects.add(xstream.fromXML(xmlParameter));
		}
		// System.out.println(xstream.toXML(objects.get(0)));
		// System.out.println(xstream.toXML(objects.get(1)));
		return objects.toArray();
	}
}

class ParameterAssignmentVisitor implements IParameterVisitor {

	private String[] values;
	private int next;
	private ArrayList<String> xmlParameters = new ArrayList<String>();
	private StringBuilder xmlParameter = new StringBuilder();

	ParameterAssignmentVisitor(String[] values) {
		this.values = values;
	}

	public void visit(SimpleParameter p) {
		beginTag(p);
		xmlParameter.append(values[next++]);
	}

	public void endVisit(SimpleParameter p) {
		endTag(p);
	}

	public void visit(ComplexParameter p) {
		beginTag(p);
	}

	public void endVisit(ComplexParameter p) {
		endTag(p);
	}

	public String[] getXMLParameters() {
		return this.xmlParameters.toArray(new String[0]);
	}

	private void beginTag(SimpleParameter p) {
		if (p.getDepth() == 0) {
			xmlParameter = new StringBuilder();
			xmlParameter.append("<" + p.getType() + ">");
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	private void beginTag(ComplexParameter p) {
		if (p.getDepth() == 0) {
			xmlParameter = new StringBuilder();
			xmlParameter.append("<" + p.getType() + ">");
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	private void endTag(Parameter p) {
		if (p.getDepth() == 0) {
			xmlParameter.append("</" + p.getType() + ">");
			xmlParameters.add(xmlParameter.toString());
		} else {
			xmlParameter.append("</" + p.getName() + ">");
		}
	}

}
