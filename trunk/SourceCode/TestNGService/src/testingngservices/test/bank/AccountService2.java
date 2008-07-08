package testingngservices.test.bank;

public class AccountService2 {

	public AccountService2() {

	}

	public double transfer(Account accountA, Account accountB, double amount) {
		accountA.setBalance(accountA.getBalance() - amount);
		accountB.setBalance(accountB.getBalance() + amount);
		return accountB.getBalance();
	}

}
