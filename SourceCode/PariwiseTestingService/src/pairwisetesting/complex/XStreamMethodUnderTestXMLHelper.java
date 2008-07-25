package pairwisetesting.complex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		} else if (isSimpleSetOrListType(p)) {
			appendSimpleSetOrListValue(p);
		} else if (isSimpleMapType(p)) {
			appendSimpleMapValue(p);
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
			} else if (isSimpleContainerType(p)) {
				xmlParameter.append("<" + getXStreamContainerType(p.getType()) + ">");
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
				} else if (isSimpleContainerType(p)) {
					xmlParameter.append("</" + getXStreamContainerType(p.getType()) + ">");
				} else {
					xmlParameter.append("</" + p.getType() + ">");
				}
			}
			xmlParameters.add(xmlParameter.toString());
		} else {
			xmlParameter.append("</" + p.getName() + ">");
		}
	}
	
	// Currently all Array types are considered simple
	private boolean isSimpleArrayType(Parameter p) {
		return p.getType().endsWith("[]");
	}
	
	// Currently all Container types are considered simple
	private boolean isSimpleContainerType(Parameter p) {
		return (isSimpleSetOrListType(p) || isSimpleMapType(p));
	}
	
	// Currently all Set or List types are considered simple
	private boolean isSimpleSetOrListType(Parameter p) {
		return (p.getType().startsWith("java.util.ArrayList")
				|| p.getType().startsWith("java.util.LinkedList")
				|| p.getType().startsWith("java.util.HashSet"));
	}
	
	// Currently all Set or List types are considered simple
	private boolean isSimpleMapType(Parameter p) {
		return (p.getType().startsWith("java.util.HashMap"));
	}
	
	private String getSimpleArrayElementType(String arrayType) {
		return arrayType.replace("[]", "");
	}

	private String getXStreamArrayType(String arrayType) {
		return arrayType.replace("[]", "-array");
	}
	
	private String getSimpleSetOrListElementType(String setOrListType) {
		String regex = "<(.+)>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(setOrListType);
		String setOrListElementType = null;
		if (matcher.find()) {
			setOrListElementType = matcher.group(1).toLowerCase();
		} else {
			setOrListElementType = "object";
		}
		// System.out.println(setOrListType);
		return setOrListElementType;
	}
	
	private String[] getSimpleMapEntryType(String mapType) {
		String regex = "<(.+)\\s*,\\s*(.+)>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mapType);
		String[] mapEntryType = new String[2];
		if (matcher.find()) {
			mapEntryType[0] = matcher.group(1).toLowerCase();
			mapEntryType[1] = matcher.group(2).toLowerCase();
		} else {
			mapEntryType[0] = "object";
			mapEntryType[1] = "object";
		}
		// System.out.println(mapType);
		return mapEntryType;
	}
	
	private String getXStreamContainerType(String containerType) {
		if (containerType.startsWith("java.util.ArrayList")) {
			return "list";
		} else if (containerType.startsWith("java.util.HashMap")) {
			return "map";
		} else if (containerType.startsWith("java.util.LinkedList")) {
			return "linked-list";
		} else if (containerType.startsWith("java.util.HashSet")) {
			return "set";
		} else {
			return null;
		}
	}
	
	public String[] getContainerElements(String simpleContainerValue) {
		simpleContainerValue = simpleContainerValue.trim();
		if (simpleContainerValue.length() <= 2) { // [], {}, ()
			return new String[0];
		} else {
			String elements
				= simpleContainerValue.substring(1, simpleContainerValue.length() - 1).trim();
			if (elements.equals("")) { // [  ], {  }, (  )
				return new String[0];
			} else {
				return elements.split("\\s*[,:]\\s*");
			}
		}
	}
	
	private void appendSetOrListElements(String setOrListElementType, String[] elements) {
		for (String element : elements) {
			xmlParameter.append("<" + setOrListElementType + ">");
			xmlParameter.append(element);
			xmlParameter.append("</" + setOrListElementType + ">");
		}
	}
	
	private void appendMapElements(String[] mapEntryType, String[] elements) {
		for (int i = 0; i < elements.length; i += 2) {
			xmlParameter.append("<entry>");
			xmlParameter.append("<" + mapEntryType[0] + ">");
			xmlParameter.append(elements[i]);
			xmlParameter.append("</" + mapEntryType[0] + ">");
			xmlParameter.append("<" + mapEntryType[1] + ">");
			xmlParameter.append(elements[i+1]);
			xmlParameter.append("</" + mapEntryType[1] + ">");
			xmlParameter.append("</entry>");
		}
	}
	
	private void appendSimpleArrayValue(SimpleParameter p) {
		String elementType = getSimpleArrayElementType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		// System.out.println(Arrays.toString(elements));
		appendSetOrListElements(elementType, elements);
	}
	
	private void appendSimpleSetOrListValue(SimpleParameter p) {
		String elementType = getSimpleSetOrListElementType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		appendSetOrListElements(elementType, elements);
	}
	
	private void appendSimpleMapValue(SimpleParameter p) {
		String[] mapEntryType = getSimpleMapEntryType(p.getType());
		String[] elements = getContainerElements(values[next++]);
		appendMapElements(mapEntryType, elements);
	}

}
