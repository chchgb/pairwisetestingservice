package testingngservices.testcasetemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Convert the input string to proper type
 *
 */
public class Converter {
	
	public static int convertTo(String input, Class<Integer> intType) {
		return Integer.parseInt(input);
	}
	
	public static short convertTo(String input, Class<Short> shortType) {
		return Short.parseShort(input);
	}
	
	public static long convertTo(String input, Class<Long> longType) {
		return Long.parseLong(input);
	}
	
	public static float convertTo(String input, Class<Float> floatType) {
		return Float.parseFloat(input);
	}
	
	public static double convertTo(String input, Class<Double> doubleType) {
		return Double.parseDouble(input);
	}
	
	public static char convertTo(String input, Class<Character> charType) {
		return input.charAt(0);
	}
	
	public static byte convertTo(String input, Class<Byte> byteType) {
		return (byte)input.charAt(0);
	}
	
	public static boolean convertTo(String input, Class<Boolean> booleanType) {
		return Boolean.parseBoolean(input);
	}
	
	public static String convertTo(String input, Class<String> stringType) {
		return input;
	}
	
	public static <T extends Enum<T>> T convertTo(String input, Class<T> enumType) {
		return Enum.valueOf(enumType, input);
	}
	
	public static Date convertTo(String input, Class<Date> dateType) {
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dateFormat.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return date;
	}
	
//	public static void main(String[] args) {
//		System.out.println(Converter.convertTo("345", int.class));
//		System.out.println(Converter.convertTo("345", long.class));
//		System.out.println(Converter.convertTo("345.7f", float.class));
//		System.out.println(Converter.convertTo("345.88", double.class));
//		System.out.println(Converter.convertTo("A", Character.class));
//		System.out.println(Converter.convertTo("STUDENT", AccountType.class).getClass());
//		System.out.println(Converter.convertTo("Name", String.class));
//		System.out.println(Converter.convertTo("True", Boolean.class));
//		System.out.println(Converter.convertTo("2008-4-25", Date.class).getClass());
//	}
}

