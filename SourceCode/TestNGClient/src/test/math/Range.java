package test.math;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

//import pairwisetesting.xml.MetaParameterXMLSerializer;

public class Range {
	
//	private Log log;
	//MetaParameterXMLSerializer metaParameterXMLSerializer;
	public static boolean isBetween(int n, int lower, int upper) {
		if (n <= upper && n >= lower) 
			return true;
		else
			return false;
	}
	
	private Range() {
		//metaParameterXMLSerializer = new MetaParameterXMLSerializer();
//		 log = LogFactory.getLog(Range.class);
	}
	
	public static Range getInstance() {
		return range;
	}
	
	public static Range range = new Range();
	
}