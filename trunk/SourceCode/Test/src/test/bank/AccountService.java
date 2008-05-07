package test.bank;

public class AccountService {

	private IAccountManager manager;
	private Logger logger;

	public double withdraw(String accountId, double amount) {
		logger.log(accountId);
		manager.beginTransaction();
		double newBalance = getNewBalanceAfterWithDraw(accountId, amount);
		manager.commit();
		logger.log(amount);
		return newBalance;
	}
	
	private double getNewBalanceAfterWithDraw(String accountId, double amount) {
		return  manager.withdraw(accountId, amount);
	}
	
	public double deposit(String accountId, double amount) {
		logger.log(accountId);
		manager.beginTransaction();
		double newBalance = getNewBalanceAfterDeposit(accountId, amount);
		manager.commit();
		logger.log(amount);
		return newBalance;
	}
	
	private double getNewBalanceAfterDeposit(String accountId, double amount) {
		return  manager.deposit(accountId, amount);
	}
	
	public double transfer(String accountIdA, String accountIdB, double amount) {
		manager.beginTransaction();
		this.withdraw(accountIdA, amount);
		double newBalanceB = this.deposit(accountIdB, amount);
		manager.commit();
		return newBalanceB;
	}
	

	public void setIAccountManager(IAccountManager manager) {
		this.manager = manager;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
