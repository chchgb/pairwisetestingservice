package pairwisetesting.plug.util;

import java.util.HashSet;
import java.util.Set;

public class ParamerterGenerator {
	public static String intRandomGenerator(int num,int min,int max){
		
		Set<String> tempSet = new HashSet<String>();
		while(tempSet.size() <= num){
			int temp = (int)(Math.random()*(max-min))+min;
			tempSet.add(String.valueOf(temp));
		}
		String result=tempSet.toString();
		result = result.substring(1,result.length()-1).replace(", ", ",");
		
		return result;
	}

}
