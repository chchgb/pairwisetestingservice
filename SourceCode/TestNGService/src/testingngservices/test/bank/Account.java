package testingngservices.test.bank;

public class Account {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "Account_" + id + "_" + balance + "_" + name;
	}

	private String id;
	private double balance;
	private String name;
}
