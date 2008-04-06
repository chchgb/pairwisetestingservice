package pairwisetesting.engine.am;

public interface IOAProvider {

	/**
	 * @param numOfFactors  the number of factors
	 * @return  the OA based on the specific implementation
	 */
	int[][] get(int numOfFactors);

}
