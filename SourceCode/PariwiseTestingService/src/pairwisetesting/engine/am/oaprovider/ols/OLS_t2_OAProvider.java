package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

/**
 * This OA provider is based on OLS and its runs is t^2
 */
public class OLS_t2_OAProvider extends OLS_OAProvider {

	/**
	 * @param t
	 *            the number of levels
	 */
	public OLS_t2_OAProvider(int t) {
		super(t);
	}

	/**
	 * @param m
	 *            the number of factors and it should be at most t+1
	 */
	@Override
	public int[][] get(int m) {

		// OA Lt^2(t^m)
		int[][] oa = new int[t * t][m];

		generateFirstColumn(oa);
		generateSecondColumn(oa);

		List<int[][]> OLS_list = getOLS_Provider().generate_OLS(t, m - 2);

		// the start column for the OLS is the third one
		int startColumnForOLS = 2;
		fillOLSInOA(oa, OLS_list, startColumnForOLS, 1);

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

}
