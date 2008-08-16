package testingngservices.testcasetemplateengine;

import org.antlr.stringtemplate.AttributeRenderer;

/**
 * This class renders the first char of the object's string representation.
 */
class FirstCharRenderer implements AttributeRenderer {
	public String toString(Object o, String formatName) {
		if (formatName.equals("capitalize")) {
			return "" + (char) (toString(o).charAt(0) - 32)
					+ toString(o).substring(1);
		} else if (formatName.equals("camelize")) {
			return "" + (char) (toString(o).charAt(0) + 32)
					+ toString(o).substring(1);
		} else {
			return toString(o);
		}
	}

	public String toString(Object o) {
		return o.toString();
	}
}
