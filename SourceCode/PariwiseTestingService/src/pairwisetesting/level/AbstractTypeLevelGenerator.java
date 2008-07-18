package pairwisetesting.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import pairwisetesting.util.ClassUtil;
import pairwisetesting.util.Directory;

public class AbstractTypeLevelGenerator implements ILevelGenerator {

	private String className;
	private String binaryDir;
	
	public AbstractTypeLevelGenerator(String className, String binaryDir) {
		this.className = className;
		this.binaryDir = binaryDir;
	}

	public String[] generateLevels() {
		ArrayList<String> dir = new ArrayList<String>();

		Class<?> targetClass = ClassUtil.getClass(className);
		Directory.TreeInfo treeInfo = Directory.walk(binaryDir, ".*[.]class$");
		for (File javaFile : treeInfo) {
			String str = javaFile.getPath();
			String currentClassName
				= str.replace(File.separator, ".").replace(binaryDir + ".", "").replace(".class", "");
			Class<?> clazz = ClassUtil.getClass(currentClassName);

			// System.out.println("=>" + clazz);
			// System.out.println(Arrays.asList(clazz.getInterfaces()));
			if (!isAbstract(clazz)
					&& (containsInterface(clazz, targetClass)
						|| isSuperClass(clazz, targetClass))) {
				dir.add(currentClassName);
			}
		}
		return dir.toArray(new String[0]);
	}
	
	public boolean isAbstract(Class<?> clazz) {
		return (ClassUtil.isInterface(clazz) || ClassUtil.isAbstractClass(clazz));
	}
	
	private boolean containsInterface(Class<?> dstClass, Class<?> srcClass) {
		if (dstClass == null) return false;
		return (Arrays.asList(dstClass.getInterfaces()).contains(srcClass)
				|| containsInterface(dstClass.getSuperclass(), srcClass));
	}
	private boolean isSuperClass(Class<?> dstClass, Class<?> srcClass) {
		Class<?> superClass = dstClass.getSuperclass();
		if (superClass == null) return false;
		return (superClass == srcClass || isSuperClass(superClass, srcClass));
	}
}
