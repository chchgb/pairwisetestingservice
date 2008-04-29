package pairwisetesting.coredomain;

import java.util.ArrayList;

import pairwisetesting.exception.MetaParameterException;

public interface IMetaParameterProvider {

	MetaParameter get() throws MetaParameterException;
	//MetaParameter get(ArrayList<ArrayList<String>> metaList);

}
