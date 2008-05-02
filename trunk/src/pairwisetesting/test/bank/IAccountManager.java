package pairwisetesting.test.bank;

public interface IAccountManager {

	void beginTransaction();
	double withdraw(String accountId, double amount);
	void commit();

}