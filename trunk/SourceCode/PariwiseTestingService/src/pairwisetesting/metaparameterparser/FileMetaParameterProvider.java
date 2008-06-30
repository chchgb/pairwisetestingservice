package pairwisetesting.metaparameterparser;

import java.io.File;

import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.IMetaParameterProvider;
import pairwisetesting.coredomain.MetaParameter;

/**
 * MetaParameterProvider from file
 *
 */
public abstract class FileMetaParameterProvider implements IMetaParameterProvider {
	
	protected String filePath;
	
	public FileMetaParameterProvider(String filePath) {
		this.filePath = new File(filePath).getAbsolutePath();
	}
	
	/**
	 * Extract factor and its levels from one line 
	 */
	protected void extractFactorLevels(MetaParameter mp, String line) {
		// System.out.println(line);
		String[] factorAndLevels = line.split("\\s*[,:;]\\s*");
		Factor factor = new Factor(factorAndLevels[0]);
		for (int i = 1; i < factorAndLevels.length; i++) {
			factor.addLevel(factorAndLevels[i]);
		}
		mp.addFactor(factor);
	}

}
