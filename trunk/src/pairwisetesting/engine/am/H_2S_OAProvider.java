package pairwisetesting.engine.am;

public class H_2S_OAProvider extends OAProvider {

	private Matrix H2; // 2 order Hadamard Matrix

	public H_2S_OAProvider() {
		H2 = new Matrix(2, 2);
		H2.setElement(1, 1, 1);
		H2.setElement(1, 2, 1);
		H2.setElement(2, 1, 1);
		H2.setElement(2, 2, -1);
	}

	/**
	 * @param m
	 *            the number of factors and it should be 2^s - 1
	 * @see pairwisetesting.engine.am.IOAProvider#get(int)
	 */
	public int[][] get(int m) {
		// Remove the all 1 column (happen to be the first column)
		int[][] res = getH2s(m + 1).to2DArray(2);

		// -1 -> 2
		for (int column = 0; column < res[0].length; column++) {
			for (int row = 0; row < res.length; row++) {
				if (res[row][column] == -1) {
					res[row][column] = 2;
				}
			}
		}
		return res;
	}

	/**
	 * @param numOfColumns
	 *            the number of columns and it should be 2^s
	 * @return H2^s Matrix
	 */
	private Matrix getH2s(int numOfColumns) {
		if (numOfColumns == 2) {
			return H2;
		} else {
			return H2.directProduct(getH2s(numOfColumns / 2));
		}
	}

}
