package pairwisetesting.coredomain;

public class EngineException extends Exception {
	private static final long serialVersionUID = -2629492456304056136L;

	public EngineException(String msg) {
		super(msg);
	}
	
	public EngineException(Exception e) {
		super(e);
	}
}
