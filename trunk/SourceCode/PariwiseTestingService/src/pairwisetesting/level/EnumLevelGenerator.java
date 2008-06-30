package pairwisetesting.level;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class EnumLevelGenerator implements ILevelGenerator {
	private Class<? extends Enum> enumType;
	
	public EnumLevelGenerator(Class<? extends Enum> enumType) {
		this.enumType = enumType;
	}
	public String[] generateLevels() {
		Enum[] enumConstants = enumType.getEnumConstants();
		ArrayList<String> levels = new ArrayList<String>();
		for (Enum e : enumConstants) {
			levels.add(e.name());
		}
		return levels.toArray(new String[0]);
	}

}
