package pairwisetesting.level;

/**
 * Based on Equivalence Partitioning and Boundary Value Analysis
 *
 */
public class EP_BVA_IntegerLevelGenerator implements ILevelGenerator {
	
	private long rangeStart;
	private long rangeEnd;
	
	public EP_BVA_IntegerLevelGenerator(long rangeStart, long rangeEnd) {
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
	}
	public String[] generateLevels() {
		return new String[] {
				"" + (rangeStart - (rangeEnd - rangeStart)),
				"" + (rangeStart - 1),
				"" + rangeStart,
				"" + (rangeStart + 1),
				"" + ((rangeStart + rangeEnd) / 2),
				"" + (rangeEnd - 1),
				"" + rangeEnd,
				"" + (rangeEnd + 1),
				"" + (rangeEnd + (rangeEnd - rangeStart)),
		};
	}

}
