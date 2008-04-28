package pairwisetesting.coredomain;

import pairwisetesting.exception.MetaParameterException;

public interface IMetaParameterProvider {

	MetaParameter get() throws MetaParameterException;
	public void setSchemaPath(String schemaPath);

}
