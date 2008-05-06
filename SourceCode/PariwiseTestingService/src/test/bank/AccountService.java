package test.bank;

public class AccountService {

	private IAccountManager manager;
	private Logger logger;

	public double withdraw(String accountId, double amount) {
		logger.log(accountId);
		manager.beginTransaction();
		double newBalance = manager.withdraw(accountId, amount);
		manager.commit();
		logger.log(amount);
		return newBalance;
	}

	public void setIAccountManager(IAccountManager manager) {
		this.manager = manager;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
