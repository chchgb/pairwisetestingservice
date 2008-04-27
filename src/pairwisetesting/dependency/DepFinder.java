package pairwisetesting.dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DepFinder {
	/**
	 * Constructor
	 *
	 */
	public DepFinder(){
		
	}
	
	public static void main(String args[]){
		DepFinder df = new DepFinder();
		
		String path = "bin\\pairwisetesting\\test";
		String name = "TestPairwiseTesting";
		String func = "test";
		
		df.findMethodDependency(path, name, func);
	}
	
	/**
	 *  Find dependencies of a specific class
	 */
	public String findDependency(String classPath, String className){
		StringBuilder outputFileName = new StringBuilder();
		outputFileName.append(classPath).append("\\");
		outputFileName.append(className).append(".xml");
		
		c2c(className, outputFileName.toString());
		c2p(outputFileName.toString());
		p2p(outputFileName.toString());
		
		return null;
	}
	
	public String findMethodDependency(String classPath, String className, String methodName){
		StringBuilder classFileName = new StringBuilder();
		classFileName.append(classPath).append("\\").append(className).append(".class");
		
		StringBuilder xmlFileName = new StringBuilder();
		xmlFileName.append(classPath).append("\\").append(className).append(".xml");
		
		extract(classFileName.toString(), xmlFileName.toString());
		f2f(xmlFileName.toString(), methodName);
		return null;
	}
	
	/**
	 *  Call DependencyExtractor.bat
	 *  to generate a XML file
	 */
	private void extract(String classFileName, String xmlFileName){
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
	private void c2c(String className, String xmlFileName){
		// Command line for c2c
		StringBuilder c2cCommand = new StringBuilder();
		c2cCommand.append("c2c.bat -scope-includes ");
		c2cCommand.append("\\").append(className).append("\\ ");
		c2cCommand.append(xmlFileName);
		
		try {
			String command = c2cCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String str = null;
			while(null != (str = in.readLine())){
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Call c2p.bat to handle XML file
	 * Get Class-to-Package dependencies
	 */
	private void c2p(String xmlFileName){
		// Command line for c2p
		StringBuilder c2pCommand = new StringBuilder();
		c2pCommand.append("c2p.bat -show-outbounds ").append(xmlFileName);
		
		try {
			String command = c2pCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String str = null;
			while(null != (str = in.readLine())){
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Call p2p.bat to handle XML file
	 * Get Package-to-Package dependencies
	 */
	private void p2p(String xmlFileName){
		// Command line for p2p
		StringBuilder p2pCommand = new StringBuilder();
		p2pCommand.append("p2p.bat -show-outbounds ").append(xmlFileName);
		
		try {
			String command = p2pCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String str = null;
			while(null != (str = in.readLine())){
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void f2f(String xmlFileName, String methodName){
		// Command for f2f
		StringBuilder f2fCommand = new StringBuilder();
		f2fCommand.append("f2f.bat -show-outbounds ");
		
		//f2fCommand.append("-scope-includes ");
		//f2fCommand.append("/").append(className).append("/ ");
		
		f2fCommand.append("-feature-scope-includes ");
		f2fCommand.append("/").append(methodName).append("/ ");
		f2fCommand.append(xmlFileName);
		
		try {
			String command = f2fCommand.toString();
			System.out.println(command);
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			
			String str = null;
			while(null != (str = in.readLine())){
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
