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

	public void releaseConnection() {
		
	}

	public boolean needClose() {
		return false;
	}

	public Integer getStatus() {
		return 0;
	}

	public String[] getAccounts() {
		return null;
	}

}
