package pairwisetesting.dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * Class Name:	DepFinder
 * 
 * Author:		Alex Wang
 * 
 * Email:		A0717220@pub.ss.pku.edu.cn
 * 
 * Date:		2008 Apr 27
 * 
 * Description:	Wrapper of DependencyFinder tools
 * 
 * Prerequist:	1. Download DependencyFinder-1.2.0.bin.zip
 * 				2. Unzip & setup environment referring to DependencyFinder\docs\Manual.html
 * 				3. Make sure DependencyFinder\bin is in your PATH, and JAVA_HOME exists
 * 
 * Warning:		This class may not be that flexible since it was tailor made for Group IV.
 * 				Feel free to modify to meet your needs.
 * 
 * Usage:		ArrayList<String> list = DepFinder.findDependentClasses(pathName, packName, className);
 * 
 * Parameters:	pathName:	Where to find the .class file
 * 				packName:	Top level package name of the project to test
 * 				className:	Class name of the method to test
 */
public class DepFinder {
	/**
	 * Default constructor is private
	 * Don't instantiate it.
	 */
	private DepFinder(){
		
	}
	
	/**
	 * 
	 * @param pathName	Path of the .class file
	 * @param packName	Top level package name of the project
	 * @param className	Name of the class to test
	 * @return Array of paths of the source file to send to server
	 * 
	 */
	public static ArrayList<String> findDependentClasses(String pathName, String packName, String className) {
		String xmlFileName = buildFileName(pathName, className, ".xml");
		String classFileName = buildFileName(pathName, className, ".class");
		
		extract(classFileName, xmlFileName);
		ArrayList<String> rawOutput = c2c(className, xmlFileName);
		
		return rearrangeResults(packName, rawOutput);
	}
	
	private static String buildFileName(String path, String className, String suffix) {
		StringBuilder fileName = new StringBuilder();
		fileName.append(path);
		fileName.append("\\");
		fileName.append(className);
		fileName.append(suffix);
		return fileName.toString();
	}
	
	/**
	 *  Call DependencyExtractor.bat
	 *  to generate XML file
	 */
	private static void extract(String classFileName, String xmlFileName) {
		StringBuilder extCommand = new StringBuilder();
		extCommand.append("DependencyExtractor.bat -xml -out ");
		extCommand.append(xmlFileName).append(" ");
		extCommand.append(classFileName);
		
		try {
			String command = extCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			
			// Wait for p to end
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Call c2c.bat to handle XML file
	 * Get Class-to-Class dependencies
	 */
	private static ArrayList<String> c2c(String className, String xmlFileName){
		// Command line for c2c
		StringBuilder c2cCommand = new StringBuilder();
		c2cCommand.append("c2c.bat ").append("-scope-includes ");
		c2cCommand.append("\\").append(className).append("\\ ");
		c2cCommand.append(xmlFileName);
		
		ArrayList<String> resultList = new ArrayList<String>();
		
		try {
			String command = c2cCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String str = null;
			while(null != (str = in.readLine())){
				resultList.add(str);
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	private static ArrayList<String> rearrangeResults(String projName, ArrayList<String> list) {
		ArrayList<String> results = new ArrayList<String>();
		Iterator<String> iter = list.iterator();
		
		while (iter.hasNext()) {
			String line = iter.next();
			String str = line.trim();
			
			if (!str.startsWith("--> ")) continue;
			
			String dep = str.substring(4);
			String classPath = dep.endsWith(" *") ? dep.substring(0, dep.length() - 2) : dep;
			String[] paths = classPath.split("\\.");

			if (!projName.equals(paths[0])) continue;

			String path = classPath.replace('.', '\\');
			String fileName = buildFileName("src", path, ".java");
			results.add(fileName);
		}
		return results;
	}
}
