package pairwisetesting.engine.am;

public abstract class OAProvider {

	/**
	 * the number of levels and the default is 2-level
	 */
	protected int t = 2;

	/**
	 * @param t
	 *           the number of levels
	 * 
	 */
	public OAProvider(int t) {
		this.t = t;
	}
	
	public OAProvider() {
		
	}

	/**
	 * @param m
	 *            the number of factors
	 * @return the OA based on the specific implementation
	 */
	public abstract int[][] get(int m);

}
