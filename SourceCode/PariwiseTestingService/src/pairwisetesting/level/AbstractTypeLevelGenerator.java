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
			if (!ClassUtil.isAbstractClass(clazz)
					&& (ClassUtil.containsInterface(clazz, targetClass)
						|| ClassUtil.isSuperClass(clazz, targetClass))) {
				dir.add(currentClassName);
			}
		}
		return dir.toArray(new String[0]);
	}
}
