package pairwisetesting.engine.am;

import java.util.ArrayList;

public class Rp_OLS_p2_OAProvider extends OAProvider {

	/**
	 * @param t
	 *            the number of levels and it should be a prime
	 * 
	 */
	public Rp_OLS_p2_OAProvider(int t) {
		this.t = t;
	}

	/**
	 * @param m
	 *            the number of factors and it should be at most t+1
	 * @see pairwisetesting.engine.am.OAProvider#get(int)
	 */
	@Override
	public int[][] get(int m) {

		// OA Lt^2(t^m)
		int[][] oa = new int[t * t][m];

		generateFirstColumn(oa);
		generateSecondColumn(oa);

		ArrayList<int[][]> OLS_list = generateRp_OLS(t, m - 2);

		// the start column for the OLS is the third one
		int columnForOLS = 2;

		for (int[][] square : OLS_list) {
			for (int row = 0; row < square.length; row++) {
				for (int column = 0; column < square.length; column++) {
					oa[row * square.length + column][columnForOLS] = square[row][column];
				}
			}
			// the column for the next LS
			columnForOLS++;
		}

		return oa;
	}

	// 111.. 222.. 333.. ...
	private void generateFirstColumn(int[][] oa) {
		for (int value = 1; value <= this.t; value++) {
			for (int i = 0; i < this.t; i++) {
				oa[(value - 1) * this.t + i][0] = value;
			}
		}
	}

	// 123.. 123.. 123.. ...
	private void generateSecondColumn(int[][] oa) {
		for (int i = 0; i < this.t; i++) {
			for (int value = 1; value <= this.t; value++) {
				oa[i * this.t + value - 1][1] = value;
			}
		}
	}

	/**
	 * @param p
	 *            the order of the OLS and it should be a prime
	 * @param n
	 *            the * number of OLS and it should be at most p-1
	 */
	private ArrayList<int[][]> generateRp_OLS(int p, int n) {
		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);

		// generate n OLS
		for (int i = 0; i < n; i++) {
			int interval = i + 1;
			OLS_list.add(generateLS(p, interval));
		}

		return OLS_list;
	}

	/**
	 * @param p
	 *            the order of the LS and it should be a prime
	 * @param interval
	 *            the interval used to generate LS
	 */
	private int[][] generateLS(int p, int interval) {
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
