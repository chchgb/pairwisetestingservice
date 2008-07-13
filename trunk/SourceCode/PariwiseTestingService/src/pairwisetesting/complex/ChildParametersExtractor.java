package pairwisetesting.complex;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pairwisetesting.util.ClassUtil;


public class ChildParametersExtractor {
	
	public Parameter[] getParameters(String className) {
		ArrayList<Parameter> list = new ArrayList<Parameter>(); 
		
		try {
			Field[] fields = Class.forName(className).getDeclaredFields();
			for (Field f : fields) {
				if (ClassUtil.isSimpleType(f.getType())) {
					Parameter p = new SimpleParameter(f.getType().getName(), f.getName());
					list.add(p);
					
				} else {
					// Need extract its child parameters
					ComplexParameter cp = new ComplexParameter(f.getType().getName(), f.getName());
					Parameter[] parameters = getParameters(f.getType().getName());
					
					for (Parameter child : parameters)
						cp.add(child);
					list.add(cp);
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.toArray(new Parameter[0]);
	}
	
	
//
//	public Parameter[] getParameters(String className,List<String> expandParameter) {
//		ArrayList<Parameter> list = new ArrayList<Parameter>(); 
//		
//		try {
//			Field[] fields = Class.forName(className).getDeclaredFields();
//			for (Field f : fields) {
//				if (ClassUtil.isSimpleType(f.getType())) {
//					Parameter p = new SimpleParameter(f.getType().getName(), f.getName());
//					list.add(p);
//					
//					System.out.println(p);
//					expandParameter.add(p.getFullName());
//				} else {
//					// Need extract its child parameters
//					ComplexParameter cp = new ComplexParameter(f.getType().getName(), f.getName());
//					IParameterVisitor ppv = new PrintParameterVisitor(expandParameter);
//					cp.accept(ppv);
//					Parameter[] parameters = getParameters(f.getType().getName(),expandParameter);
//					for (Parameter child : parameters)
//						cp.add(child);
//					list.add(cp);
//				}			
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list.toArray(new Parameter[0]);
//	}
//	
	
}
