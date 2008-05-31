package pairwisetesting.engine.am;

import pairwisetesting.coredomain.EngineException;

public interface IOAProviderFactory {
	/**
	 * @param t
	 *            the number of levels
	 * @param m
	 *            the number of factors
	 * 
	 * @return the proper OAProvider based on t and m
	 */
	OAProvider create(int t, int m) throws EngineException;
}
