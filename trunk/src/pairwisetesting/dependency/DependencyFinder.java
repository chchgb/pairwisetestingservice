package pairwisetesting.dependency;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;

import pairwisetesting.util.Directory;

public class DependencyFinder {

	private String srcPath;
	private String binPath;
	private String className;
	private String classPath;

	private HashSet<String> packagePrefix;
	private HashSet<String> directory;
	
	private ArrayList<Class<?>> interfaceList = new ArrayList<Class<?>>();
	private ArrayList<Class<?>> abstractClassList = new ArrayList<Class<?>>();
	private ArrayList<Class<?>> concreteClassList = new ArrayList<Class<?>>();
	
	private HashSet<String> srcFullClassNameSet = new HashSet<String>();

	public DependencyFinder(String fullClassName) {
		className = fullClassName;
		classPath = className.replace('.', '/');
		srcPath = "src";
		binPath = "bin";

		directory = walkDirectory();
		packagePrefix = enumPrefix();
	}

	public DependencyFinder(String fullClassName, String sourcePath,
			String binaryPath) {
		className = fullClassName;
		classPath = className.replace('.', '/');
		srcPath = sourcePath;
		binPath = binaryPath;

		directory = walkDirectory();
		packagePrefix = enumPrefix();
	}

	public DependencyResult findDependency() {
		extract();
		ArrayList<String> output = c2c();
		ArrayList<String> list = formatC2COutput(output);
		DependencyResult result = generateResult(list);
		feedMockList(result);
		return result;
	}

	private String buildXMLPath() {
		StringBuilder builder = new StringBuilder();
		builder.append(binPath).append("\\");
		builder.append(classPath).append(".xml");

		return builder.toString();
	}

	private String buildClassPath() {
		StringBuilder builder = new StringBuilder();
		builder.append(binPath).append("\\");
		builder.append(classPath).append(".class");

		return builder.toString();
	}

	private String narrowClassName() {
		String[] array = className.split("\\.");

		// The last element in array is the narrowed name
		return array[array.length - 1];
	}

	/**
	 * Call DependencyExtractor.bat to generate xml file
	 */
	private void extract() {
		String classPath = buildClassPath();
		String xmlPath = buildXMLPath();

		StringBuilder extCommand = new StringBuilder();
		extCommand.append("DependencyExtractor.bat -xml -out ");
		extCommand.append(xmlPath).append(" ");
		extCommand.append(classPath);

		try {
			String command = extCommand.toString();
			//System.out.println(command);

			Process p = Runtime.getRuntime().exec(command);

			// Wait for the process p to end
			// Ensure the XML file is generated
			// Or, we can't call c2c, c2p, ...
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> c2c() {
		String xmlPath = buildXMLPath();
		String name = narrowClassName();

		// Command line for c2c
		StringBuilder c2cCommand = new StringBuilder();
		c2cCommand.append("c2c.bat ").append("-scope-includes ");
		c2cCommand.append("\\").append(name).append("\\ ");
		c2cCommand.append(xmlPath);

		ArrayList<String> resultList = new ArrayList<String>();

		try {
			String command = c2cCommand.toString();
			//System.out.println(command);

			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p
					.getInputStream()));

			String str = null;
			while (null != (str = in.readLine())) {
				resultList.add(str);
				//System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	private ArrayList<String> formatC2COutput(ArrayList<String> c2cOutput) {
		HashSet<String> results = new HashSet<String>();
		Iterator<String> iter = c2cOutput.iterator();

		while (iter.hasNext()) {
			String line = iter.next();
			String str = line.trim();

			if (!str.startsWith("--> "))
				continue;

			String dep = str.substring(4);
			String string = dep.endsWith(" *") ? dep.substring(0,
					dep.length() - 2) : dep;

			String[] name = string.split("[$]");
			//System.out.println(name[0]);
			results.add(name[0]);
		}
		return new ArrayList<String>(results);
	}

	private DependencyResult generateResult(ArrayList<String> list) {
		DependencyResult result = new DependencyResult();
		Iterator<String> iter = list.iterator();

		while (iter.hasNext()) {
			String path = iter.next();
			feedResultList(result, path);
		}
		return result;
	}

	private void feedResultList(DependencyResult result, String fullClassName) {
		String file = buildSourcePath(fullClassName);
		
		if (directory.contains(file)) {
			//System.out.println(file);
			result.srcList.add(file);
			srcFullClassNameSet.add(fullClassName);
		} else {
			String lib = resolveLibraryName(fullClassName);
			//if (lib != null)
				//System.out.println(lib);
			if (lib != null)
				result.libList.add(lib);
		}
	}

	private void feedMockList(DependencyResult result) {
		for (String fullClassName : srcFullClassNameSet) {
			
			// Collect interfaces & abstract & concrete classes
			try {
				Class<?> clazz = Class.forName(fullClassName);
				if (clazz.isInterface()) {
					interfaceList.add(clazz);
				} else if (Modifier.isAbstract(clazz.getModifiers())) {
					abstractClassList.add(clazz);
				} else {
					concreteClassList.add(clazz);
				}				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Remove implemented interfaces & extended abstract classes
		for (Class<?> c : concreteClassList) {
			Iterator<Class<?>> iter = interfaceList.iterator();
			while (iter.hasNext()) {
				if (Arrays.asList(c.getInterfaces()).contains(iter.next())) {
					iter.remove();
				}
			}
			iter = abstractClassList.iterator();
			while (iter.hasNext()) {
				if (c.getSuperclass() == iter.next()) {
					iter.remove();
				}
			}
		}
		
		// Left interfaces & abstract classes need mock
		for (Class<?> c : interfaceList) {
			result.mockList.add(c.getName());
		}
		for (Class<?> c : abstractClassList) {
			result.mockList.add(c.getName());
		}
	}

	private String buildSourcePath(String path) {
		String filePath = path.replace('.', '/');

		StringBuilder builder = new StringBuilder();
		builder.append(srcPath).append("/");
		builder.append(filePath).append(".java");

		return builder.toString();
	}

	private String resolveLibraryName(String path) {
		String[] str = path.split("\\.");
		if (packagePrefix.contains(str[0])) {
			if (str[0].equals("java"))
				return null;
			else
				return str[1];
		} else
			return str[0];

	}

	private HashSet<String> walkDirectory() {
		HashSet<String> dir = new HashSet<String>();

		Directory.TreeInfo treeInfo = Directory.walk(srcPath, ".*[.]java$");
		for (File javaFile : treeInfo) {
			String str = javaFile.getPath();
			String path = str.replace('\\', '/');
			dir.add(path);
		}
		return dir;
	}

	private HashSet<String> enumPrefix() {
		HashSet<String> prefix = new HashSet<String>();
		prefix.add("org");
		prefix.add("java");
		prefix.add("com");
		prefix.add("nu");
		return prefix;
	}
}
