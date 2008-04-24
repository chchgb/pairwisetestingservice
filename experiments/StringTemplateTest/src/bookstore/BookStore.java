package bookstore;
import java.util.HashMap;


import com.google.common.collect.Maps;

public class BookStore {
	public double computeDiscountedPrice(int level, AccountType accountType, String coupon) {
		double fixedPrice = 100;
		HashMap<Integer, Integer> levelMap = Maps.newHashMap();
		levelMap.put(1, 90);
		levelMap.put(2, 80);
		levelMap.put(3, 70);

		HashMap<String, Integer> couponMap = Maps.newHashMap();
		couponMap.put("C001", 5);
		couponMap.put("C002", 10);
		couponMap.put("C003", 20);

		double discountedPrice = 0;

		switch (accountType) {
		case NORMAL:
			discountedPrice = fixedPrice * levelMap.get(level) / 100
					- couponMap.get(coupon);
			break;
		case STUDENT:
			discountedPrice = fixedPrice * 70 / 100 - couponMap.get(coupon);
			break;
		case INTERNAL:
			discountedPrice = fixedPrice * 60 / 100 - couponMap.get(coupon);
			 // Double mode fault
//			 if (level == 2) {
//				 discountedPrice = discountedPrice - 1;
//			 }
			break;
		}

		return discountedPrice;
	}
}