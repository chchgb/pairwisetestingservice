package test.bank;

public interface IAccountManager {

	void beginTransaction();
	double withdraw(String accountId, double amount) throws RuntimeException;
	double deposit(String accountId, double amount) throws RuntimeException;
	void commit();
	void rollback();
	void releaseCollection();
	boolean needClose();
}