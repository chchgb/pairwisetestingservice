package pairwisetesting.engine.am.oaprovider;

import pairwisetesting.engine.am.IOAProviderFactory;
import pairwisetesting.engine.am.OAProvider;
import pairwisetesting.engine.am.oaprovider.ols.OLS_t2_OAProvider;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

public class SelfRuleOAProviderFactory implements IOAProviderFactory {

	public OAProvider create(int t, int m) {
		if (MathUtil.partOfPrimePower(t) != null && m <= t + 1) {
			return new OLS_t2_OAProvider(t);
		} else {
			// Find the bigger OA that can handle the request
			int nextPrimePower = 0;
			if (t > m - 2) {
				nextPrimePower = MathUtil.nextPrimePower(t);
			} else {
				nextPrimePower = MathUtil.nextPrimePower(m - 2);
			}
			while (true) {
				if (m <= nextPrimePower + 1) {
					return new OLS_t2_OAProvider(nextPrimePower);
				} else {
					nextPrimePower = MathUtil.nextPrimePower(nextPrimePower);
				}
			}
		}
	}

}
