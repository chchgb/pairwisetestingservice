package pairwisetesting.execution;



import org.antlr.stringtemplate.AttributeRenderer;

public class FirstCharRenderer implements AttributeRenderer {
	public String toString(Object o, String formatName) {
        if (formatName.equals("capitalize")) {
            return "" + (char)(toString(o).charAt(0) - 32) + toString(o).substring(1);
        } else if (formatName.equals("camelize")) {
        	return "" + (char)(toString(o).charAt(0) + 32) + toString(o).substring(1);
        } else {
        	return toString(o);
        }
    }

	@Override
	public String toString(Object o) {
		return o.toString();
	}
}
