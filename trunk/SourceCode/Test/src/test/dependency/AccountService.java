package test.dependency;

public class AccountService {

	private IAccountManager iam;
	private AbstractAccountManager absmgr;
	private Logger l;

	public IAccountManager getAccountManager() {
		return iam;
	}

	public void setAccountManager(IAccountManager iam) {
		this.iam = iam;
	}
}
