package pairwisetesting.engine.am.oaprovider;

import java.util.ArrayList;

import pairwisetesting.engine.am.OAProvider;

public abstract class OLS_OAProvider extends OAProvider {

	/**
	 * @param t
	 *            the number of levels
	 * 
	 */
	public OLS_OAProvider(int t) {
		super(t);
	}

	/**
	 * Fill the OLS into OA 2D array
	 * 
	 * @param oa
	 *            the OA to be filled
	 * @param OLS_list
	 *            the list of OLS
	 * @param startColumnForOLS
	 *            the start column for the OLS
	 * @param numOfLSRepetitions
	 *            the number of Repetitions for LS
	 */
	protected void fillOLSInOA(int[][] oa, ArrayList<int[][]> OLS_list,
			int startColumnForOLS, int numOfLSRepetitions) {
		int columnForNextLS = startColumnForOLS;

		for (int[][] square : OLS_list) {
			for (int i = 0; i < numOfLSRepetitions; i++) {

				int nextRepetitionStartIndex = i * square.length * square.length;

				for (int row = 0; row < square.length; row++) {
					for (int column = 0; column < square.length; column++) {
						oa[nextRepetitionStartIndex + row * square.length
								+ column][columnForNextLS] = square[row][column];
					}
				}
			}
			// the column for the next LS
			columnForNextLS++;
		}
	}

	/**
	 * Generate OLS based on Rp finite field
	 * 
	 * @param p
	 *            the order of the OLS and it should be a prime
	 * @param n
	 *            the * number of OLS and it should be at most p-1
	 */
	protected ArrayList<int[][]> generateRp_OLS(int p, int n) {
		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);

		// generate n OLS
		for (int i = 0; i < n; i++) {
			int interval = i + 1;
			OLS_list.add(generateLS(p, interval));
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
	protected int[][] generateLS(int p, int interval) {
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
