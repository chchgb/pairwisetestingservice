package pairwisetesting.engine.am.oaprovider;

import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;

public class OAProviderFactory implements IOAProviderFactory {

	public OAProvider create(int t, int m) {
		if (t == 2 && MathUtil.is_2sMinusOne(m)) {
			return new H_2s_OAProvider();
		} else if (MathUtil.isPrime(t) && (m <= t + 1)) {
			return new Rp_OLS_p2_OAProvider(t);
		} else if (MathUtil.isPrime(t) && (m > t + 1)) {
			return new Rp_OLS_pu_OAProvider(t);
		} else {
			return null;
		}
	}
}
