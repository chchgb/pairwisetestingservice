package pairwisetesting.engine.am.oaprovider.ols;

import java.util.ArrayList;
import java.util.List;

public class Rp_OLS_Provider implements OLS_Provider {

	public List<int[][]> generate_OLS(int t, int n) {
		return generateRp_OLS(t, n);
	}
	
	/**
	 * Generate OLS based on Rp finite field
	 * 
	 * @param p
	 *            the order of the OLS and it should be a prime
	 * @param n
	 *            the number of OLS and it should be at most p-1
	 */
	private List<int[][]> generateRp_OLS(int p, int n) {
		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);

		// generate n OLS
		for (int i = 0; i < n; i++) {
			int interval = i + 1;
			OLS_list.add(generateRp_LS(p, interval));
		}

		return OLS_list;
	}

	/**
	 * Generate LS based on the fact that the order is a prime
	 * 
	 * @param p
	 *            the order of the LS and it should be a prime
	 * @param interval
	 *            the interval used to generate LS
	 */
	private int[][] generateRp_LS(int p, int interval) {
		int[][] ls = new int[p][p];
		for (int row = 0; row < p; row++) {
			int value = (row * interval) % p + 1; // start value
			for (int column = 0; column < p; column++) {
				ls[row][column] = value;

				// next value
				value = (value + 1) % p;
				if (value == 0)
					value = p;
			}
		}
		return ls;
	}

}
