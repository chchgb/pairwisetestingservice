package pairwisetesting.metaparameterparser;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

/**
 * Extract Factors/Levels from Excel
 *
 */
public class ExcelMetaParameterProvider extends FileMetaParameterProvider {

	public ExcelMetaParameterProvider(String filePath) {
		super(filePath);
	}

	public MetaParameter get() throws MetaParameterException {
		Workbook workbook = null;
		MetaParameter mp = new MetaParameter(2);
		try {
			workbook = Workbook.getWorkbook(new File(this.filePath));
			Sheet sheet = workbook.getSheet(0);
			int columns = sheet.getColumns();

			for (int j = 0; j < columns; j++) {

				Cell[] cells = sheet.getColumn(j);
				Factor factor = null;

				for (int i = 0; i < cells.length; i++) {
					String cellContent = cells[i].getContents().trim();
					// Ignore empty cell
					if (cellContent.equals("")) {
						continue;
					}
					if (factor == null) {	
						factor = new Factor(cellContent);
					} else {
						factor.addLevel(cellContent);
					}
				}
				// Ignore empty column
				if (factor != null) {
					mp.addFactor(factor);
				}
			}
			return mp;
		} catch (Exception e) {
			throw new MetaParameterException(e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}

}
