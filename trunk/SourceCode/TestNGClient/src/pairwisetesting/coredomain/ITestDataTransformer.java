package pairwisetesting.coredomain;

public interface ITestDataTransformer {

	/**
	 * Transform the raw test data based on the meta parameter
	 * 
	 * @return test data after transformation
	 */
	public String[][] transform(MetaParameter mp, String[][] rawTestData);

	/**
	 * Null test data transformer with no transformation
	 */
	public static final ITestDataTransformer NULL = new ITestDataTransformer() {
		public String[][] transform(MetaParameter mp, String[][] rawTestData) {
			return rawTestData;
		}
	};
}
