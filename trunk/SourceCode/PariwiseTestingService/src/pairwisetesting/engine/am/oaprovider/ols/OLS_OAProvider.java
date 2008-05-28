package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

/**
 * The OA provider based on OLS
 */
public abstract class OLS_OAProvider extends OAProvider {
	
	private OLS_Provider ols_Provider;

	/**
	 * @param t
	 *            the number of levels
	 * 
	 */
	public OLS_OAProvider(int t) {
		super(t);
		if (MathUtil.isPrime(t)) {
			this.setOLS_Provider(new Rp_OLS_Provider());
		} else {
			this.setOLS_Provider(new Poly_OLS_Provider());
		}
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
	protected void fillOLSInOA(int[][] oa, List<int[][]> OLS_list,
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

	public OLS_Provider getOLS_Provider() {
		return ols_Provider;
	}

	public void setOLS_Provider(OLS_Provider ols_Provider) {
		this.ols_Provider = ols_Provider;
	}

}
