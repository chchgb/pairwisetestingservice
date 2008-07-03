package pairwisetesting.coredomain;

public class MetaParameterException extends Exception {
	private static final long serialVersionUID = 5851204815295097203L;

	public MetaParameterException(String msg) {
		super(msg);
	}
	
	public MetaParameterException(Exception e) {
		super(e);
	}
}
