package pairwisetesting.test;

import junit.framework.TestCase;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.CountParameterVisitor;
import pairwisetesting.complex.IParameterVisitor;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.PrintParameterVisitor;
import pairwisetesting.complex.SimpleParameter;

public class ComplexParameterTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParameter() {
		Parameter p = new SimpleParameter("String", "id");
		
		assertEquals("String", p.getType());
		assertEquals("id", p.getName());
		
		ComplexParameter st1 = new ComplexParameter("Student", "student");
		st1.add(p);
		ComplexParameter tc = new ComplexParameter("Teacher", "Yu Lian");
		tc.add(new SimpleParameter("String", "id"));
		st1.add(tc);
		p = new SimpleParameter("boolean", "male");
		st1.add(p);
		assertEquals("Student", st1.getType());
		assertEquals("student", st1.getName());
		
		CountParameterVisitor pv = new CountParameterVisitor();
		st1.accept(pv);
		assertEquals(5, pv.getNodeCount());
		assertEquals(3, pv.getLeafCount());
		
		IParameterVisitor ppv = new PrintParameterVisitor();
		st1.accept(ppv);
	}
	
	public void testMethodUnderTest() {
		SimpleParameter p1 = new SimpleParameter("", "");
		ComplexParameter p2 = new ComplexParameter("", "");
		ComplexParameter p3 = new ComplexParameter("", "");
		
		MethodUnderTest m = new MethodUnderTest("void", "foo");
		m.add(p1);
		m.add(p2);
		m.add(p3);
		
		assertEquals("void", m.getReturnType());
		assertEquals("foo", m.getName());
		assertEquals(3, m.getParameters().length);
		
		String expectedXMLOutput = "";
		System.out.println(m.toXML());
		assertEquals(expectedXMLOutput, m.toXML());
	}
}
