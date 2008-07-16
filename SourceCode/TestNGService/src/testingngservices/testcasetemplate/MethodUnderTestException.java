package testingngservices.testcasetemplate;

public class MethodUnderTestException extends Exception {

	private static final long serialVersionUID = 7402037845141432533L;

	public MethodUnderTestException(String msg) {
		super(msg);
	}
	
	public MethodUnderTestException(Exception e) {
		super(e);
	}

}
