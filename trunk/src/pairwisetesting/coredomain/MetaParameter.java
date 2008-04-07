package pairwisetesting.coredomain;

import java.util.LinkedHashMap;

public class MetaParameter {

	private int strength = 2;
	private LinkedHashMap<String, Factor> factorMap = new LinkedHashMap<String, Factor>();

	public MetaParameter(int strength) {
		this.strength = strength;
	}

	public MetaParameter() {
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getStrength() {
		return strength;
	}

	public void addFactor(Factor f1) {
		factorMap.put(f1.getName(), f1);
	}

	public Factor getFactor(String factorName) {
		return factorMap.get(factorName);
	}

	public Factor[] getFactors() {
		return factorMap.values().toArray(new Factor[0]);
	}

	public String[] getFactorNames() {
		return factorMap.keySet().toArray(new String[0]);
	}

	public String getLevelOfFactor(String factorName, int index) {
		return getFactor(factorName).getLevel(index);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof MetaParameter))
			return false;
		MetaParameter mp = (MetaParameter) other;
		return (strength == mp.strength) && (factorMap.equals(mp.factorMap));
	}

	public int getNumOfFactors() {
		return factorMap.values().size();
	}

	/**
	 * @return the max number of levels within all the factors
	 */
	public int getMaxNumOfLevels() {
		int max = 0;
		Factor[] factors = this.getFactors();
		for (Factor factor : factors) {
			if (max < factor.getNumOfLevels())
				max = factor.getNumOfLevels();
		}
		return max;
	}

}
