package pairwisetesting.engine.am.oaprovider.ols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pairwisetesting.engine.am.oaprovider.gf.ExtendedGaloisField;
import pairwisetesting.engine.am.oaprovider.gf.GaloisField;
import pairwisetesting.engine.am.oaprovider.gf.GaloisPolynomial;
import pairwisetesting.engine.am.oaprovider.util.MathUtil;

public class Poly_OLS_Provider implements OLS_Provider {

	public List<int[][]> generate_OLS(int t, int n) {
		return generatePoly_OLS(t, n);
	}
	
	/**
	 * Generate OLS based on polynomial finite field
	 * 
	 * @param pm
	 *            the order of the OLS and it should be prime power
	 * @param n
	 *            the number of OLS and it should be at most pm-1
	 */
	private List<int[][]> generatePoly_OLS(int pm, int n) {
		
		ArrayList<int[][]> OLS_list = new ArrayList<int[][]>(n);
		
		 // p & m
		int[] parts = MathUtil.partOfPrimePower(pm);
		int[][] firstPoly_LS = generatePoly_LS(parts[0], parts[1]);
		OLS_list.add(firstPoly_LS);
		
		int[][] nextPoly_LS = Arrays.copyOf(firstPoly_LS, firstPoly_LS.length);
		for (int i = 1; i < n; i++) {
			
			int[] secondRow = nextPoly_LS[1];
			
			// 3->2, 4->3, 5->4 ...
			for (int row = 1; row < nextPoly_LS.length - 1; row++) {
				nextPoly_LS[row] = nextPoly_LS[row + 1];
			}
			// second row -> last row
			nextPoly_LS[nextPoly_LS.length - 1] = secondRow;
			
			// add and copy a new LS with raw data to process
			OLS_list.add(nextPoly_LS);
			nextPoly_LS = Arrays.copyOf(nextPoly_LS, nextPoly_LS.length);
		}
		
		return OLS_list;
	}
	
	/**
	 * Generate LS based on polynomial finite field
	 * 
	 * @param p
	 *            the prime part of the order of the OLS
	 * @param m
	 *            the power part of the order of the OLS
	 */
	private int[][] generatePoly_LS(int p, int m) {
		
		GaloisField field = new GaloisField(p);
		field.setDisplayMode(GaloisField.ALFAPOWER);
		ExtendedGaloisField extendedField = new ExtendedGaloisField(field, 'X', m);
		GaloisPolynomial[] alfaPowers = extendedField.getAlfaPowers();
		
		// Map alfaPowers to number in OA
		HashMap<String, Integer> alfaPowerNumberMap = new HashMap<String, Integer>();
		alfaPowerNumberMap.put("0", 1);
		int nextNumber = 2;
		for (GaloisPolynomial alfaPower : alfaPowers) {
			if (!alfaPowerNumberMap.containsKey(alfaPower.toString())) {
				alfaPowerNumberMap.put(alfaPower.toString(), nextNumber);
				nextNumber++;
			}
		}
		
		// System.out.println(alfaPowerNumberMap);
		
		// order of the OLS
		int n = (int)Math.pow(p, m);
		int[][] ls = new int[n][n];
		
		// left-top
		ls[0][0] = 1;
		
		// first row
		for (int j = 1; j < n; j++) {
			ls[0][j] = alfaPowerNumberMap.get(alfaPowers[j-1].toString());
		}
		// first column
		for (int i = 1; i < n; i++) {
			ls[i][0] = alfaPowerNumberMap.get(alfaPowers[i-1].toString());
		}
		
		// addition table
		GaloisPolynomial moduloPoly = extendedField.getModuloPoly();
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < n; j++) {
				GaloisPolynomial remains =  alfaPowers[i-1].sum(alfaPowers[j-1]).divide(moduloPoly)[1];
				ls[i][j] = alfaPowerNumberMap.get(remains.toString());
			}
		}
		
		return ls;
	}

}
