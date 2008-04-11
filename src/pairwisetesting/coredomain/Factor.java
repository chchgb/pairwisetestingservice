package pairwisetesting.coredomain;

import java.io.Serializable;
import java.util.ArrayList;

public class Factor implements Serializable{

	private static final long serialVersionUID = -4146558040253604753L;
	private String name = "";
	private ArrayList<String> levelList = new ArrayList<String>();

	public Factor(String name) {
		this.name = name;
	}

	public Factor(String name, String[] levels) {
		this(name);
		for (String level : levels) {
			levelList.add(level);
		}
	}

	public String getName() {
		return name;
	}

	public void addLevel(String level) {
		levelList.add(level);
	}

	public String getLevel(int i) {
		return levelList.get(i);
	}

	public int getNumOfLevels() {
		return levelList.size();
	}

	public String[] getLevels() {
		return levelList.toArray(new String[0]);
	}
	
	public ArrayList<String> getLevelList(){
		return levelList;
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Factor))
			return false;
		Factor f = (Factor) other;
		return (name.equals(f.name) && (levelList.equals(f.levelList)));
	}

}
