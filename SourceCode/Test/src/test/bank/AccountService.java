package test.bank;

public class AccountService {

	private IAccountManager manager;
	private Logger logger;
	private double result;

	public double withdraw(String accountId, double amount) {
		logger.log(accountId);
		manager.beginTransaction();
		double newBalance = getNewBalanceAfterWithDraw(accountId, amount);
		manager.commit();
		logger.log(amount);
		return newBalance;
	}
	
	private double getNewBalanceAfterWithDraw(String accountId, double amount) {
		return manager.withdraw(accountId, amount);
	}
	
	public double checkInvocationWithReturnValue(String accountId, double amount) {
		
		// Local initialization
		double temp = manager.withdraw(accountId, amount);
		
		// Local assignment
		temp = manager.withdraw(accountId, amount);
		
		// Field assignment
		this.result = manager.withdraw(accountId, amount);
		
		// Array initialization
		double arr[] = new double[] {manager.withdraw(accountId, amount)};
		
		// Array element assignment
		arr[0] = manager.withdraw(accountId, amount);
		
		// Constructor argument
		Double d = new Double(manager.withdraw(accountId, amount));
		
		// method argument
		fun(manager.withdraw(accountId, amount));
		
		// condition statement
		boolean res = !(manager.withdraw(accountId, amount) > 100 && 5 < amount);
		
		// if statement
		if (manager.needClose()) {
			
		}
		
		// while statement
		while (manager.needClose()) {
			
		}
		
		// do statement
		do {
			
		} while (manager.needClose());
		
		// for statement
		for (int i = 0; manager.needClose(); i++) {
			
		}
		
		// return statement
		return manager.withdraw(accountId, amount);

	}
	
	public void fun(double input) {
		
	}
	
	public void checkInvocationWithLoop(String accountId, double amount) {
		
		for (int i = 0; i < 10; i++) {
			manager.withdraw(accountId, amount);
		}
		
		int[] arr = {1, 2, 3, 4, 5, 6, 7};
		for (int i : arr) {
			manager.withdraw(accountId, amount);
		}
		
		int i = 0;
		double sum = 0;
		while (i < 5) {
			sum += manager.withdraw(accountId, amount);
			i++;
		}
		
		i = 0;
		sum = 0;
		do {
			double result = manager.withdraw(accountId, amount);
			sum += result;
			i++;
		} while (i < 5);
		
		if (manager.needClose()) {
			;
		}
		
		while (manager.needClose()) {
			;
		}
		
		do {
			;
		} while (manager.needClose());
	}
	
	public void checkInvocationWithTryCatchFinally(String accountId, double amount) {
		manager.beginTransaction();
		try {
			double newBalance = manager.withdraw(accountId, amount);
			manager.commit();
		} catch (Exception e) {
			manager.rollback();
		} finally {
			manager.releaseCollection();
		}
	}
	
	public void checkInvocationWithIfElse(String accountId, double amount) {
		manager.beginTransaction();
		try {
			double newBalance = manager.withdraw(accountId, amount);
			manager.commit();
		} catch (Exception e) {
			manager.rollback();
		} finally {
			if (manager.needClose()) {
				manager.releaseCollection();
			} 
		}
	}
	
	public double checkInvocationWithAllFeatures(String accountId, double amount) {
		manager.beginTransaction();
		double newBalance = 0;
		
		try {
			
			for (int i = 0; i < 10; i++) {
				newBalance = this.withdraw(accountId, amount);
			}
			
			int i = 0;
			do {
				newBalance = this.deposit(accountId, amount);
				i++;
			} while (i < 10);
			
			manager.commit();
		} catch (Exception e) {
			manager.rollback();
		} finally {
			if (manager.needClose()) {
				manager.releaseCollection();
			} 
		}
		
		for (int i = 0; i < 10; i++) {
			withdraw(accountId, amount);
		}
		
		int j = 0;
		do {
			deposit(accountId, amount);
			j++;
		} while (j < 10);
		
		return newBalance;
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
		return manager.deposit(accountId, amount);
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
