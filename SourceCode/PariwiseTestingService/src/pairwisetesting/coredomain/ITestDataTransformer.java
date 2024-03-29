package pairwisetesting.coredomain;

import com.google.common.base.Preconditions;

/**
 * This interface acts as the transformer to transform the raw test data to test
 * data that are friendly to the end user.
 * 
 * @see MetaParameter
 * @see pairwisetesting.engine.am.OATestDataTransformer
 * @see pairwisetesting.engine.jenny.JennyTestDataTransformer
 */
public interface ITestDataTransformer {

	/**
	 * Transforms the raw test data based on the meta parameter.
	 * 
	 * @param mp
	 *            the meta parameter
	 * @param rawTestData
	 *            the raw test data generated by engine
	 * @return the transformed test data
	 * @throws NullPointerException
	 *             if {@code mp} or {@code rawTestData} is null
	 */
	public String[][] transform(MetaParameter mp, String[][] rawTestData);

	/**
	 * Null test data transformer with no transformation.<p>
	 * This design technique is borrowed from Robert C. Martin's book
	 * "Agile Software Development", Chinese edition, page 172.
	 */
	public static final ITestDataTransformer NULL = new ITestDataTransformer() {
		/* (non-Javadoc)
		 * @see pairwisetesting.coredomain.ITestDataTransformer#transform(pairwisetesting.coredomain.MetaParameter, java.lang.String[][])
		 */
		public String[][] transform(MetaParameter mp, String[][] rawTestData) {
			Preconditions.checkNotNull(mp, "meta parameter");
			Preconditions.checkNotNull(rawTestData, "raw test data");
			return rawTestData;
		}
	};
}
