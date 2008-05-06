package pairwisetesting.util;

public class MathUtil {
	
	/**
	 * Check if n is 2^s - 1
	 */
	public static boolean is_2sMinusOne(int n) {
		int s = 1;
		while (true) {
			int res = (int) Math.pow(2, s) - 1;
			if (n == res) {
				return true;
			} else if (res > n) {
				return false;
			} else {
				s++;
			}
		}
	}

	/**
	 * Check if n is a prime
	 */
	public static boolean isPrime(int n) {
		boolean prime = true;
		for (int i = 3; i <= Math.sqrt(n); i += 2) {
			if (n % i == 0) {
				prime = false;
				break;
			}
		}
		if ((n % 2 != 0 && prime && n > 2) || n == 2) {
			return true;
		} else {
			return false;
		}
	}
}
