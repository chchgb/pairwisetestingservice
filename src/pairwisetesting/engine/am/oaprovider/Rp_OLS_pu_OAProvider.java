package pairwisetesting.engine.am.oaprovider;

import java.util.ArrayList;

/**
 * The OA provider based on OLS and Rp finite field and its runs is p^u
 *
 */
public class Rp_OLS_pu_OAProvider extends OLS_OAProvider {

	private int u = 2; // OA contains t^u runs (rows) and the default is 2

	/**
	 * @param t
	 *            currently the number of levels and it should be a prime
	 * 
	 */
	public Rp_OLS_pu_OAProvider(int t) {
		super(t);
	}

	/**
	 * @param m
	 *            the number of factors and it should be at least t+1
	 */
	@Override
	public int[][] get(int m) {

		this.u = m - (this.t - 1);

		// OA Lt^u(u^m)
		int[][] oa = new int[getRuns()][m];

		ArrayList<int[]> rows = generate_tuXuRows();
		fill_tuXuRowsInOA(rows, oa);

		ArrayList<int[][]> OLS_list = generateRp_OLS(this.t, this.t - 1);

		// the start column for the OLS is the u-th one
		int startColumnForOLS = u;
		int numOfLSRepetitions = getNumOfLSRepetitions();
		fillOLSInOA(oa, OLS_list, startColumnForOLS, numOfLSRepetitions);

		return oa;
	}

	private void fill_tuXuRowsInOA(ArrayList<int[]> rows, int[][] oa) {
		for (int i = 0; i < rows.size(); i++) {
			int[] row = rows.get(i);
			for (int j = 0; j < row.length; j++) {
				oa[i][j] = row[j];
			}
		}
	}

	/**
	 * @return the first t^u rows and each row contains u factors
	 */
	private ArrayList<int[]> generate_tuXuRows() {
		ArrayList<int[]> resultRows = new ArrayList<int[]>(getRuns());

		int[] row = new int[this.u];
		generate_tuXuRowsBackTrack(0, row, resultRows);

		return resultRows;
	}

	private void generate_tuXuRowsBackTrack(int nextIndex, int[] row,
			ArrayList<int[]> resultRows) {
		if (nextIndex >= row.length) {
			int[] newRow = new int[row.length];
			System.arraycopy(row, 0, newRow, 0, newRow.length);
			resultRows.add(newRow);
		} else {
			for (int value = 1; value <= this.t; value++) {
				row[nextIndex] = value;
				generate_tuXuRowsBackTrack(nextIndex + 1, row, resultRows);
			}
		}
	}

	private int getRuns() {
		return (int) Math.pow(this.t, this.u);
	}

	private int getNumOfLSRepetitions() {
		return (int) Math.pow(this.t, this.u - 2);
	}
}
