package test.math;

//import pairwisetesting.util.LibDependence;

public class Range {
	//LibDependence libDependence;
	public static boolean isBetween(int n, int lower, int upper) {
		if (n <= upper && n >= lower) 
			return true;
		else
			return false;
	}
	
	private Range() {
		
	}
	
	public static Range getInstance() {
		return range;
	}
	
	public static Range range = new Range();
	
}
