package test.bank;

public interface IAccountManager {

	void beginTransaction();
	double withdraw(String accountId, double amount);
	double deposit(String accountId, double amount);
	void commit();

}