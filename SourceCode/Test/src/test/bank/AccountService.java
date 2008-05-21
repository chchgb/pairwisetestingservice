package test.bank;

public class AccountService {

	private IAccountManager manager;
	private Logger logger;
	private double result;
	
	public AccountService() {
		
	}
	
	public AccountService(IAccountManager manager) {
		this.manager = manager;
	}

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
		
		// ? :
		int i = manager.needClose() ? 0 : -1;
		
		// && 
		boolean b = manager.needClose() && amount > 40;
		
		// |
		int s = manager.getStatus() | 2;
		
		// !
		boolean boo = !manager.needClose();
		
		// instanceof expression
		boolean isInstance = manager.getStatus() instanceof Integer;
		
		// Case expression
		int j = (int)manager.withdraw(accountId, amount) | 8;
		
		// for each statement
		for (String account : manager.getAccounts()) {
			
		}
		
		// return statement
		return manager.withdraw(accountId, amount);

	}
	
	public void fun(double input) {
		
	}
	
	public void checkJMockInvocation1(String accountId, double amount) {
		manager.withdraw(accountId, amount);
		manager.deposit(accountId, amount);
		
		int i = 0;
		do {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			i++;
		} while (i > 10);		
		
		manager.withdraw(accountId, amount);
		manager.deposit(accountId, amount);
		
		i = 0;
		do {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			i++;
		} while (i > 10);

	}
	
	public void checkJMockInvocation2(String accountId, double amount) {
		manager.withdraw(accountId, amount);
		manager.deposit(accountId, amount);
		
		int i = 0;
		while (i > 10) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			i++;
		}
		
		manager.deposit(accountId, amount);
		
		i = 0;
		while (i > 10) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			i++;
		}
	}
	
	public void checkJMockInvocation3(String accountId, double amount) {	
		int i = 0;
		
		while (i > 10) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			i++;
		}
		
		for (int j = 0; j < 10; j++) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
		}
		
		if (amount > 50) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
		}
	}
	
	public void checkJMockInvocation4(String accountId, double amount) {
		manager.withdraw(accountId, amount);
		manager.deposit(accountId, amount);
		try {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			manager.commit();
		} catch (Exception e) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			manager.rollback();
		} finally {
			manager.releaseConnection();
		}
		try {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			manager.commit();
		} catch (Exception e) {
			manager.withdraw(accountId, amount);
			manager.deposit(accountId, amount);
			manager.rollback();
		} finally {
			manager.releaseConnection();
		}
		manager.withdraw(accountId, amount);
	}
	
	public double checkInvocationWithConditionalReturn1(String accountId, double amount) {
		if (amount > 50) {
			return 0;
		}
		return manager.withdraw(accountId, amount);
	}
	
	public void checkInvocationWithConditionalReturn2(String accountId, double amount) {
		if (amount > 50) {
			return;
		}
		manager.withdraw(accountId, amount);
	}
	
	public void checkInvocationWithRecursive1(String accountId, double amount) {
		manager.withdraw(accountId, amount);
		double d = manager.deposit(accountId, amount);
		logger.log(accountId);
		logger.log(amount);
		
		if (d > 9) return;
		
		checkInvocationWithRecursive1(accountId, amount);
		
		manager.withdraw(accountId, amount);
		manager.deposit(accountId, amount);
		logger.log(accountId);
		logger.log(amount);
	}
	
	public void checkInvocationWithRecursive2(String accountId, double amount) {
		logger.log(accountId);
		logger.log(amount);
		
		int res = fibonacci(10);
		
		checkInvocationWithRecursive1(accountId, amount);
	}
	
	public int fibonacci(int n) {
		logger.log(n);
		if (n == 0 || n == 1)
			return n;
		else return fibonacci(n - 1) + fibonacci(n - 2);
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
			manager.releaseConnection();
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
				manager.releaseConnection();
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
				manager.releaseConnection();
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
