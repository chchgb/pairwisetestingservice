package pairwisetesting.dependency;

import java.util.ArrayList;

public class DependencyResult {

	public DependencyResult() {
		srcList = new ArrayList<String>();
		libList = new ArrayList<String>();
		mockList = new ArrayList<String>();
	}

	public ArrayList<String> srcList;
	public ArrayList<String> libList;
	public ArrayList<String> mockList;
}
