package pairwisetesting.plug.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MethodParser {

	private MethodParser() {
		
	}
	
	public static Map parse(String methodDeclaration) {
		
		
		Map result = new HashMap();

		if (methodDeclaration == null || methodDeclaration.trim() == "") {
			return result;
		}
		
		String parameterString = methodDeclaration.substring(methodDeclaration.indexOf("(") + 1, methodDeclaration.indexOf(")"));
		
		String[] parameterArray = parameterString.split(", ");
		ArrayList<String> parameterList = new ArrayList<String>();
		for (String item : parameterArray) {
			parameterList.add(item.trim());
		}
		result.put("parameters", parameterList);
		
		String frontDeclaration = methodDeclaration.substring(0, methodDeclaration.indexOf("("));
		String[] frontArray = frontDeclaration.split(" ");
		
		result.put("methodName", frontArray[frontArray.length - 1].trim());
		if (frontArray.length > 1)
			result.put("returnType", frontArray[frontArray.length -2]);
		if (frontArray.length > 2) {
			ArrayList<String> arrayList = new ArrayList<String>();
			for (int i = 0; i < frontArray.length - 2; i++) {
				arrayList.add(frontArray[i]);
			}

			result.put("accessModifiers", arrayList);
		}

		return result;
	}
	
//	public static void main(String[] args) {
//		
//		HashMap map = (HashMap)MethodParser.parse("public static final void compute(int, String) sdfs");
//
//		System.out.println(map.get("returnType"));
//		System.out.println(map.get("methodName"));
//		for (String item : (ArrayList<String>)map.get("accessModifiers")) {
//			System.out.print(item);
//		}
//		System.out.println();
//		for (String item : (ArrayList<String>)map.get("parameters")) {
//			System.out.print(item);
//		}
//	} 
}
