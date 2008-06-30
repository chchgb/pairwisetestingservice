package pairwisetesting.metaparameterparser;

/**
 * Create the right FileMetaParameterProvider based on suffix
 *
 */
public class FileMetaParameterProviderFactory {

	public FileMetaParameterProvider create(String filePath) {
		if (filePath.endsWith("doc")) {
			return new WordMetaParameterProvider(filePath);
		} else if (filePath.endsWith("xls")) {
			return new ExcelMetaParameterProvider(filePath);
		} else if (filePath.endsWith("txt")) {
			return new TextMetaParameterProvider(filePath);
		} else {
			return null;
		}
	}

}
