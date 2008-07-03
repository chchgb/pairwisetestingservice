package pairwisetesting.testcasesgenerator;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import pairwisetesting.coredomain.ITestCasesGenerator;
import pairwisetesting.coredomain.MetaParameter;

public class ExcelTestCasesGenerator implements ITestCasesGenerator {

	private String filePath;

	public ExcelTestCasesGenerator(String filePath) {
		this.filePath = filePath;
	}

	public String generate(MetaParameter mp, String[][] testData) {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(
					filePath));
			WritableSheet sheet = workbook.createSheet("Test Cases", 0);

			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			WritableCellFormat format = new WritableCellFormat(font);

			String[] factorNames = mp.getFactorNames();
			for (int i = 0; i < factorNames.length; i++) {
				Label factorLabel = new Label(i, 0, factorNames[i], format);
				sheet.addCell(factorLabel);
			}

			for (int i = 0; i < testData.length; i++) {
				for (int j = 0; j < testData[0].length; j++) {
					Label levelLabel = new Label(j, i + 1, testData[i][j], format);
					sheet.addCell(levelLabel);
				}
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TXTTestCasesGenerator().generate(mp, testData);
	}

}
