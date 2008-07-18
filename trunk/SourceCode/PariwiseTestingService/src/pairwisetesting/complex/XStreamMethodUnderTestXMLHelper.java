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
		m.getReturnValueParameter().accept(pv);
		ArrayList<Object> objects = new ArrayList<Object>();
		// System.out.println(Arrays.toString(pv.getXMLParameters()));
		for (String xmlParameter : pv.getXMLParameters()) {
			objects.add(xstream.fromXML(xmlParameter));
		}
//		System.out.println(xstream.toXML(objects.get(0)));
//		System.out.println(xstream.toXML(objects.get(1)));
//		System.out.println(xstream.toXML(objects.get(2)));
		return objects.toArray();
	}
}

class ParameterAssignmentVisitor implements IParameterVisitor {

	private String[] values;
	private int next;
	private ArrayList<String> xmlParameters = new ArrayList<String>();
	private StringBuilder xmlParameter = new StringBuilder();
	
	// Used to store the concrete type for endVisit usage
	private String currentConcreteType;

	ParameterAssignmentVisitor(String[] values) {
		this.values = values;
	}

	public void visit(SimpleParameter p) {
		beginTag(p);
		if (isSimpleArrayType(p)) {
			appendSimpleArrayValue(p);
		} else {
			xmlParameter.append(values[next++]);
		}
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
			if (isSimpleArrayType(p)) {
				xmlParameter.append("<" +  getXStreamArrayType(p.getType()) + ">");
			} else {
				xmlParameter.append("<" + p.getType() + ">");
			}
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	private void beginTag(ComplexParameter p) {
		if (p.getDepth() == 0) {
			xmlParameter = new StringBuilder();
			if (p.isAbstract()) {
				this.currentConcreteType = this.values[next++];
				xmlParameter.append("<" + this.currentConcreteType + ">");
			} else {
				xmlParameter.append("<" + p.getType() + ">");
			}
		} else {
			xmlParameter.append("<" + p.getName() + ">");
		}
	}

	private void endTag(Parameter p) {
		if (p.getDepth() == 0) {
			if (p.isAbstract()) {
				xmlParameter.append("</" + this.currentConcreteType + ">");
			} else {
				if (isSimpleArrayType(p)) {
					xmlParameter.append("</" + getXStreamArrayType(p.getType()) + ">");
				} else {
					xmlParameter.append("</" + p.getType() + ">");
				}
			}
			xmlParameters.add(xmlParameter.toString());
		} else {
			xmlParameter.append("</" + p.getName() + ">");
		}
	}
	
	// Currently all array types are considered simple
	private boolean isSimpleArrayType(Parameter p) {
		return p.getType().endsWith("[]");
	}
	
	private String getElementType(String arrayType) {
		return arrayType.replace("[]", "");
	}

	private String getXStreamArrayType(String arrayType) {
		return arrayType.replace("[]", "-array");
	}
	
	public String[] getElements(String arrayValue) {
		arrayValue = arrayValue.trim();
		if (arrayValue.length() <= 2) { // []
			return new String[0];
		} else {
			String elements = arrayValue.substring(1, arrayValue.length() - 1).trim();
			return elements.split("\\s*,\\s*");
		}
	}
	
	private void appendSimpleArrayValue(SimpleParameter p) {
		String elementType = getElementType(p.getType());
		String[] elements = getElements(values[next++]);
		// System.out.println(Arrays.toString(elements));
		for (String element : elements) {
			xmlParameter.append("<" + elementType + ">");
			xmlParameter.append(element);
			xmlParameter.append("</" + elementType + ">");
		}
	}
}
