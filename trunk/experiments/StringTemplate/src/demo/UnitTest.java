package demo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UnitTest {
	@Test(dataProvider = "range-provider")
	public void testisBetween(int n, int lower, int upper, boolean expected) {
		Assert.assertEquals(Util.isBetween(n, lower, upper), expected);
	}

	@DataProvider(name = "range-provider")
	public Object[][] rangeData() {
		int lower = 5;
		int upper = 10;
		return new Object[][] { { lower - 1, lower, upper, false },
				{ lower, lower, upper, true },
				{ lower + 1, lower, upper, true },
				{ upper - 1, lower, upper, true },
				{ upper, lower, upper, true },
				{ upper + 1, lower, upper, false }, };
	}
} 
