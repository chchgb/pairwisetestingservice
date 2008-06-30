package pairwisetesting.engine.am.oaprovider;

import pairwisetesting.coredomain.EngineException;
import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.hadamard.H_2s_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_tu_OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

public class OAProviderFactory implements IOAProviderFactory {

	public OAProvider create(int t, int m) throws EngineException {
		if (t == 2 && MathUtil.is_2sMinusOne(m)) {
			return new H_2s_OAProvider();
		} else if (MathUtil.partOfPrimePower(t) != null) {
			if (m <= t + 1) {
				return new OLS_t2_OAProvider(t);
			} else {
				return new OLS_tu_OAProvider(t);
			}
		} else {
			throw new EngineException("No OA provider available");
		}
	}
}
