package testingngservices.testcasetemplateengine;

/**
 * Thrown to indicate exceptions related with the test case template engine.
 */
public class TestCaseTemplateEngineException extends Exception {

	private static final long serialVersionUID = -5640639255358903411L;

	public TestCaseTemplateEngineException(String msg) {
		super(msg);
	}
	
	public TestCaseTemplateEngineException(Exception e) {
		super(e);
	}
}
