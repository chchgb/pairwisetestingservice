package testingngservices.test;

import org.junit.Test;

import pairwisetesting.util.LibDependence;
import testingngservices.TestNGServiceImpl;


public class UtilTest {
	@Test
	public void LibDependence(){
		LibDependence temp = new LibDependence();
		
		temp.addJavaLib("D:/MyShare/Tomcat/lib/testng-5.8-jdk15.jar");
		temp.writeLib("testng-5.8-jdk15.jar", "D:/");
		
		
	}
	
	@Test
	public void LibList(){
		TestNGServiceImpl testNG = new TestNGServiceImpl();
		String result = testNG.getLibList();
		System.out.println(result);
	}

}
