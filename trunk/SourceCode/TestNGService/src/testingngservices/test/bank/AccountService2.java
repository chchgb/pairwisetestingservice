package testingngservices.test.bank;

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

}