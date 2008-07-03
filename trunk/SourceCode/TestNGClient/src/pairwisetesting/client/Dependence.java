package pairwisetesting.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dependence {

	
	public static ArrayList<String> getAllLibList(String classpathFileName){
		ArrayList<String> libList = new ArrayList<String>();
		File classpath = null;
		if(classpathFileName.equals("")){
			classpath = new File(".classpath");
		}else{
			classpath = new File(classpathFileName);
		}
		 
		try {
			BufferedReader in = new BufferedReader(new FileReader(classpath));
			try {
				String libFullPath = null;
				while ((libFullPath = in.readLine()) != null) {
					//System.out.println("libFullPath : "+ libFullPath);
					if(libFullPath.contains(".jar")){
						String path = libFullPath.substring(libFullPath.indexOf("path=\"")+6, libFullPath.indexOf(".jar")+4);
						//System.out.println("libFullPath : "+path);
						libList.add(path);
					}
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return libList;
	}
	


}
