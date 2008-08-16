package testingngservices.testcasetemplateengine;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import com.google.common.base.Preconditions;

import pairwisetesting.util.ClassUtil;
import testingngservices.testcasetemplate.TestCaseTemplateParameter;

/**
 * This class acts as the engine to generate test cases based on TestNG, JMock
 * JUnit and StringTemplate.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong>
 */
public class TestCaseTemplateEngine {

	private String testCaseTemplateParameterXmlData = "";
	private String pairwiseTestCasesXmlData = "";
	private String templateDir = "templates";
	private final String testCaseTemplateName = "PairwiseTest";

	/**
	 * Constructs a test case template engine.
	 */
	public TestCaseTemplateEngine() {
	}

	/**
	 * Generates and returns the TestNG test cases.
	 * 
	 * @return the generated TestNG test cases
	 * @throws TestCaseTemplateEngineException
	 *             if the engine has problems in generating test cases
	 */
	public String generateTestNGTestCase()
			throws TestCaseTemplateEngineException {
		TestCaseTemplateParameter tp = null;
		try {
			tp = TestCaseTemplateParameter
					.fromXML(testCaseTemplateParameterXmlData);
		} catch (Exception e) {
			throw new TestCaseTemplateEngineException(e);
		}

		StringTemplateGroup group = new StringTemplateGroup("mygroup",
				templateDir, DefaultTemplateLexer.class);

		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
		t.registerRenderer(String.class, new FirstCharRenderer());

		if (tp.hasImports()) {
			t.setAttribute("imports", tp.getImports());
		}

		if (tp.hasClassesToMock()) {
			t.setAttribute("needMock", true);
			for (Map.Entry<String, String> entry : tp
					.getClassToMockInstanceNameEntrySet()) {
				t.setAttribute("classToMockNames", ClassUtil
						.getSimpleClassName(entry.getKey()));
				t.setAttribute("classToMockInstanceNames", entry.getValue());
				t.setAttribute("jmockInvokeSequences", tp
						.getJMockInvocationSequence(entry.getKey()));
			}
		}

		t.setAttribute("packageName", tp.getPackageName());
		t.setAttribute("classUnderTest", tp.getClassUnderTest());

		if (tp.hasConstructorArguments()) {
			t.setAttribute("constructorArgs", tp.getConstructorArguments());
		}

		t.setAttribute("methodUnderTest", tp.getMethodUnderTest().getName());
		t.setAttribute("params", tp.getMethodParameters());
		t.setAttribute("isStaticMethod", tp.isStaticMethod());

		if (tp.isSingleton()) {
			t.setAttribute("isSingleton", tp.isSingleton());
			t.setAttribute("singletonMethod", tp.getSingletonMethod());
		}

		if (tp.hasCheckStateMethod()) {
			t.setAttribute("checkStateMethod", tp.getCheckStateMethod());
		}

		t.setAttribute("returnType", tp.getReturnType());

		if (tp.hasDelta()) {
			t.setAttribute("delta", tp.getDelta());
		}

		t.setAttribute("testCases", pairwiseTestCasesXmlData);
		t.setAttribute("methodUnderTestXmlData", tp.getMethodUnderTestXmlData());

		return t.toString();
	}

	/**
	 * Sets the XML data of the test case template parameter.
	 * 
	 * @param xmlData
	 *            the specified XML data of the test case template parameter
	 * @throws NullPointerException
	 *             if {@code xmlData} is null
	 */
	public void setTestCaseTemplateParameterXmlData(String xmlData) {
		Preconditions.checkNotNull(xmlData, 
				"XML data of the test case template parameter");
		this.testCaseTemplateParameterXmlData = xmlData;
	}

	/**
	 * Sets the XML data of Pairwise test cases.
	 * 
	 * @param xmlData
	 *            the specified XML data of Pairwise test cases
	 * @throws NullPointerException
	 *             if {@code xmlData} is null
	 */
	public void setPairwiseTestCasesXmlData(String xmlData) {
		Preconditions.checkNotNull(xmlData, "XML data of Pairwise test cases");
		this.pairwiseTestCasesXmlData = xmlData.replace("\"", "\\\"").replace(
				"\n", "");
	}

	/**
	 * Sets the StringTemplate's template dir.
	 * 
	 * @param templateDir
	 *            the specified template dir
	 * @throws NullPointerException
	 *             if {@code templateDir} is null
	 */
	public void setTemplateDir(String templateDir) {
		Preconditions.checkNotNull(templateDir, "template dir");
		this.templateDir = templateDir;
	}

}
