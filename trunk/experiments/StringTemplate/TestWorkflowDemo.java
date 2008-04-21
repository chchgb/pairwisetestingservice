import java.util.*;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.net.*;
import java.io.*;

import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.*;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class TestWorkflowDemo {

	public static void main(String[] args) throws Exception {

		// Another way to compile Java source codes
//		String[] options = new String[]{"-d", "bin", "src/demo/Test2.java", "src/demo/Util.java"};
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//		int compilationResult =	compiler.run(null, null, null, options);
//		if (compilationResult == 0){
//			System.out.println("Compilation is successful");
//		} else {
//			System.out.println("Compilation Failed");
//		}
		
		// Generate the source code of test case
		StringTemplate query = new StringTemplate(TextFile.read("UnitTest.st"));
		query.setAttribute("class", "Util");
		query.setAttribute("methodName", "isBetween");
		query.setAttribute("args", new Arg("n", "int"));
		query.setAttribute("args", new Arg("lower", "int"));
		query.setAttribute("args", new Arg("upper", "int"));
		query.setAttribute("returnType", "boolean");
		TextFile.write("src/demo/UnitTest.java", query.toString());
		System.out.println("Test case was generated.");
		
		// Compile the classes related with test
		String[] files = new String[]{"src/demo/UnitTest.java", "src/demo/Util.java"};
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjects(files);
		String[] options = new String[] {"-d", "bin"};
		compiler.getTask(null, fileManager, null, Arrays.asList(options), null, compilationUnits1).call();
		fileManager.close();
		System.out.println("Test case was compiled.");
		
		// Load the classes related with test
		File outputDir = new File("bin");  
		ClassLoader cl = Thread.currentThread().getContextClassLoader();  
		URL[] urls = new URL[]{outputDir.toURI().toURL()};  
		URLClassLoader ucl = new URLClassLoader(urls, cl);  
		Class clazz = ucl.loadClass("demo.UnitTest"); 
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
