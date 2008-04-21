package demo;

public class Util {
	public static boolean isBetween(int n, int lower, int upper) {
		if (n <= upper && n >= lower) 
			return true;
		else
			return false;
	}
}
