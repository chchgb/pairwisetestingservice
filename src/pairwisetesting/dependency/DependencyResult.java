package pairwisetesting.dependency;

import java.util.ArrayList;

public class DependencyResult {

	public ArrayList<String> srcList;
	public ArrayList<String> libList;
	public ArrayList<String> mockList;

	public static ArrayList<String> transferPath(String endPath,
			ArrayList<String> transfer) {
		ArrayList<String> result = new ArrayList<String>();
		for (String source : transfer) {
			String temp = source.substring(endPath.length(), source.length());
			result.add(temp);
		}
		return result;
	}

}