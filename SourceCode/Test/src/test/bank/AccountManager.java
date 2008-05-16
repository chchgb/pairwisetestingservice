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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseCollection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needClose() {
		// TODO Auto-generated method stub
		return false;
	}

}
