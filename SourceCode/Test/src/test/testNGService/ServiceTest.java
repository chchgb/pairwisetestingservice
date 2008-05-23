package test.testNGService;

import org.junit.Test;

import testingngservices.ITestNGService;
import testingngservices.TestNGServiceImpl;


public class ServiceTest {
	@Test
	public void TestNGServiceImpl(){
		ITestNGService service = new TestNGServiceImpl();
		
		String libList = service.getLibList();
		
		System.out.println("LibList" + libList);
		
		
	}

}
