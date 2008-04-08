package pairwisetesting.engine.am.oaprovider;

import java.util.Arrays;

public class Matrix {

	private int[][] elements;

	public Matrix(int numOfRows, int numOfColumns) {
		this.elements = new int[numOfRows][numOfColumns];
	}

	public void setElement(int row, int column, int value) {
		this.elements[row - 1][column - 1] = value;
	}

	public int getElement(int row, int column) {
		return this.elements[row - 1][column - 1];
	}

	public int getNumOfRows() {
		return this.elements.length;
	}

	public int getNumOfColumns() {
		return this.elements[0].length;
	}

	public Matrix directProduct(Matrix other) {
		Matrix res = new Matrix(this.getNumOfRows() * other.getNumOfRows(),
				this.getNumOfColumns() * other.getNumOfColumns());
		for (int rowA = 1; rowA <= this.getNumOfRows(); rowA++) {
			for (int colA = 1; colA <= this.getNumOfColumns(); colA++) {
				for (int rowB = 1; rowB <= other.getNumOfRows(); rowB++) {
					for (int colB = 1; colB <= other.getNumOfColumns(); colB++) {
						res.setElement(
								(rowA - 1) * other.getNumOfRows() + rowB,
								(colA - 1) * other.getNumOfColumns() + colB,
								this.getElement(rowA, colA)
										* other.getElement(rowB, colB));
					}
				}
			}
		}
		return res;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Matrix))
			return false;
		Matrix m = (Matrix) other;
		return Arrays.deepEquals(this.elements, m.elements);
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(this.elements);
	}

	/**
	 * @param startColumn the start column to the end column will generate the 2D array 
	 */
	public int[][] to2DArray(int startColumn) {
		int[][] res = new int[this.getNumOfRows()][this.getNumOfColumns() - startColumn + 1];
		for (int column = startColumn; column <= this.getNumOfColumns(); column++) {
			for (int row = 1; row <= this.getNumOfRows(); row++) {
				res[row - 1][column - startColumn] = this.getElement(row, column);
			}
		}
		return res;
	}

}
