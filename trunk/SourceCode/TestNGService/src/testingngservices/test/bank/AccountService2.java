package testingngservices.test.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class AccountService2 {

	public AccountService2() {

	}

	public double transfer(Account accountA, Account accountB, double amount) {
		accountA.setBalance(accountA.getBalance() - amount);
		accountB.setBalance(accountB.getBalance() + amount);
		return accountB.getBalance();
	}
	
	public Account transfer2(Account accountA, Account accountB, double amount) {
		accountA.setBalance(accountA.getBalance() - amount);
		accountB.setBalance(accountB.getBalance() + amount);
		return accountB;
	}
	
	public Account transfer3(Account accountA, Account accountB, double[] amounts) {
		for (double amount : amounts) {
			transfer2(accountA, accountB, amount);
		}
		return accountB;
	}
	
	public double[] transfer4(Account accountA, Account accountB, double[] amounts) {
		for (double amount : amounts) {
			transfer2(accountA, accountB, amount);
		}
		return amounts;
	}
	
	public double transfer5(Account accountA, Account accountB, ArrayList<Double> amounts) {
		double sum = 0;
		for (double amount : amounts) {
			sum += amount;
			transfer2(accountA, accountB, amount);
		}
		return sum;
	}
	
	public double transfer6(Account accountA, Account accountB, LinkedList<Double> amounts) {
		double sum = 0;
		for (double amount : amounts) {
			sum += amount;
			transfer2(accountA, accountB, amount);
		}
		return sum;
	}
	
	public double transfer7(Account accountA, Account accountB, HashSet<Double> amounts) {
		double sum = 0;
		for (double amount : amounts) {
			sum += amount;
			transfer2(accountA, accountB, amount);
		}
		return sum;
	}
	
	public double transfer8(Account accountA, Account accountB, HashMap<String, Double> amounts) {
		double sum = 0;
		for (Map.Entry<String, Double> entry : amounts.entrySet()) {
			sum += entry.getValue();
			transfer2(accountA, accountB, entry.getValue());
		}
		return sum;
	}
	
	private double fun1(HashMap<String, String> map) {
		return 0;
	}

}
