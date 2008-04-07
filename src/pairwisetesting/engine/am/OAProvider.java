package pairwisetesting.engine.am;

public abstract class OAProvider {

	/**
	 * the number of levels and the default is 2-level
	 */
	protected int t = 2;

	/**
	 * @param m
	 *            the number of factors
	 * @return the OA based on the specific implementation
	 */
	public abstract int[][] get(int m);

}
