package pairwisetesting.complex;


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

	public void assign(MethodUnderTest m, String[] values) {
		m.accept(new ParameterAssignmentVisitor(values));
	}
}

class ParameterAssignmentVisitor implements IParameterVisitor {
	
	private String[] values;
	private int next;
	
	ParameterAssignmentVisitor(String[] values) {
		this.values = values;
	}
	
	public void visit(SimpleParameter p) {
		beginTag(p);
		System.out.print(values[next++]);
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
	
	private void beginTag(SimpleParameter p) {
		if (p.getDepth() == 0) {
			System.out.print("<" + p.getType() + ">");
		} else {
			System.out.print("<" + p.getName() + ">");
		}
	}
	
	private void beginTag(ComplexParameter p) {
		if (p.getDepth() == 0) {
			System.out.println("<" + p.getType() + ">");
		} else {
			System.out.println("<" + p.getName() + ">");
		}
		
	}
	
	private void endTag(Parameter p) {
		if (p.getDepth() == 0) {
			System.out.println("</" + p.getType() + ">");
		} else {
			System.out.println("</" + p.getName() + ">");
		}
	}
	
}
