package pairwisetesting.engine.am.oaprovider.ols;

import java.util.List;

public interface OLS_Provider {
	/**
	 * Generate OLS based on finite field
	 * 
	 * @param t
	 *            the order of the OLS
	 * @param n
	 *            the number of OLS and it should be at most t-1
	 */
	List<int[][]> generate_OLS(int t, int n);
}
