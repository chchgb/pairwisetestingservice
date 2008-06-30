package pairwisetesting.metaparameterparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.coredomain.MetaParameterException;

/**
 * Extract Factors/Levels from Word
 *
 */
public class WordMetaParameterProvider extends FileMetaParameterProvider {

	public WordMetaParameterProvider(String filePath) {
		super(filePath);
	}

	public MetaParameter get() throws MetaParameterException {
		MetaParameter mp = new MetaParameter(2);
		String command = String.format("python word_extractor.py %s %s", filePath, ":");
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream("word_extractor_output.txt"), "utf-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				extractFactorLevels(mp, line);
			}
			in.close();
			return mp;
		} catch (Exception e) {
			throw new MetaParameterException(e);
		}
	}

}
