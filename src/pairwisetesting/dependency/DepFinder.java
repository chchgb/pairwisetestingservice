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
		
		df.findDependency(path, name);
	}
	
	/**
	 *  Find dependencies of a specific class
	 */
	public String findDependency(String classPath, String className){
		StringBuilder outputFileName = new StringBuilder();
		outputFileName.append(classPath).append("\\");
		outputFileName.append(className).append(".xml");
		
		StringBuilder extCommand = new StringBuilder();
		extCommand.append("DependencyExtractor.bat -xml -out ");
		extCommand.append(outputFileName).append(" ");
		extCommand.append(classPath).append("\\");
		extCommand.append(className).append(".class");
		
		extract(extCommand.toString());
		c2c(className, outputFileName.toString());
		c2p(outputFileName.toString());
		p2p(outputFileName.toString());
		
		return null;
	}
	
	/**
	 *  Call DependencyExtractor.bat
	 *  to generate a XML file
	 */
	private void extract(String command){
		System.out.println(command);
		try {
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
	private void c2c(String className, String fileName){
		// Command line for c2c
		StringBuilder c2cCommand = new StringBuilder();
		c2cCommand.append("c2c.bat -scope-includes ");
		c2cCommand.append("\\").append(className).append("\\ ");
		c2cCommand.append(fileName);
		
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
	private void c2p(String fileName){
		// Command line for c2p
		StringBuilder c2pCommand = new StringBuilder();
		c2pCommand.append("c2p.bat -show-outbounds ").append(fileName);
		
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
	private void p2p(String fileName){
		// Command line for p2p
		StringBuilder p2pCommand = new StringBuilder();
		p2pCommand.append("p2p.bat -show-outbounds ").append(fileName);
		
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
}
