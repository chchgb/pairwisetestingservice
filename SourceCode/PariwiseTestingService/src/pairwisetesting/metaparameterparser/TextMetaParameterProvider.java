package pairwisetesting.metaparameterparser;

import java.util.List;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;
import pairwisetesting.util.TextFile;

public class TextMetaParameterProvider extends FileMetaParameterProvider {

	public TextMetaParameterProvider(String filePath) {
		super(filePath);
	}

	public MetaParameter get() throws MetaParameterException {
		MetaParameter mp = new MetaParameter(2);
		try {
			List<String> lines = new TextFile(filePath);
			for (String line : lines) {
				extractFactorLevels(mp, line);
			}
			return mp;
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
	}

}
