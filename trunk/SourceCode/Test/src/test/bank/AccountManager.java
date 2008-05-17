package test.bank;

public class AccountManager implements IAccountManager {

	public void beginTransaction() {

	}

	public void commit() {

	}

	public double withdraw(String accountId, double amount)  {
		return 0;
	}

	public double deposit(String accountId, double amount) {
		return 0;
	}

	public void rollback() {
		
	}

	public void releaseCollection() {
		
	}

	public boolean needClose() {
		return false;
	}

}
