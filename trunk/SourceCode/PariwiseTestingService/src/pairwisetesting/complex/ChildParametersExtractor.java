package pairwisetesting.complex;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ChildParametersExtractor {

	public Parameter[] getParameters(String className) {
		ArrayList<Parameter> list = new ArrayList<Parameter>(); 
		
		try {
			Field[] fields = Class.forName(className).getDeclaredFields();
			for (Field f : fields) {
				if (isSimpleType(f.getType())) {
					Parameter p = new SimpleParameter(f.getType().getName(), f.getName());
					list.add(p);
				} else {
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
	
	private boolean isSimpleType(Class<?> clazz){
		return clazz.isPrimitive() || clazz == String.class;
	}
}
