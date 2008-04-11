package pairwisetesting.engine.am;

public interface IOAProviderFactory {
	/**
	 * @param t
	 *            the number of levels
	 * @param m
	 *            the number of factors
	 * @return the proper OAProvider based on t and m
	 */
	OAProvider create(int t, int m);
}
