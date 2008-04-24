package st;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import util.TextFile;

public class TestCaseTemplate {
	
	private static String testCaseTemplateName = "PairwiseTest";

	public static void main(String[] args) throws Exception {

		String packageName = "math";
		String testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		String classUnderTestFileName = "src/math/Range.java";

		String[] imports = new String[] { "java.util.*", "java.io.*" };
		String classUnderTest = "Range";
		String methodUnderTest = "isBetween";
		String returnType = "boolean";
		boolean isStaticMethod = true;
		Argument[] arguments = new Argument[] { new Argument("n", "int"),
				new Argument("lower", "int"), new Argument("upper", "int") };
		String testCases = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>n</factor>"
            + "<factor>lower</factor><"
            + "factor>upper</factor>"
            + "<run><level>3</level><level>1</level><level>4</level></run>"
            + "<run><level>3</level><level>3</level><level>4</level></run>"
            + "<run><level>4</level><level>3</level><level>4</level></run>"
            + "</testcases>";
		testCases = testCases.replaceAll("\"", "'");
		
		StringTemplateGroup group = new StringTemplateGroup("mygroup",
				"templates", DefaultTemplateLexer.class);
		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("args", arguments);
		t.setAttribute("isStaticMethod", isStaticMethod);
		t.setAttribute("testCases", testCases);
		
//		String packageName = "bookstore";
//		String testCaseFileName = "src" + "/" + packageName + "/"
//				+ testCaseTemplateName + ".java";
//		
//		String[] imports = new String[] { };
//		String classUnderTestFileName = "src/bookStore/BookStore.java";
//		String classUnderTest = "BookStore";
//		String methodUnderTest = "computeDiscountedPrice";
//		Argument[] arguments = new Argument[] { new Argument("level", "int"),
//				new Argument("accountType", "AccountType"), new Argument("coupon", "String") };
//		String returnType = "double";
//		boolean isStaticMethod = false;
//		double delta = 0.001;
//		
//		String testCases = "<?xml version=\"1.0\"?>"
//            + "<testcases>"
//            + "<factor>Level</factor>"
//            + "<factor>AccountType</factor><"
//            + "factor>Coupon</factor>"
//            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
//            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
//            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
//            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
//            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
//            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
//            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
//            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
//            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
//            + "</testcases>";
//		testCases = testCases.replaceAll("\"", "'");
//		
//		StringTemplateGroup group = new StringTemplateGroup("mygroup",
//				"templates", DefaultTemplateLexer.class);
//		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
//		t.setAttribute("packageName", packageName);
//		t.setAttribute("imports", imports);
//		t.setAttribute("classUnderTest", classUnderTest);
//		t.setAttribute("methodUnderTest", methodUnderTest);
//		t.setAttribute("returnType", returnType);
//		t.setAttribute("args", arguments);
//		t.setAttribute("isStaticMethod", isStaticMethod);
//		t.setAttribute("delta", delta);
//		t.setAttribute("testCases", testCases);
		
		// System.out.println(t);
		TextFile.write(testCaseFileName, t.toString());
		System.out.println("Test case was generated.");

		// Compile the classes related with test
		String[] files = new String[] { testCaseFileName,
				classUnderTestFileName };
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
				.getJavaFileObjects(files);
		String[] options = new String[] { "-d", "bin" };
		compiler.getTask(null, fileManager, null, Arrays.asList(options), null,
				compilationUnits1).call();
		fileManager.close();
		System.out.println("Test case was compiled.");

		// Load the classes related with test
		File outputDir = new File("bin");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL[] urls = new URL[] { outputDir.toURI().toURL() };
		URLClassLoader ucl = new URLClassLoader(urls, cl);
		Class<?> clazz = ucl.loadClass(packageName + "." + testCaseTemplateName);
		System.out.println("Test case class was loaded.");

		// Run Test with TestNG
		System.out.println("Run Test case with TestNG...");
		TestNG tng = new TestNG();
		tng.setTestClasses(new Class[] { clazz });
		TestListenerAdapter listener = new TestListenerAdapter();
		tng.addListener(listener);
		tng.run();
		System.out.println("PASSED: " + listener.getPassedTests().size());
	}
}