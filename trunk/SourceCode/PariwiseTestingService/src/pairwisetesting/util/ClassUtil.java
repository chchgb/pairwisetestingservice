package pairwisetesting.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClassUtil {
	
	private static HashMap<String, Class<?>> primitiveClassMap = new HashMap<String, Class<?>>();
	static {
		primitiveClassMap.put("int", int.class);
		primitiveClassMap.put("double", double.class);
		primitiveClassMap.put("float", float.class);
		primitiveClassMap.put("char", char.class);
		primitiveClassMap.put("boolean", boolean.class);
	}

	public static Class<?> getClass(String className) {
		try {
			if (primitiveClassMap.containsKey(className)) {
				return primitiveClassMap.get(className);
			} else {
				ClassLoader cl = Thread.currentThread().getContextClassLoader();			
				return cl.loadClass(className);
				//return Class.forName(className, true, cl);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isAbstractClass(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	public static boolean isInterface(Class<?> clazz) {
		return clazz.isInterface();
	}
	

	public static String getReturnTypeName(String className, String methodName,
			String... parameterTypeNames) {
		return getReturnTypeName(getClass(className), methodName,
				parameterTypeNames);
	}

	/**
	 * @param clazz
	 *            the class to check
	 * @param methodName
	 *            the method's name
	 * @param parameterTypeNames
	 *            the method's parameter type names
	 * @return if parameterTypeNames is empty, then return the first method's
	 *         returnTypeName with the same method name (Mainly used in the case
	 *         of no overloading and parameterTypeNames is not easy to get) if
	 *         parameterTypeNames is given, then return the specific method's
	 *         returnTypeName
	 */
	public static String getReturnTypeName(Class<?> clazz, String methodName,
			String... parameterTypeNames) {
		ArrayList<Class<?>> parameterTypeList = new ArrayList<Class<?>>();
		for (String parameterTypeName : parameterTypeNames) {
			parameterTypeList.add(getClass(parameterTypeName));
		}
		Method method = null;
		try {
			if (parameterTypeList.size() == 0) {
				Method[] allMethods = clazz.getDeclaredMethods();
				for (Method m : allMethods) {
					if (m.getName().equals(methodName)) {
						method = m;
						break;
					}
				}
			} else {
				method = clazz.getDeclaredMethod(methodName, parameterTypeList
						.toArray(new Class<?>[0]));
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return method.getReturnType().getName();
	}

	public static String getSimpleClassName(String className) {
		return className.replaceFirst("(.*[.])", "");
	}
	
	public static boolean isSimpleType(Class<?> clazz){
		return (clazz.isPrimitive() 
				|| clazz == String.class
				|| clazz.isEnum() 
				// Currently array type is considered as simple type
				|| clazz.isArray()); 
	}
	
	public static boolean isSimpleType(String className){
		if (className.equals("void")) {
			return true;
		}
		// Currently only support one dimension array type
		return isSimpleType(getClass(className.replace("[]", "")));
	}
	
	/**
	 * @return the first Method object that matches the given method name
	 */
	public static Method getFirstMethod(Class<?> clazz, String returnTypeName, String methodName) {
		Method[] allMethods = clazz.getDeclaredMethods();
		for (Method m : allMethods) {
			if (m.getReturnType().getCanonicalName().equals(returnTypeName)
					&& m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}
	
	public static boolean containsInterface(Class<?> dstClass, Class<?> srcClass) {
		if (dstClass == null) return false;
		return (Arrays.asList(dstClass.getInterfaces()).contains(srcClass)
				|| containsInterface(dstClass.getSuperclass(), srcClass));
	}
	public static boolean isSuperClass(Class<?> dstClass, Class<?> srcClass) {
		Class<?> superClass = dstClass.getSuperclass();
		if (superClass == null) return false;
		return (superClass == srcClass || isSuperClass(superClass, srcClass));
	}
}
