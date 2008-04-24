package st;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	private static String testCaseFileName;
	private static String classUnderTestFileName;
	private static String packageName;

	public static void main(String[] args) throws Exception {
		
		//testStaticMethod();
		//testSingleton();
		//testInstanceMethodwithDefaultConstructor();
		//testInstanceMethodwithConstructorArgs();
		testInstanceMethodwithCheckStateMethod();
	
		compileRelatedClasses(testCaseFileName, classUnderTestFileName);
		Class<?> clazz = loadClass(packageName + "." + testCaseTemplateName);
		runWithTestNG(clazz);
	}
	
	private static void testSingleton() {
		System.out.println("testSingleton...");
		packageName = "math";
		testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		classUnderTestFileName = "src/math/Range.java";

		String[] imports = new String[] { "java.util.*", "java.io.*" };
		String classUnderTest = "Range";
		String methodUnderTest = "isBetween";
		String returnType = "boolean";
		Parameter[] params = new Parameter[] { new Parameter("n", "int"),
				new Parameter("lower", "int"), new Parameter("upper", "int") };
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
		t.registerRenderer(String.class, new FirstCharRenderer());
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("params", params);
		t.setAttribute("isSingleton", true);
		t.setAttribute("singletonMethod", "getInstance");
		t.setAttribute("testCases", testCases);
		writeTestCasetoFile(t.toString());
	}
	
	private static void testStaticMethod() {
		System.out.println("testStaticMethod...");
		packageName = "math";
		testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		classUnderTestFileName = "src/math/Range.java";

		String[] imports = new String[] { "java.util.*", "java.io.*" };
		String classUnderTest = "Range";
		String methodUnderTest = "isBetween";
		String returnType = "boolean";
		Parameter[] params = new Parameter[] { new Parameter("n", "int"),
				new Parameter("lower", "int"), new Parameter("upper", "int") };
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
		t.registerRenderer(String.class, new FirstCharRenderer());
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("params", params);
		t.setAttribute("isStaticMethod", true);
		t.setAttribute("testCases", testCases);
		writeTestCasetoFile(t.toString());
	}
	
	private static void testInstanceMethodwithDefaultConstructor() {
		System.out.println("testInstanceMethodwithDefaultConstructor...");
		packageName = "bookstore";
		testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		classUnderTestFileName = "src/bookStore/BookStore.java";
		
		String[] imports = new String[] { };
		String classUnderTest = "BookStore";
		String methodUnderTest = "computeDiscountedPrice";
		Parameter[] params = new Parameter[] { new Parameter("level", "int"),
				new Parameter("accountType", "AccountType"), new Parameter("coupon", "String") };
		String returnType = "double";
		double delta = 0.001;
		
		String testCases = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		testCases = testCases.replaceAll("\"", "'");
		
		StringTemplateGroup group = new StringTemplateGroup("mygroup",
				"templates", DefaultTemplateLexer.class);
		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
		t.registerRenderer(String.class, new FirstCharRenderer());
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("params", params);
		t.setAttribute("delta", delta);
		t.setAttribute("testCases", testCases);
		writeTestCasetoFile(t.toString());
	}
	
	private static void testInstanceMethodwithConstructorArgs() {
		System.out.println("testInstanceMethodwithConstructorArgs...");
		packageName = "bookstore";
		testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		classUnderTestFileName = "src/bookStore/BookStore.java";
		
		String[] imports = new String[] { };
		String classUnderTest = "BookStore";
		String methodUnderTest = "computeDiscountedPrice";
		Argument[] constructorArgs = new Argument[] { new Argument("DangDang", "String"), new Argument("100", "int")};
		Parameter[] params = new Parameter[] { new Parameter("level", "int"),
				new Parameter("accountType", "AccountType"), new Parameter("coupon", "String") };
		String returnType = "double";
		double delta = 0.001;
		
		String testCases = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		testCases = testCases.replaceAll("\"", "'");
		
		StringTemplateGroup group = new StringTemplateGroup("mygroup",
				"templates", DefaultTemplateLexer.class);
		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
		t.registerRenderer(String.class, new FirstCharRenderer());
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("constructorArgs", constructorArgs);
		t.setAttribute("params", params);
		t.setAttribute("delta", delta);
		t.setAttribute("testCases", testCases);
		writeTestCasetoFile(t.toString());
	}
	
	private static void testInstanceMethodwithCheckStateMethod() {
		System.out.println("testInstanceMethodwithCheckStateMethod...");
		packageName = "bookstore";
		testCaseFileName = "src" + "/" + packageName + "/"
				+ testCaseTemplateName + ".java";
		classUnderTestFileName = "src/bookStore/BookStore.java";
		
		String[] imports = new String[] { };
		String classUnderTest = "BookStore";
		String methodUnderTest = "computeDiscountedPrice";
		Argument[] constructorArgs = new Argument[] { new Argument("DangDang", "String"), new Argument("100", "int")};
		Parameter[] params = new Parameter[] { new Parameter("level", "int"),
				new Parameter("accountType", "AccountType"), new Parameter("coupon", "String") };
		String returnType = "double";
		double delta = 0.001;
		
		String testCases = "<?xml version=\"1.0\"?>"
            + "<testcases>"
            + "<factor>Level</factor>"
            + "<factor>AccountType</factor><"
            + "factor>Coupon</factor>"
            + "<run><level>1</level><level>NORMAL</level><level>C001</level></run>"
            + "<run><level>1</level><level>STUDENT</level><level>C002</level></run>"
            + "<run><level>1</level><level>INTERNAL</level><level>C003</level></run>"
            + "<run><level>2</level><level>NORMAL</level><level>C002</level></run>"
            + "<run><level>2</level><level>STUDENT</level><level>C003</level></run>"
            + "<run><level>2</level><level>INTERNAL</level><level>C001</level></run>"
            + "<run><level>3</level><level>NORMAL</level><level>C003</level></run>"
            + "<run><level>3</level><level>STUDENT</level><level>C001</level></run>"
            + "<run><level>3</level><level>INTERNAL</level><level>C002</level></run>"
            + "</testcases>";
		testCases = testCases.replaceAll("\"", "'");
		
		StringTemplateGroup group = new StringTemplateGroup("mygroup",
				"templates", DefaultTemplateLexer.class);
		StringTemplate t = group.getInstanceOf(testCaseTemplateName);
		t.registerRenderer(String.class, new FirstCharRenderer());
		t.setAttribute("packageName", packageName);
		t.setAttribute("imports", imports);
		t.setAttribute("classUnderTest", classUnderTest);
		t.setAttribute("methodUnderTest", methodUnderTest);
		t.setAttribute("returnType", returnType);
		t.setAttribute("constructorArgs", constructorArgs);
		t.setAttribute("params", params);
		t.setAttribute("delta", delta);
		t.setAttribute("checkStateMethod", "getDiscountedPrice");
		t.setAttribute("testCases", testCases);
		writeTestCasetoFile(t.toString());
	}
	
	private static void writeTestCasetoFile(String testCase) {
		TextFile.write(testCaseFileName, testCase);
		System.out.println("Test case was generated.");
	}

	private static void compileRelatedClasses(String...classFiles) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager
				.getJavaFileObjects(classFiles);
		String[] options = new String[] { "-d", "bin" };
		compiler.getTask(null, fileManager, null, Arrays.asList(options), null,
				compilationUnits1).call();
		fileManager.close();
		System.out.println("Test case was compiled.");
	}
	
	private static Class<?> loadClass(String className)	throws MalformedURLException, ClassNotFoundException {
		File outputDir = new File("bin");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL[] urls = new URL[] { outputDir.toURI().toURL() };
		URLClassLoader ucl = new URLClassLoader(urls, cl);
		Class<?> clazz = ucl.loadClass(className);
		System.out.println("Test case class was loaded.");
		return clazz;
	}
	
	private static void runWithTestNG(Class<?>... classes) {
		System.out.println("Run Test case with TestNG...");
		TestNG tng = new TestNG();
		tng.setTestClasses(classes);
		TestListenerAdapter listener = new TestListenerAdapter();
		tng.addListener(listener);
		tng.run();
		System.out.println("PASSED: " + listener.getPassedTests().size());
	}
}