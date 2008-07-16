package pairwisetesting.test;

import java.util.Arrays;

import junit.framework.TestCase;
import pairwisetesting.complex.ChildParametersExtractor;
import pairwisetesting.complex.ComplexParameter;
import pairwisetesting.complex.IMethodUnderTestXMLHelper;
import pairwisetesting.complex.IParameterVisitor;
import pairwisetesting.complex.MethodUnderTest;
import pairwisetesting.complex.Parameter;
import pairwisetesting.complex.SimpleParameter;
import pairwisetesting.complex.XStreamMethodUnderTestXMLHelper;
import pairwisetesting.complex.parametervisitor.CountParameterVisitor;
import pairwisetesting.complex.parametervisitor.PrintParameterVisitor;
import pairwisetesting.test.edu.Student;

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
		assertEquals("student.id", p.getFullName());

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
		assertEquals("student.male", male.getFullName());
		assertEquals("student.teacher.id", tid.getFullName());

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
		ComplexParameter p2 = new ComplexParameter(
				"pairwisetesting.test.edu.Student", "student");
		SimpleParameter sid = new SimpleParameter("String", "id");
		p2.add(sid);
		ComplexParameter p3 = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
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

		Object[] objects = helper.assign(xml, new String[] { "100", "s001", "t001" });
		assertEquals(100, Integer.parseInt(objects[0].toString()));
		assertEquals("s001", ((Student)objects[1]).getId());
		assertEquals("t001", ((Student)objects[1]).getTeacher().getId());
	}
	
	public void testChildParametersExtractor(){
		String className = "pairwisetesting.test.edu.Student";
		ChildParametersExtractor e = new ChildParametersExtractor();
		Parameter[] ps = e.getParameters(className);
		
		Parameter[] expected = new Parameter[2];
		SimpleParameter sid = new SimpleParameter("java.lang.String", "id");
		expected[0] = sid;
		ComplexParameter teacher = new ComplexParameter(
				"pairwisetesting.test.edu.Teacher", "teacher");
		SimpleParameter tid = new SimpleParameter("java.lang.String", "id");
		teacher.add(tid);
		expected[1] = teacher;

		assertTrue("It should be equal", Arrays.equals(expected, ps));
	}
}