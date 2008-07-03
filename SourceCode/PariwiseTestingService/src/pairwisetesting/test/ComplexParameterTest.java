package pairwisetesting.test;

import junit.framework.TestCase;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.IMethodUnderTestXMLHelper;
import pairwisetesting.complex.IParameterVisitor;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.complex.parametervisitor.CountParameterVisitor;
import pairwisetesting.complex.parametervisitor.PrintParameterVisitor;

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
		assertEquals("student_id", p.getFullName());
		
		ComplexParameter tc = new ComplexParameter("Teacher", "teacher");
		Parameter tid = new SimpleParameter("String", "id");
		tc.add(tid);
		st1.add(tc);
		Parameter male = new SimpleParameter("boolean", "male");
		st1.add(male);
		assertEquals("Student", st1.getType());
		assertEquals("student", st1.getName());
		assertEquals(3, st1.getChildren().length);
		assertEquals(1, tc.getChildren().length);
		assertEquals("student_male", male.getFullName());
		assertEquals("student_teacher_id", tid.getFullName());
		
		CountParameterVisitor pv = new CountParameterVisitor();
		st1.accept(pv);
		assertEquals(5, pv.getNodeCount());
		assertEquals(3, pv.getLeafCount());
		
		IParameterVisitor ppv = new PrintParameterVisitor();
		st1.accept(ppv);
	}
	
	public void testMethodUnderTest() {
		SimpleParameter p1 = new SimpleParameter("int", "number");
		ComplexParameter p2 = new ComplexParameter("Student", "student");
		p2.add(new SimpleParameter("String", "id"));
		ComplexParameter p3 = new ComplexParameter("Teacher", "teacher");
		p3.add(new SimpleParameter("String", "id"));
		
		MethodUnderTest m = new MethodUnderTest("void", "foo");
		m.add(p1);
		m.add(p2);
		m.add(p3);
		
		assertEquals("void", m.getReturnType());
		assertEquals("foo", m.getName());
		assertEquals(3, m.getParameters().length);
	}
	
	public void testMethodUnderTestXMLHelper() {
		SimpleParameter p1 = new SimpleParameter("int", "number");
		ComplexParameter p2 = new ComplexParameter("Student", "student");
		SimpleParameter sid = new SimpleParameter("String", "id");
		p2.add(sid);
		ComplexParameter p3 = new ComplexParameter("Teacher", "teacher");
		SimpleParameter tid = new SimpleParameter("String", "id");
		p3.add(tid);
		p2.add(p3);
		
		MethodUnderTest m = new MethodUnderTest("void", "foo");
		m.add(p1);
		m.add(p2);
		IMethodUnderTestXMLHelper helper = new XStreamMethodUnderTestXMLHelper();
		String xml = helper.toXML(m);
		// System.out.println(xml);
		assertEquals(m, helper.fromXML(xml));
		
		helper.assign(m, new String[] {"100", "s001", "t001"});
	}
}
