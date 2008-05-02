package pairwisetesting.execution.testcasetemplateengine;

import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import pairwisetesting.execution.testcasetemplate.FirstCharRenderer;
import pairwisetesting.execution.testcasetemplate.TestCaseTemplateParameter;
import pairwisetesting.util.ClassUtil;


public class TestCaseTemplateEngine {

	private String testCaseTemplateParameterXmlData = "";
	private String pairwiseTestCasesXmlData = "";
	private String templateDir = "";
	private final String testCaseTemplateName = "PairwiseTest";
	
	public String generateTestNGTestCase() throws TestCaseTemplateEngineException {
		TestCaseTemplateParameter tp = null;
		try {
			tp = new TestCaseTemplateParameter(testCaseTemplateParameterXmlData);
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
			for (Map.Entry<String, String> entry : tp.getClassToMockInstanceNameEntrySet()) {
				t.setAttribute("classToMockNames", ClassUtil.getSimpleClassName(entry.getKey()));
				t.setAttribute("classToMockInstanceNames", entry.getValue());
				t.setAttribute("jmockInvokeSequences", tp.getJMockInvokeSequence(entry.getKey()));
			}
		}

		t.setAttribute("packageName", tp.getPackageName());
		t.setAttribute("classUnderTest", tp.getClassUnderTest());
		
		if (tp.hasConstructorArguments()) {
			t.setAttribute("constructorArgs", tp.getConstructorArguments());
		}
		
		t.setAttribute("methodUnderTest", tp.getMethodUnderTest());
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
		
		return t.toString();
	}

	public void setTestCaseTemplateParameterXmlData(String xmlData) {
		this.testCaseTemplateParameterXmlData = xmlData;
	}

	public void setPairwiseTestCasesXmlData(String xmlData) {
		this.pairwiseTestCasesXmlData = xmlData.replaceAll("\"", "'").replace("\n", "");
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

}
