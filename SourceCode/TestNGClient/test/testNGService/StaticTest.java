package test.testNGService;

import org.junit.Test;

import pairwisetesting.plug.util.ParamerterGenerator;


public class StaticTest {
	@Test
	public void random(){
		String result = ParamerterGenerator.intRandomGenerator(10,1,10);
		System.out.println("result :" + result);
		
	}

}
